package com.voodoodyne.gstrap.test;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.TestExtensionContext;

/**
 */
public class SystemClockExtension implements AfterEachCallback {

	@Override
	public void afterEach(final TestExtensionContext context) throws Exception {
		SystemClock.reset();
	}
}
