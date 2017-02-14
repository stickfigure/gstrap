package com.voodoodyne.gstrap.objectify;

import com.google.common.base.Preconditions;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Utilities for working with transactions
 */
public class Transactions {

	/** Throw an exception if we aren't in a transaction */
	public static void require() {
		Preconditions.checkState(ofy().getTransaction() != null);
	}
}
