package com.voodoodyne.gstrap.taskqueue;

import com.google.api.client.util.Objects;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Just a single named queue */
@Data
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)	// we can trust Queue equals and hashcode
public class SimpleQueueHelper extends QueueHelper {
	private final Queue queue;

	public SimpleQueueHelper(String name) {
		this(QueueFactory.getQueue(name));
	}

	public SimpleQueueHelper(Queue queue) {
		super(null);
		this.queue = queue;
	}

	private SimpleQueueHelper(final Long countdownMillis, final Queue queue) {
		super(countdownMillis);
		this.queue = queue;
	}

	@Override
	public QueueHelper withCountdownMillis(final long millis) {
		return new SimpleQueueHelper(millis, queue);
	}

	@Override
	public Queue queue() {
		return queue;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("super", super.toString())
				.add("queue", queue.getQueueName())
				.toString();
	}
}
