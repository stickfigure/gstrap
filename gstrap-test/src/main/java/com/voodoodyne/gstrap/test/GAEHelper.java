package com.voodoodyne.gstrap.test;

import com.google.appengine.api.taskqueue.dev.LocalTaskQueue;
import com.google.appengine.api.taskqueue.dev.QueueStateInfo;
import com.google.appengine.api.taskqueue.dev.QueueStateInfo.TaskStateInfo;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalSearchServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInfo;

import java.util.List;
import java.util.Map;
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

	/**
	 * Use internal APIs in the local task queue to process all tasks... and keep processing them
	 * becuse tasks can enqueue other tasks. Only stop when there is nothing left.
	 */
	@SneakyThrows
	public void awaitTasks(final TestContext ctx) {
		boolean stop = false;
		while (!stop) {
			stop = true;

			final LocalTaskQueue ltq = LocalTaskQueueTestConfig.getLocalTaskQueue();

			for (final Map.Entry<String, QueueStateInfo> queueEntry: ltq.getQueueStateInfo().entrySet()) {
				// copy just in case this changes underneath us
				List<TaskStateInfo> tasks = Lists.newArrayList(queueEntry.getValue().getTaskInfo());

				for (final TaskStateInfo task: tasks) {
					ctx.req(() -> ltq.runTask(queueEntry.getKey(), task.getTaskName()));
					stop = false;
				}
			}
		}
	}
}
