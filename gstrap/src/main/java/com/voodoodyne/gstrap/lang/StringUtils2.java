/*
 */

package com.voodoodyne.gstrap.lang;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 */
public class StringUtils2 {
	/**
	 * String must have some non-whitespace chars
	 */
	public static boolean notEmpty(String str) {
		return str != null && str.trim().length() > 0;
	}

	/**
	 * Returns a trimmed string or null if trimming produces an empty string.  Also produces null if input is null.
	 */
	public static String trimToNull(String value) {
		if (value == null)
			return null;

		value = value.trim();

		if (value.isEmpty())
			return null;
		else
			return value;
	}

	/** Chop to N chars */
	public static String chopTo(String str, int numberOfChars) {
		if (str.length() > numberOfChars)
			return str.substring(0, numberOfChars);
		else
			return str;
	}

	/** First letter captialized, everything else lowercase */
	public static String capitalize(final String input) {
		return StringUtils.capitalize(input.toLowerCase());
	}

	/** Break down into a set of fragments suitable for partial searching */
	public static Set<String> fragment(final String input) {
		if (StringUtils.isBlank(input))
			return Collections.emptySet();

		final Set<String> fragments = new HashSet<>();

		for (int i = 1; i <= input.length(); i++)
			fragments.add(input.substring(0, i));

		return fragments;
	}
}
