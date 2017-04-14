package com.voodoodyne.gstrap.test;

import java.util.concurrent.Callable;

/**
 */
public interface RequestFilter {

	/**
	 *
	 */
	<T> Callable<T> filter(final Callable<T> callable);

}
