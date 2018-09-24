/*
 */

package com.voodoodyne.gstrap;

import com.google.inject.Injector;
import com.google.inject.Key;

import javax.inject.Inject;
import java.lang.annotation.Annotation;

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

	static public <T> T inst(final Class<T> type, final Class<? extends Annotation> annotationType) {
		return injector.getInstance(Key.get(type, annotationType));
	}
}