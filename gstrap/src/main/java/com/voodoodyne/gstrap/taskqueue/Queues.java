package com.voodoodyne.gstrap.taskqueue;

import com.google.appengine.api.taskqueue.QueueFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utilitiy method for dealing with queues
 */
public class Queues
{
	/** 'default' is a java keyword */
	public static QueueHelper deflt() {
		return new SimpleQueueHelper(QueueFactory.getDefaultQueue());
	}

	/**
	 */
	public static QueueHelper named(final String queueName) {
		return new SimpleQueueHelper(queueName);
	}

	/**
	 * Add the tasks to their default queues in the most optimum way possible.
	 */
	public static <T extends GuicyDeferredTask> void add(final Collection<T> tasks) {
		final Map<QueueHelper, List<T>> byQueue = tasks.stream().collect(Collectors.groupingBy(GuicyDeferredTask::defaultQueue));

		byQueue.forEach(QueueHelper::add);
	}
}
