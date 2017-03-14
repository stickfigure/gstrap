package com.voodoodyne.gstrap.objectify;

import java.util.Objects;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Utility functions for dealing with Objectify Entities
 */
public class Entities {
	
	/**
	 * If the two values are not equal, defer a save on the entity. Null-safe.
	 *
	 * Requires that we are in an active transaction
	 *
	 * @return true if the two values differ
	 */
	public static <T> boolean saveIfDifferent(final HasKey<?> entity, final T firstValue, final T secondValue) {
		Transactions.require();

		final boolean different = !Objects.equals(firstValue, secondValue);
		if (different)
			ofy().defer().save().entity(entity);

		return different;
	}

	/**
	 * In addition to the basic logic, executes the closure if different
	 */
	public static <T> boolean saveIfDifferent(final HasKey<?> entity, final T firstValue, final T secondValue, final Runnable runIfDifferent) {
		final boolean different = saveIfDifferent(entity, firstValue, secondValue);
		
		if (different)
			runIfDifferent.run();

		return different;
	}
}
