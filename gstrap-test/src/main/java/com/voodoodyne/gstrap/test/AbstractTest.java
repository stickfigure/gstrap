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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Callable;

/**
 */
abstract public class AbstractTest {

	/** */
	protected final GAEHelper gae = new GAEHelper();

	/** */
	private Closeable objectify;

	/** */
	@Inject
	private TestContext ctx;

	/**
	 * You almost certainly want to use Modules.combine() and Modules.override() to multiplex several into one.
	 */
	abstract protected Module module();

	/** */
	@BeforeEach
	public void setUpTestBase(final TestInfo testInfo) {
		MockitoAnnotations.initMocks(this);
		gae.setUp(testInfo);
		objectify = ObjectifyService.begin();

		final RequestScope requestScope = new RequestScope();

		final AbstractModule basicTestModule = new AbstractModule() {
			@Override
			protected void configure() {
				this.bindScope(RequestScoped.class, requestScope);
				this.bind(RequestScope.class).toInstance(requestScope);

				this.bind(HttpServletRequest.class).to(FakeHttpServletRequest.class);
				this.bind(HttpServletResponse.class).to(FakeHttpServletResponse.class);
			}
		};

		final Injector injector = Guice.createInjector(Modules.override(basicTestModule).with(module()));
		injector.injectMembers(this);
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
		return ctx.inst(type);
	}

	/** Execute within the context of a request */
	public <T> T req(final Callable<T> callable) throws Exception {
		return ctx.req(callable);
	}

	/** Execute within the context of a request */
	public void req(final Runnable runnable) throws Exception {
		ctx.req(runnable);
	}

	/** Run everything on the queue. Also any tasks that the tasks add. Stops when totally empty. */
	public void awaitTasks() {
		gae.awaitTasks(ctx);
	}
}
