/*
 */

package servlet;

import javax.inject.Singleton;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Hides app from the world, but allows GAE magic calls through.
 */
@Singleton
abstract public class PrivateAuthFilter extends BasicAuthFilter
{
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		// Try to avoid stepping on cron and tasks
		if (request.getHeader("X-AppEngine-Cron") != null
				|| request.getHeader("X-AppEngine-QueueName") != null
				|| request.getServletPath() != null && request.getServletPath().startsWith("/_")	// gae
				|| request.getServletPath() != null && request.getServletPath().startsWith("/img/")) {
			chain.doFilter(request, response);
		} else {
			super.doFilter(request, response, chain);
		}
	}
}