package com.voodoodyne.gstrap.objectify.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.voodoodyne.gstrap.objectify.KeyStringer;

/**
 * Call jackson's {@code ObjectMapper.registerModule(new ShortObjectifyJacksonModule())} to enable
 * intelligent serialization and deserialization of various Objectify and GAE classes.
 *
 * These use a shorter, more transparent string form.
 */
public class ShortObjectifyJacksonModule extends SimpleModule {
	private static final long serialVersionUID = 1L;

	public ShortObjectifyJacksonModule(final KeyStringer keyStringer) {
		super("Objectify Short", Version.unknownVersion());

		// Objectify Key
		this.addSerializer(Key.class, new ShortKeySerializer(keyStringer));
		this.addKeySerializer(Key.class, new ShortKeyKeySerializer(keyStringer));
		this.addDeserializer(Key.class, new ShortKeyDeserializer(keyStringer));

		// Objectify Ref
		this.addSerializer(Ref.class, new ShortRefSerializer(keyStringer));
		this.addKeySerializer(Ref.class, new ShortRefKeySerializer(keyStringer));
		this.addDeserializer(Ref.class, new ShortRefDeserializer(keyStringer));

		// Native datastore Key
		this.addSerializer(com.google.appengine.api.datastore.Key.class, new ShortRawKeySerializer(keyStringer.getRaw()));
		this.addKeySerializer(com.google.appengine.api.datastore.Key.class, new ShortRawKeyKeySerializer(keyStringer.getRaw()));
		this.addDeserializer(com.google.appengine.api.datastore.Key.class, new ShortRawKeyDeserializer(keyStringer.getRaw()));
	}

}
