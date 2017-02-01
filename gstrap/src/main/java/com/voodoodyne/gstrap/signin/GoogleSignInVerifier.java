package com.voodoodyne.gstrap.signin;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.JsonFactory;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Verifies Google SignIn identity tokens
 * @link https://github.com/googleplus/gplus-verifytoken-java
 */
@RequiredArgsConstructor
public class GoogleSignInVerifier {

	private final String clientId;
	private final GoogleIdTokenVerifier verifier;
	private final JsonFactory jsonFactory;

	public GoogleIdToken.Payload verify(String tokenString) throws IOException, GeneralSecurityException {
		GoogleIdToken token = GoogleIdToken.parse(jsonFactory, tokenString);

		if (!verifier.verify(token))
			throw new GeneralSecurityException("Token did not verify");

		GoogleIdToken.Payload payload = token.getPayload();

		if (!payload.getAudience().equals(clientId))
			throw new GeneralSecurityException("Audience mismatch");

		if (!payload.getAuthorizedParty().equals(clientId))
			throw new GeneralSecurityException("Client ID mismatch");

		return payload;
	}
}
