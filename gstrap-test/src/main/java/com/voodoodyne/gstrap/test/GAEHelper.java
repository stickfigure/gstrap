package com.voodoodyne.gstrap.test;

import com.google.appengine.api.taskqueue.dev.LocalTaskQueue;
import com.google.appengine.api.taskqueue.dev.QueueStateInfo;
import com.google.appengine.api.taskqueue.dev.QueueStateInfo.TaskStateInfo;
import com.google.appengine.api.urlfetch.URLFetchServicePb.URLFetchRequest;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalSearchServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig.DeferredTaskCallback;
import com.google.common.collect.Lists;
import com.voodoodyne.gstrap.util.Accumulator;
import com.voodoodyne.gstrap.util.Counter;
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
	private static final int MAX_TASK_RETRIES = 3;

	/**
	 * In order to use Guice in tasks, we need to extend the deferred task callback and manage the
	 * request scope.  Also forces tasks to execute in serial.
	 */
	public static class LoggingDeferredTaskCallback extends DeferredTaskCallback {
		private static final long serialVersionUID = -3346119326008280305L;

		@Override
		public synchronized int execute(URLFetchRequest req) {
			String taskName = extractTaskName(req);

			log.info("Executing task " + taskName);
			try {
				return super.execute(req);
			} catch (RuntimeException ex) {
				log.error("Error executing " + taskName, ex);
				throw ex;
			}
		}

		private String extractTaskName(URLFetchRequest req) {
			return req.toString().replaceAll("(?s).*Payload: .*%", "").replaceAll("(?s)\\\\.*", "");
		}
	}

	/** */
	private final LocalServiceTestHelper helper =
			new LocalServiceTestHelper(
					new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy(),
					new LocalTaskQueueTestConfig()
							.setQueueXmlPath("src/main/webapp/WEB-INF/queue.xml")
							.setDisableAutoTaskExecution(true)
							.setCallbackClass(LoggingDeferredTaskCallback.class),
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
	 *
	 * This relies on the thread local stuff set up by the helper.
	 */
	@SneakyThrows
	public static void awaitTasks(final Requestor requestor) {
		boolean stop = false;
		while (!stop) {
			stop = true;

			final LocalTaskQueue ltq = LocalTaskQueueTestConfig.getLocalTaskQueue();

			//noinspection MismatchedQueryAndUpdateOfCollection
			final Accumulator<String, Counter> taskCounts = new Accumulator<>(Counter::new);

			for (final Map.Entry<String, QueueStateInfo> queueEntry: ltq.getQueueStateInfo().entrySet()) {
				// copy just in case this changes underneath us
				List<TaskStateInfo> tasks = Lists.newArrayList(queueEntry.getValue().getTaskInfo());

				for (final TaskStateInfo task: tasks) {
					final Counter counter = taskCounts.get(task.getTaskName());
					if (++counter.count > MAX_TASK_RETRIES)
						throw new RuntimeException("Too many task retries for " + task);

					requestor.req(() -> ltq.runTask(queueEntry.getKey(), task.getTaskName()));
					stop = false;
				}
			}
		}
	}
}
