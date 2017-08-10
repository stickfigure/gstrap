package com.voodoodyne.gstrap.gae;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.voodoodyne.hattery.AppEngineTransport;
import com.voodoodyne.hattery.HttpRequest;

/**
 * Stuff we just always want set up on GAE
 */
public class GAEServicesModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	public URLFetchService fetchService() {
		return URLFetchServiceFactory.getURLFetchService();
	}

	@Provides
	public HttpRequest hatteryRequest(final ObjectMapper mapper, final URLFetchService fetchService) {
		return new HttpRequest().transport(new AppEngineTransport(fetchService) {
			@Override
			protected FetchOptions defaultOptions() {
				return super.defaultOptions().validateCertificate();
			}
		}).mapper(mapper);
	}
}
