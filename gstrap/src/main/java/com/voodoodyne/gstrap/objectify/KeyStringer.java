package com.voodoodyne.gstrap.objectify;

import com.googlecode.objectify.Key;
import com.voodoodyne.gstrap.objectify.KeyPartStringerImpl.Prefix;

/**
 * <p>Wraps the RawKeyStringer to provide objectify generic {@code Key<?>}s</p>
 *
 * @see RawKeyStringer
 */
public class KeyStringer {

	private final RawKeyStringer raw;

	public KeyStringer(final Prefix... pairs) {
		this.raw = new RawKeyStringer(pairs);
	}

	public KeyStringer(final KeyPartStringer keyPartStringer) {
		this.raw = new RawKeyStringer(keyPartStringer);
	}

	/** Convert key into a pretty but reversible string version */
	public String stringify(final Key<?> key) {
		return raw.stringify(key.getRaw());
	}

	/** Convert stringified key back into a key */
	public <T> Key<T> keyify(final String stringified) {
		return Key.create(raw.keyify(stringified));
	}
}
