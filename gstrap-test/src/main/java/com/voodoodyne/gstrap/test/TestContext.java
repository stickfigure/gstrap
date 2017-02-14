package com.voodoodyne.gstrap.test;

import com.google.inject.Injector;

import javax.inject.Inject;
import java.util.concurrent.Callable;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 */
public class TestContext {

	/** */
	private final Injector injector;

	/** */
	private final RequestScope requestScope;

	@Inject
	public TestContext(final Injector injector, final RequestScope requestScope) {
		this.injector = injector;
		this.requestScope = requestScope;
	}

	/**
	 * Get from the current injector
	 */
	public <T> T inst(Class<T> type) {
		return injector.getInstance(type);
	}

	/** Execute within the context of a request */
	public <T> T req(final Callable<T> callable) throws Exception {
		requestScope.enter();

		try {
			ofy().flush();
			ofy().clear();
			return callable.call();
		} finally {
			ofy().flush();
			ofy().clear();
			requestScope.exit();
		}
	}

	/** Execute within the context of a request */
	public void req(final Runnable runnable) throws Exception {
		req(() -> {
			runnable.run();
			return null;
		});
	}

}
