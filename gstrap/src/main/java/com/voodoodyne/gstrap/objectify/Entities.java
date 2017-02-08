package com.voodoodyne.gstrap.objectify;

import java.util.Objects;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Utility functions for dealing with Objectify Entities
 */
public class Entities {
	
	/**
	 * If the two values are not equal, defer a save on the entity. Null-safe.
	 * @return true if the two values differ
	 */
	public static <T> boolean saveIfDifferent(final Object entity, final T firstValue, final T secondValue) {
		final boolean different = !Objects.equals(firstValue, secondValue);
		if (different)
			ofy().defer().save().entity(entity);

		return different;
	}
}
