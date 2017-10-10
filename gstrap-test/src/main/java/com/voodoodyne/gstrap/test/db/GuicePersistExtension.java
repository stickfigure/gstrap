package com.voodoodyne.gstrap.test.db;

import com.google.inject.Injector;
import com.google.inject.persist.PersistFilter;
import com.voodoodyne.gstrap.test.GuiceExtension;
import com.voodoodyne.gstrap.test.Requestor;
import com.voodoodyne.gstrap.test.ServletFilterAdapter;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Optional; if using guice-persist, this runs the PersistFilter around every test.
 */
public class GuicePersistExtension implements BeforeEachCallback, AfterEachCallback {

	@Override
	public void beforeEach(final ExtensionContext context) throws Exception {
		final Injector injector = GuiceExtension.getInjector(context);

		final PersistFilter filter = injector.getInstance(PersistFilter.class);

		final Requestor requestor = injector.getInstance(Requestor.class);
		requestor.addFilter(new ServletFilterAdapter(filter));

		filter.init(null);
	}

	@Override
	public void afterEach(final ExtensionContext context) throws Exception {
		final Injector injector = GuiceExtension.getInjector(context);

		final PersistFilter filter = injector.getInstance(PersistFilter.class);

		filter.destroy();
	}

}
