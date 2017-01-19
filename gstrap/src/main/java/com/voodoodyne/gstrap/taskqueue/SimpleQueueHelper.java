package com.voodoodyne.gstrap.taskqueue;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** Just a single named queue */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
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
}
