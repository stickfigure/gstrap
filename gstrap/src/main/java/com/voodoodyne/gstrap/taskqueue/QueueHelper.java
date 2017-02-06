package com.voodoodyne.gstrap.taskqueue;

import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueConstants;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Builder;
import com.google.appengine.api.taskqueue.TransientFailureException;
import com.google.common.collect.Iterables;
import com.voodoodyne.gstrap.lang.Strings2;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import static com.googlecode.objectify.ObjectifyService.ofy;

/** Better interface to queues */
@Data
@Slf4j
abstract public class QueueHelper {

	private static final String DISALLOWED_CHARS_IN_TASK_NAME_REGEX = "[^a-zA-Z0-9_-]";

	/** If not null, delay execution by this long */
	private final Long countdownMillis;

	/** @return a new immutable QueueHelper with the countdown */
	abstract public QueueHelper withCountdownMillis(final long millis);

	/** Determines implicit transaction state from ofy().getTransaction() */
	public void add(final DeferredTask payload) {
		this.add(ofy().getTransaction(), payload);
	}

	/** */
	private void add(final Transaction txn, final DeferredTask payload) {
		TaskOptions taskOptions = makeOptions(txn, payload);
		add(txn, taskOptions);
	}

	private TaskOptions makeOptions(final Transaction txn, final DeferredTask payload) {
		TaskOptions taskOptions = Builder.withPayload(payload);

		if (txn == null) {
			taskOptions = taskOptions.taskName(nameOf(payload));
		}

		if (countdownMillis != null) {
			taskOptions = taskOptions.countdownMillis(countdownMillis);
		}

		return taskOptions;
	}

	/** */
	private String nameOf(final DeferredTask task) {
		final String cleaned = task.toString().replaceAll(DISALLOWED_CHARS_IN_TASK_NAME_REGEX, "-");
		return Strings2.chopTo(cleaned, 462) + "--" + UUID.randomUUID();
	}

	/** */
	private void add(final Transaction txn, TaskOptions payload) {
		final Queue queue = queue();

		if (log.isDebugEnabled())
			log.debug("Queue '" + queue.getQueueName() + "' adding " + payload);

		try {
			queue.add(txn, payload);
		} catch (TransientFailureException e) {
			log.warn("Error enqueueing " + payload + ", retrying", e);
			try {
				queue.add(txn, payload);
			} catch (TransientFailureException e1) {
				log.error("Error enqueueing " + payload + ", waiting 2s", e1);
				try { Thread.sleep(2000); } catch (InterruptedException e2) { throw new RuntimeException(e2); }
				queue.add(txn, payload);
			}
		}
	}

	/** Automatically partitions as necessary */
	public void add(final Iterable<? extends DeferredTask> payloads) {
		final Iterable<TaskOptions> opts = Iterables.transform(payloads, payload -> makeOptions(null, payload));

		final Iterable<List<TaskOptions>> partitioned = Iterables.partition(opts, QueueConstants.maxTasksPerAdd());

		for (final List<TaskOptions> piece: partitioned)
			queue().add(null, piece);
	}

	abstract public Queue queue();
}
