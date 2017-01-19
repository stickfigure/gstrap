package com.voodoodyne.gstrap.pebble;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;

import javax.inject.Singleton;

/**
 * Allows injection of a PebbleEngine that reads resource files with .peb.html extension from the classpath.
 */
public class PebbleModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Singleton
	@Provides
	public PebbleEngine pebbleEngine() {
		final ClasspathLoader loader = new ClasspathLoader();
		loader.setSuffix(".peb.html");

		final PebbleEngine pebbleEngine = new PebbleEngine.Builder()
				.loader(loader)
				.strictVariables(true)
				.build();

		return pebbleEngine;
	}
}
