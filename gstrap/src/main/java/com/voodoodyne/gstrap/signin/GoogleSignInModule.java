package com.voodoodyne.gstrap.signin;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import lombok.RequiredArgsConstructor;

import javax.inject.Singleton;

/**
 * Enables injection of a GoogleSignInVerifier
 */
@RequiredArgsConstructor
public class GoogleSignInModule extends AbstractModule {

	/**
	 * Looks like "blah-blah.apps.googleusercontent.com"
	 */
	private final String googleClientId;

	@Override
	protected void configure() {
	}

	@Singleton
	@Provides
	public JsonFactory jsonFactory() {
		return new JacksonFactory();
	}

	@Provides
	@Singleton
	public GoogleIdTokenVerifier googleIdTokenVerifier(JsonFactory jsonFactory) {
		return new GoogleIdTokenVerifier(new NetHttpTransport(), jsonFactory);
	}

	@Provides
	@Singleton
	public GoogleSignInVerifier googleSignInVerifier(GoogleIdTokenVerifier googleIdTokenVerifier, JsonFactory jsonFactory) {
		return new GoogleSignInVerifier(googleClientId, googleIdTokenVerifier, jsonFactory);
	}

}
