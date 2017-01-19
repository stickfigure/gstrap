package com.voodoodyne.gstrap.test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalSearchServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInfo;

import java.util.UUID;

/**
 * @see <a href="http://code.google.com/appengine/docs/java/howto/unittesting.html">Unit Testing in Appengine</a>
 */
@Slf4j
@RequiredArgsConstructor
public class GAEHelper {

	/** */
	private final LocalServiceTestHelper helper =
			new LocalServiceTestHelper(
					new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy(),
					new LocalTaskQueueTestConfig()
							.setQueueXmlPath("src/main/webapp/WEB-INF/queue.xml")
							.setDisableAutoTaskExecution(true),
					new LocalSearchServiceTestConfig());

	/** */
	public void setUp(TestInfo testInfo) {
		// Set a unique appId so datastore keys don't conflict...
		final String appId = testInfo.getTestClass().get().getSimpleName()
				+ "-" + testInfo.getTestMethod().get().getName()
				+ "-" + UUID.randomUUID();

		helper.setEnvAppId(appId);
		helper.setUp();
	}

	/** */
	public void tearDown() {
		helper.tearDown();
	}
}
