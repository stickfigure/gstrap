package com.voodoodyne.gstrap.taskqueue;

import com.google.api.client.util.Objects;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/** Tries to distribute tasks among a group of queues */
@Data
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
public class MultiQueueHelper extends QueueHelper {
	private static final Random RANDOM = new Random();

	private final List<Queue> queues;

	public MultiQueueHelper(final Queue... queues) {
		this(Arrays.asList(queues));
	}

	public MultiQueueHelper(final List<Queue> queues) {
		this(null, queues);
	}

	public MultiQueueHelper(final String... names) {
		this(namesToQueues(names));
	}

	private MultiQueueHelper(final Long countdownMillis, final List<Queue> queues) {
		super(countdownMillis);
		this.queues = queues;
	}

	@Override
	public QueueHelper withCountdownMillis(final long millis) {
		return new MultiQueueHelper(millis, queues);
	}

	@Override
	public Queue queue() {
		return queues.get(RANDOM.nextInt(queues.size()));
	}

	private static Queue[] namesToQueues(final String[] names) {
		final Queue[] queues = new Queue[names.length];
		for (int i=0; i<names.length; i++)
			queues[i] = QueueFactory.getQueue(names[i]);

		return queues;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("super", super.toString())
				.add("queues", queues.stream().map(Queue::getQueueName).collect(Collectors.joining(",", "[", "]")))
				.toString();
	}
}
