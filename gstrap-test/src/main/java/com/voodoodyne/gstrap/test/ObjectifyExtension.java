package com.voodoodyne.gstrap.test;

import com.google.inject.Injector;
import com.googlecode.objectify.ObjectifyFilter;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.TestExtensionContext;

/**
 */
public class ObjectifyExtension implements BeforeEachCallback {

	@Override
	public void beforeEach(final TestExtensionContext context) throws Exception {
		final Injector injector = GuiceExtension.getInjector(context);

		final ObjectifyFilter filter = injector.getInstance(ObjectifyFilter.class);

		final Requestor requestor = injector.getInstance(Requestor.class);
		requestor.addFilter(new ServletFilterAdapter(filter));
	}
}
