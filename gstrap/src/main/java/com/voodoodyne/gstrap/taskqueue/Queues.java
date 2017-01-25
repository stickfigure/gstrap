package com.voodoodyne.gstrap.taskqueue;

import com.google.appengine.api.taskqueue.QueueFactory;

import java.util.Iterator;

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
	 * Add the tasks to their default queue. They should all be the same type, so they all go to
	 * the default queue of the first task.
	 */
	public static <T extends GuicyDeferredTask> void add(final Iterable<T> tasks) {
		final Iterator<T> iterator = tasks.iterator();
		if (iterator.hasNext()) {
			final T first = iterator.next();
			first.defaultQueue().add(tasks);
		}
	}
}
