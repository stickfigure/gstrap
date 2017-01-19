package com.voodoodyne.gstrap.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 */
abstract public class AbstractTest {

	/** */
	private static final SimpleScope FAKE_REQUEST_SCOPE = new SimpleScope();

	/** */
	protected final GAEHelper gae = new GAEHelper();

	/** */
	protected Injector injector;

	private Closeable objectify;

	/** */
	abstract protected Iterable<Module> modules();

	/** */
	@BeforeEach
	public void setUpTestBase(final TestInfo testInfo) {
		MockitoAnnotations.initMocks(this);
		gae.setUp(testInfo);
		objectify = ObjectifyService.begin();

		injector = Guice.createInjector(modules());

		injector.injectMembers(this);
		injector.injectMembers(gae);
	}

	/** */
	@AfterEach
	public void tearDownTestBase() {
		objectify.close();
		gae.tearDown();
		SystemClock.reset();
	}

	/**
	 * Get from the current injector
	 */
	public <T> T inst(Class<T> type) {
		return injector.getInstance(type);
	}

	/** Execute within the context of a request */
	public <T> T req(final Callable<T> callable) throws Exception {
		FAKE_REQUEST_SCOPE.enter();

		try {
			ofy().flush();
			ofy().clear();
			return callable.call();
		} finally {
			ofy().flush();
			ofy().clear();
			FAKE_REQUEST_SCOPE.exit();
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
