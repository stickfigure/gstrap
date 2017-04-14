package com.voodoodyne.gstrap.test;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.TestExtensionContext;
import org.mockito.MockitoAnnotations;

/**
 */
public class MockitoExtension implements BeforeEachCallback {

	@Override
	public void beforeEach(final TestExtensionContext context) throws Exception {
		final Object testInstance = context.getTestInstance();

		MockitoAnnotations.initMocks(testInstance);
	}
}
