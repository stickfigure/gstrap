package com.voodoodyne.gstrap.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;

/**
 * Jackson mapper which has been configured properly. Overrides some of Jackson's obnoxious defaults
 * and adds some good stuff like Guava.
 */
public class BetterObjectMapper extends ObjectMapper {
	public BetterObjectMapper() {
		this.registerModule(new GuavaModule());
		this.registerModule(new ParameterNamesModule());
		this.registerModule(new Jdk8Module());
		this.registerModule(new JavaTimeModule());

		this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	/** Pretty prints json */
	public String pretty(Object object) {
		return JSONUtils.pretty(this, object);
	}

	public <T> T read(byte[] content, Class<T> type) {
		try {
			return readValue(content, type);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
