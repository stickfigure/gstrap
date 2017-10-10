/*
 */

package com.voodoodyne.gstrap;

import com.google.inject.Injector;

import javax.inject.Inject;

/**
 * A static reference to the Injector so that entities and other hard-to-inject places can use
 * injection. Avoid using this where possible, but use it where you must.
 *
 * Requires requestStaticInjection()
 */
public class Inj {
	@Inject
	static private Injector injector;

	static public Injector injector() {
		return injector;
	}

	static public <T> T inst(final Class<T> type) {
		return injector.getInstance(type);
	}
}