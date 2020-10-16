package com.voodoodyne.gstrap.test;

import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.concurrent.Callable;

/**
 * Sets up and tears down state as if a request was made to a web container.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ScopeRequestFilter implements RequestFilter {

	/** */
	private final RequestScope requestScope;

	@Override
	public <T> Callable<T> filter(final Callable<T> callable) {
		return () -> {
			requestScope.enter();
			try {
				return callable.call();
			} finally {
				requestScope.exit();
			}
		};
	}
}
