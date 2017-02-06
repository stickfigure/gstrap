package com.voodoodyne.gstrap.objectify.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.googlecode.objectify.Key;
import com.voodoodyne.gstrap.objectify.KeyStringer;

import java.io.IOException;

/**
 * Will deserialize an Objectify Key<?> that was serialized with the ShortKeySerializer
 */
@SuppressWarnings("rawtypes")
public class ShortKeyDeserializer extends StdDeserializer<Key> {
	private static final long serialVersionUID = 1L;

	private final KeyStringer keyStringer;

	/**
	 * @param keyStringer */
	public ShortKeyDeserializer(final KeyStringer keyStringer) {
		super(Key.class);
		this.keyStringer = keyStringer;
	}

	/** */
	@Override
	public Key deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String text = jp.getText();
		return Key.create(keyStringer.keyify(text));
	}
}
