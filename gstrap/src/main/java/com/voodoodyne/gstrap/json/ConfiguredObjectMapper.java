package com.voodoodyne.gstrap.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.googlecode.objectify.util.jackson.ObjectifyJacksonModule;

import java.io.IOException;

/**
 * Jackson mapper which has been configured properly. Overrides some of Jackson's obnoxious defaults.
 */
public class ConfiguredObjectMapper extends ObjectMapper {
	public static ConfiguredObjectMapper INSTANCE = new ConfiguredObjectMapper();

	public ConfiguredObjectMapper() {
		this.registerModule(new JodaModule());
		this.registerModule(new ObjectifyJacksonModule());
		this.registerModule(new GuavaModule());

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
