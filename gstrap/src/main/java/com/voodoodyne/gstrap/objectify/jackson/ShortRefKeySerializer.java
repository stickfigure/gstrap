package com.voodoodyne.gstrap.objectify.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.googlecode.objectify.Ref;
import com.voodoodyne.gstrap.objectify.KeyStringer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * Like ShortRefSerializer, but handles Refs when they are used as Map keys.  Always serializes to the short key string representation.
 */
@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
public class ShortRefKeySerializer extends JsonSerializer<Ref> {

	private final KeyStringer keyStringer;

	@Override
	public void serialize(Ref value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeFieldName(keyStringer.stringify(value.key()));
	}
}
