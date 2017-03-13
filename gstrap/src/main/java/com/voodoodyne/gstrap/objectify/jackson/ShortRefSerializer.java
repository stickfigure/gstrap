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
 * Serializing Ref<?> is a little complicated - if we have a loaded value, render it as-is, but if not, just render the key string.
 */
@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
public class ShortRefSerializer extends JsonSerializer<Ref> {

	private final KeyStringer keyStringer;

	@Override
	public void serialize(Ref value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		Object obj = value.getValue();
		if (obj != null) {
			// writeObject() abandons the serialization context and breaks @JsonView
			//jgen.writeObject(obj);
			// Tatu says that this is better:
			provider.defaultSerializeValue(value.getValue(), jgen);
		} else {
			jgen.writeString(keyStringer.stringify(value.key()));
		}
	}
}
