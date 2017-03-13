package com.voodoodyne.gstrap.objectify.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.googlecode.objectify.Ref;
import com.voodoodyne.gstrap.objectify.KeyStringer;

import java.io.IOException;

/**
 * Will deserialize an Objectify Ref<?> that was serialized with the ShortRefSerializer.  Note that it doesn't
 * currently work for full objects (ie, serialized when the ref was loaded), only for string keys (ie,
 * serialized when the ref was not loaded).
 */
@SuppressWarnings("rawtypes")
public class ShortRefDeserializer extends StdDeserializer<Ref> {
	private static final long serialVersionUID = 1L;

	private final KeyStringer keyStringer;

	/**
	 * @param keyStringer
	 */
	public ShortRefDeserializer(final KeyStringer keyStringer) {
		super(Ref.class);
		this.keyStringer = keyStringer;
	}

	/**
	 */
	@Override
	public Ref deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		if (jp.getCurrentToken() != JsonToken.VALUE_STRING)
			throw new IllegalStateException("Cannot yet deserialize Refs that were serialized to a full entity object (as opposed to just string key representation)");

		String text = jp.getText();
		return Ref.create(keyStringer.keyify(text));
	}
}
