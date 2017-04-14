package com.voodoodyne.gstrap.test;

import lombok.Data;

import javax.servlet.Filter;
import javax.xml.ws.Holder;
import java.util.concurrent.Callable;

/**
 * Allows a servlet Filter to be used as a RequestFilter
 */
@Data
public class ServletFilterAdapter implements RequestFilter {

	/** */
	private final Filter filter;

	@Override
	public <T> Callable<T> filter(final Callable<T> callable) {
		return () -> {
			final Holder<T> holder = new Holder<>();

			filter.doFilter(null, null, (request, response) -> {
				try {
					holder.value = callable.call();
				} catch (RuntimeException e) {
					throw e;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});

			return holder.value;
		};
	}
}
