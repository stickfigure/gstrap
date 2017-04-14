package com.voodoodyne.gstrap.test;

import com.google.inject.Injector;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.TestExtensionContext;

/**
 * Separate out the actual injection of things from the guice setup so that other extensions do setup
 * that might affect what gets injected.
 */
public class GuiceInjectExtension implements BeforeEachCallback {
	@Override
	public void beforeEach(final TestExtensionContext context) throws Exception {
		final Injector injector = GuiceExtension.getInjector(context);
		final Object testInstance = context.getTestInstance();

		injector.injectMembers(testInstance);
	}
}
