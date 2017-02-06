package com.voodoodyne.gstrap.lang;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Even more utilities for working with Strings
 */
public class StringsSplit {

	/**
	 * Combine the parts into a single string separated with the separator. Any instances of the
	 * separator will be escaped by doubling it. Can be reversed with splitWithEscape().
	 */
	public static String combineWithEscape(final List<String> parts, final char separator) {
		Preconditions.checkArgument(!parts.isEmpty(), "You can't combine an empty list");

		final StringBuffer result = new StringBuffer(128);

		for (final String part : parts) {
			Preconditions.checkArgument(!part.isEmpty(), "You can't combine an empty string");

			for (int i=0; i<part.length(); i++) {
				final char ch = part.charAt(i);

				if (ch == separator) {
					result.append(separator);
				}

				result.append(ch);
			}

			result.append(separator);
		}

		// We always added one extra separator
		result.deleteCharAt(result.length() - 1);

		return result.toString();
	}

	/**
	 * Split only on single instances of the char, not on doubles. Doubles are turned into singles.
	 * For example, "asdf__asdf_qwer__qwer" becomes ["asdf_asdf", "qwer_qwer"]. Extra separator
	 * chars at beginning or end are ignored.
	 */
	public static List<String> splitWithEscape(final String combined, final char separator) {
		final List<String> result = new ArrayList<>();

		final int lastIndex = combined.length() - 1;

		StringBuilder currentWord = new StringBuilder(128);
		int index = 0;

		while (index <= lastIndex) {
			final char charHere = combined.charAt(index);
			if (charHere == separator) {
				if (index == lastIndex || combined.charAt(index + 1) != separator) {
					if (currentWord.length() > 0) {
						result.add(currentWord.toString());
						currentWord = new StringBuilder(128);
					}
				} else {
					currentWord.append(separator);
					index++;	// skip extra separator
				}
			} else {
				currentWord.append(charHere);
			}

			index++;
		}

		if (currentWord.length() > 0)
			result.add(currentWord.toString());

		return result;
	}

}
