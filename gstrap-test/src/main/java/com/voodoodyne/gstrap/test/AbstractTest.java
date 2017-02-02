package com.voodoodyne.gstrap.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.util.Modules;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import com.voodoodyne.gstrap.test.servlet.FakeHttpServletRequest;
import com.voodoodyne.gstrap.test.servlet.FakeHttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Callable;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 */
abstract public class AbstractTest {

	/** */
	private final SimpleScope requestScope = new SimpleScope();

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

		final AbstractModule basicTestModule = new AbstractModule() {
			@Override
			protected void configure() {
				this.bindScope(RequestScoped.class, requestScope);
				this.bind(HttpServletRequest.class).to(FakeHttpServletRequest.class);
				this.bind(HttpServletResponse.class).to(FakeHttpServletResponse.class);
			}
		};
		injector = Guice.createInjector(Modules.override(basicTestModule).with(modules()));

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
