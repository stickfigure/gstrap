package com.voodoodyne.gstrap.test;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * We have to pretend we're a web container and do various kinds of processing per request. This
 * singleton manages that process. Extensions can register filters that will be applied to each
 * request.
 *
 * Works in concert with the GuiceExtension.
 */
@Singleton
public class Requestor {

	/** They will be run in order */
	private final List<RequestFilter> filters = new ArrayList<>();

	public void addFilter(final RequestFilter filter) {
		this.filters.add(filter);
	}

	/** Execute within the context of a request */
	public <T> T req(final Callable<T> callable) throws Exception {
		return wrap(callable, filters.size()-1).call();
	}

	/** Execute within the context of a request; just a wrapper for the Callable version */
	final public void req(final Runnable runnable) throws Exception {
		req(() -> {
			runnable.run();
			return null;
		});
	}

	/** Recursively wrap them, but do it in reverse order by decrementing the index */
	private <T> Callable<T> wrap(final Callable<T> callable, int index) {
		if (index < 0)
			return callable;

		final Callable<T> filtered = filters.get(index).filter(callable);
		return wrap(filtered, index - 1);
	}
}
