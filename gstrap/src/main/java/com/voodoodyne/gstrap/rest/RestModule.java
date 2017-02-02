package com.voodoodyne.gstrap.rest;

import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.ServletModule;
import com.voodoodyne.gstrap.logging.LogCall;
import com.voodoodyne.gstrap.logging.LogCallInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import java.lang.reflect.AnnotatedElement;

/**
 * Binds Resteasy objects with the specified path prefix. Empty prefix is root.
 *
 * All rest endpoint methods get logged.
 */
@Slf4j
@RequiredArgsConstructor
public class RestModule extends ServletModule {

	/** If set to something other than "", update resteasy.servlet.mapping.prefix in web.xml */
	private final String prefix;

	/** Root prefix */
	public RestModule() {
		this("");
	}

	@Override
	protected void configureServlets() {
		final Matcher<AnnotatedElement> jaxrsMethods = Matchers.annotatedWith(LogCall.class)
				.or(Matchers.annotatedWith(GET.class))
				.or(Matchers.annotatedWith(POST.class))
				.or(Matchers.annotatedWith(PUT.class))
				.or(Matchers.annotatedWith(DELETE.class))
				.or(Matchers.annotatedWith(OPTIONS.class))
				.or(Matchers.annotatedWith(HEAD.class));
		bindInterceptor(Matchers.any(), jaxrsMethods, new LogCallInterceptor());

		bind(ObjectMapperContextResolver.class);
		bind(JodaParamConverterProvider.class);

		filter(prefix + "/*").through(GuiceResteasyFilterDispatcher.class);
	}
}
