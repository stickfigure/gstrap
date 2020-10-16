package com.voodoodyne.gstrap.rest;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.voodoodyne.gstrap.lang.Types;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * JSON structure of an error body from the ThrowableMapper
 */
@RequiredArgsConstructor
public class ErrorBody {
	private final Throwable t;

	public String getMessage() {
		return t.getMessage();
	}

	public String getType() {
		return t.getClass().getSimpleName();
	}

	public List<String> getTypes() {
		return Types.getTypes(t, ClientException.class, RuntimeException.class, Exception.class, Throwable.class);
	}

	public ErrorBody getCause() {
		return t.getCause() == null ? null : new ErrorBody(t.getCause());
	}

	@JsonUnwrapped
	public Object getAdditionalProperties() {
		if (t instanceof AdditionalProperties) {
			return ((AdditionalProperties)t).getAdditionalProperties();
		} else {
			return null;
		}
	}
}
