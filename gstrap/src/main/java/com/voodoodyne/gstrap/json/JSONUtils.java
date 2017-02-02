package com.voodoodyne.gstrap.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 */
public class JSONUtils {

	/** Pretty prints json */
	public static String pretty(final ObjectMapper mapper, final Object object) {
		try {
			return mapper.writerWithView(Object.class).withDefaultPrettyPrinter().writeValueAsString(object);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
