package servlet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that only executes once; ignored on re-entrancy.
 */
abstract public class ReentrantFilter extends AbstractFilter {

	/** Key in the request attrs */
	private static final String KEY = ReentrantFilter.class.getName();

	@Override
	final protected void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {

		final Object alreadyDone = request.getAttribute(KEY);
		if (alreadyDone != null) {
			chain.doFilter(request, response);
		} else {
			request.setAttribute(KEY, KEY);
			doFilterOnce(request, response, chain);
		}
	}

	abstract protected void doFilterOnce(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException;
}
