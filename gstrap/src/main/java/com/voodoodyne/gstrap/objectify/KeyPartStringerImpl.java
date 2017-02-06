package com.voodoodyne.gstrap.objectify;

import com.google.common.base.Preconditions;
import com.googlecode.objectify.Key;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps between key prefixes and kinds using a simple map. Prefixes are case insensitive, case preserving.
 */
public class KeyPartStringerImpl implements KeyPartStringer {
	private final Map<String, String> prefixToKind = new HashMap<>();
	private final Map<String, String> kindToPrefix = new HashMap<>();

	/** Set by the first registered prefix */
	private Integer prefixLength;

	@Data
	@RequiredArgsConstructor
	public static class Prefix {
		private final String kind;
		private final String prefix;

		/**
		 * Associate the kind of the class with the prefix
		 * @param prefix will be toString()ed
		 */
		public Prefix(final Class<?> kind, final Object prefix) {
			this(Key.getKind(kind), prefix.toString());
		}
	}

	public KeyPartStringerImpl(final Prefix... pairs) {
		for (final Prefix pair : pairs) {
			register(pair);
		}
	}

	/** Alternative to passing in on the constructor. However, this is not thread-safe. */
	public void register(final Prefix pair) {
		if (prefixLength == null) {
			prefixLength = pair.getPrefix().length();
		} else {
			Preconditions.checkArgument(pair.getPrefix().length() == prefixLength, "Prefixes must all have the same length; '%s' has a different length", pair.getPrefix());
		}

		final String normalizedPrefix = pair.getPrefix().toUpperCase();

		Preconditions.checkState(!prefixToKind.containsKey(normalizedPrefix), "Tried to register prefix '%s' twice", normalizedPrefix);

		prefixToKind.put(normalizedPrefix, pair.getKind());
		kindToPrefix.put(pair.getKind(), pair.getPrefix());
	}

	private String prefixOf(final String kind) {
		final String prefix = kindToPrefix.get(kind);
		Preconditions.checkArgument(prefix != null, "Unknown kind %s", kind);
		return prefix;
	}

	private String kindOf(final String prefix) {
		final String kind = prefixToKind.get(prefix.toUpperCase());
		Preconditions.checkArgument(kind != null, "Unknown prefix %s", prefix);
		return kind;
	}

	@Override
	public String assemble(final Part part) {
		final String prefix = prefixOf(part.getKind());
		return prefix + part.getIdentifier();
	}

	@Override
	public Part disassemble(final String assembled) {
		final String prefix = assembled.substring(0, prefixLength);
		final String identity = assembled.substring(prefixLength);

		final String kind = kindOf(prefix);
		return new Part(kind, identity);
	}
}
