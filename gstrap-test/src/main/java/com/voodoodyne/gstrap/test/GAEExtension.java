package com.voodoodyne.gstrap.test;

import com.voodoodyne.gstrap.test.util.TestInfoContextAdapter;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.TestExtensionContext;

/**
 */
public class GAEExtension implements BeforeEachCallback, AfterEachCallback {

	private static final Namespace NAMESPACE = Namespace.create(GAEExtension.class);

	@Override
	public void beforeEach(final TestExtensionContext context) throws Exception {
		final GAEHelper helper = new GAEHelper();

		context.getStore(NAMESPACE).put(GAEHelper.class, helper);

		helper.setUp(new TestInfoContextAdapter(context));
	}

	@Override
	public void afterEach(final TestExtensionContext context) throws Exception {
		final GAEHelper helper = context.getStore(NAMESPACE).get(GAEHelper.class, GAEHelper.class);
		helper.tearDown();
	}
}
