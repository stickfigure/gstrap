package com.voodoodyne.gstrap.objectify;

import com.google.inject.servlet.ServletModule;
import com.googlecode.objectify.ObjectifyFilter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

/**
 */
@Slf4j
public class ObjectifyModule extends ServletModule {
	@Override
	protected void configureServlets() {
		bind(ObjectifyFilter.class).in(Singleton.class);
		filter("/*").through(ObjectifyFilter.class);
	}
}
