package com.voodoodyne.gstrap.taskqueue;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

/**
 * A deferred task that performs Guice injection on itself before executing.
 * Note that this class needs guice configuration for requestStaticInjection(GuicyDeferredTask.class);
 * All injected fields should be transient! Otherwise they will get serialized and that's probably bad.
 */
@Slf4j
abstract public class GuicyDeferredTask implements DeferredTask
{
	private static final long serialVersionUID = 1L;

	@Inject protected static Injector injector;
	
	/** Perform guice injection and then continue */
	@Override
	public final void run() {
		try {
			log.debug("Running task " + this);
	    	injector.injectMembers(this);
	    	this.run2();
		} catch (RuntimeException ex) {
			log.error("Error running task " + this, ex);
			throw ex;
		}
	}
	
	/** Implement this instead of run(), executed after guice injection */
	abstract protected void run2();

	/**
	 * If you just call Queues.add(), this will be the queue used. You can of course
	 * explicitly add tasks to any queue.
	 */
	abstract public QueueHelper defaultQueue();

	/** Add to the default queue */
	public void add() {
		defaultQueue().add(this);
	}
}
