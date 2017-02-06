package com.voodoodyne.gstrap.objectify.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.appengine.api.datastore.Key;
import com.voodoodyne.gstrap.objectify.KeyStringer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * Configuring this serializer will make native datastore Key objects render as their short string *when they are used as Map keys*.
 */
@RequiredArgsConstructor
public class ShortRawKeyKeySerializer extends JsonSerializer<Key> {

	private final KeyStringer keyStringer;

	@Override
	public void serialize(Key value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeFieldName(keyStringer.stringify(value));
	}
}
