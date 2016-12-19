package com.voodoodyne.gstrap.taskqueue;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Random;

/** Tries to distribute tasks among a group of queues */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class MultiQueueHelper extends QueueHelper {
	private static final Random RANDOM = new Random();

	private final Queue[] queues;

	public MultiQueueHelper(Queue... queues) {
		super(null);
		this.queues = queues;
	}

	public MultiQueueHelper(String... names) {
		this(namesToQueues(names));
	}

	private MultiQueueHelper(final Long countdownMillis, final Queue[] queues) {
		super(countdownMillis);
		this.queues = queues;
	}

	@Override
	public QueueHelper withCountdownMillis(final long millis) {
		return new MultiQueueHelper(millis, queues);
	}

	@Override
	public Queue queue() {
		return queues[RANDOM.nextInt(queues.length)];
	}

	private static Queue[] namesToQueues(final String[] names) {
		final Queue[] queues = new Queue[names.length];
		for (int i=0; i<names.length; i++)
			queues[i] = QueueFactory.getQueue(names[i]);

		return queues;
	}
}
