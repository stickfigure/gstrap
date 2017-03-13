package com.voodoodyne.gstrap.objectify.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.appengine.api.datastore.Key;
import com.voodoodyne.gstrap.objectify.RawKeyStringer;

import java.io.IOException;

/**
 * Will deserialize a google native datastore Key that was serialized with the ShortRawKeySerializer
 */
public class ShortRawKeyDeserializer extends StdDeserializer<Key> {
	private static final long serialVersionUID = 1L;

	private final RawKeyStringer keyStringer;

	/** */
	public ShortRawKeyDeserializer(final RawKeyStringer keyStringer) {
		super(Key.class);
		this.keyStringer = keyStringer;
	}

	/** */
	@Override
	public Key deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String text = jp.getText();
		return keyStringer.keyify(text);
	}
}
