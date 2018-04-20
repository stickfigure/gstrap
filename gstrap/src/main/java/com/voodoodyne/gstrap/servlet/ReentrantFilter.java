package com.voodoodyne.gstrap.servlet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that only executes once per subclass instance; ignored on re-entrancy.
 */
abstract public class ReentrantFilter extends AbstractFilter {

	@Override
	final protected void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
		final String key = "reentrant-" + getClass().getName();

		final Object alreadyDone = request.getAttribute(key);
		if (alreadyDone != null) {
			chain.doFilter(request, response);
		} else {
			request.setAttribute(key, key);
			doFilterOnce(request, response, chain);
		}
	}

	abstract protected void doFilterOnce(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException;
}
