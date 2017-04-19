package com.voodoodyne.gstrap.rest;

/**
 * Extend this to produce a 400-level error code to the client instead of a 500-level error.
 * The difference between this exception hierarchy and the WebApplicationException hierarchy
 * (ClientErrorException etc) is that those will render the Response as contained in the exception;
 * this (and any other random exception) gets rendered by the ThrowableMapper as JSON.
 */
public class ClientException extends RuntimeException {
	public ClientException() {
	}

	public ClientException(final String message) {
		super(message);
	}

	public ClientException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ClientException(final Throwable cause) {
		super(cause);
	}

	public ClientException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
