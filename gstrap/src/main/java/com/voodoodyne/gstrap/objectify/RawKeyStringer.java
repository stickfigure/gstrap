package com.voodoodyne.gstrap.objectify;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.common.base.Preconditions;
import com.voodoodyne.gstrap.lang.StringsSplit;
import com.voodoodyne.gstrap.objectify.KeyPartStringer.Part;
import com.voodoodyne.gstrap.objectify.KeyPartStringerImpl.Prefix;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * <p>The default stringified Key in GAE is an encoded protobuf structure that is long, hard to read, and looks
 * terrible in user facing URLs. This is an alternative strategy which produces unambiguous, unique, transparent keys.
 * The downside is that you have to register prefixes for every kind and the prefixes must all be the same length. Some
 * example keys this will produce:</p>
 *
 * <ul>
 *     <li>TH123 (simple numeric key)</li>
 *     <li>TH123_OT456 (parent/child key, both numeric)</li>
 *     <li>SRZname (single component key but with string name; prefix is SR)</li>
 * </ul>
 *
 * <p>Most numeric keys look great. Multipart keys are separated with underscores. String name keys always have a 'Z'
 * after the prefix to keep them unambiguous with numeric keys.</p>
 *
 * <p>These are not guaranteed to be web-safe; you should still urlencode them if putting them in URLs.</p>
 */
@RequiredArgsConstructor
public class RawKeyStringer {
	/**
	 * If key is string name instead of numeric id, this will be the first char of the value. Without this,
	 * string named keys with all numbers would be indistinguishable.
	 */
	private final char NAME_INDICATOR_CHAR = 'Z';

	/** Separator for parent/child key components */
	private final char SEPARATOR_CHAR = '_';

	private final KeyPartStringer partStringer;

	/** Convenience constructor just uses the default implementation */
	public RawKeyStringer(final Prefix... pairs) {
		this.partStringer = new KeyPartStringerImpl(pairs);
	}

	/** Convert key into a pretty but reversible string version */
	public String stringify(final Key key) {
		final StringBuilder bld = new StringBuilder();
		stringify(key, bld);
		return bld.toString();
	}

	/** Recursive implementation */
	private void stringify(final Key key, final StringBuilder into) {
		Preconditions.checkArgument(key.isComplete(), "Key must have either id or name");

		if (key.getParent() != null) {
			stringify(key.getParent(), into);
			into.append(SEPARATOR_CHAR);
		}

		final Part part = new Part(key.getKind(), escapeIdentifier(key));
		final String assembled = partStringer.assemble(part);
		into.append(assembled);
	}

	/**
	 * Numeric ids are easy, just print them. String names are more complicated - prefix them with the
	 * NAME_INDICATOR_CHAR and escape SEPARATOR_CHAR by printing it twice.
	 */
	private String escapeIdentifier(final Key key) {
		if (key.getName() == null) {
			return Long.toString(key.getId());
		} else {
			final StringBuilder escaped = new StringBuilder();
			escaped.append(NAME_INDICATOR_CHAR);

			for (int i=0; i<key.getName().length(); i++) {
				final char ch = key.getName().charAt(i);

				if (ch == SEPARATOR_CHAR)
					escaped.append(SEPARATOR_CHAR);

				escaped.append(ch);
			}

			return escaped.toString();
		}
	}

	/** Convert stringified key back into a key */
	public Key keyify(final String stringified) {
		final List<String> parts = StringsSplit.splitWithEscape(stringified, SEPARATOR_CHAR);

		Key parent = null;

		for (final String part : parts) {
			parent = keyify(parent, part);
		}

		return parent;
	}

	/** Keyify a particular part of a stringified key, assuming the specified parent */
	private Key keyify(final Key parent, final String assembledPart) {
		final Part part = partStringer.disassemble(assembledPart);

		if (part.getIdentifier().charAt(0) == NAME_INDICATOR_CHAR) {
			return KeyFactory.createKey(parent, part.getKind(), part.getIdentifier().substring(1));
		} else {
			return KeyFactory.createKey(parent, part.getKind(), Long.parseLong(part.getIdentifier()));
		}
	}
}
