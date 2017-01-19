package com.voodoodyne.gstrap.test;

import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.googlecode.objectify.util.FutureNow;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Future;

/**
 * Simple so you only need to override one method
 */
@Singleton
public class BaseURLFetchService implements URLFetchService {

	/** The only method you need to override */
	@Override
	public HTTPResponse fetch(final HTTPRequest httpRequest) throws IOException {
		return URLFetchServiceFactory.getURLFetchService().fetch(httpRequest);
	}

	@Override
	final public HTTPResponse fetch(final URL url) throws IOException {
		return fetch(new HTTPRequest(url));
	}

	@Override
	final public Future<HTTPResponse> fetchAsync(final URL url) {
		return fetchAsync(new HTTPRequest(url));
	}

	@Override
	final public Future<HTTPResponse> fetchAsync(final HTTPRequest httpRequest) {
		try {
			return new FutureNow<>(fetch(httpRequest));
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
