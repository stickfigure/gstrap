/*
 * $Id: AbstractFilter.java 465 2006-05-22 01:30:41Z lhoriman $
 * $URL: https://subetha.googlecode.com/svn/branches/resin/src/org/subethamail/web/util/AbstractFilter.java $
 */

package servlet;

import lombok.Getter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 */
abstract public class AbstractFilter implements Filter
{
	/**
	 */
	@Getter
	private FilterConfig config = null;

	/**
	 */
	public void destroy() {
	}

	/**
	 */
	public void init(final FilterConfig cfg) throws ServletException {
		this.config = cfg;
	}

	/**
	 */
	public final void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		this.doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
	}

	/**
	 */
	abstract protected void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
		throws IOException, ServletException;
}
