package com.voodoodyne.gstrap.objectify;

import com.google.common.base.Preconditions;
import com.googlecode.objectify.impl.TransactionImpl;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Utilities for working with transactions
 */
public class Transactions {

	/** Throw an exception if we aren't in a transaction */
	public static void require() {
		Preconditions.checkState(ofy().getTransaction() != null);
	}

	/**
	 * For any given key, do something once in a given transaction. Useful for ensuring that tasks are only
	 * enqueued once.
	 * @param bagKey an arbitrary unique hashable value
	 * @param runnable some code to execute if this is the first time we've seen the hashable value in the transaction
	 */
	public static void once(final Object bagKey, final Runnable runnable) {
		final TransactionImpl txn = (TransactionImpl)ofy().getTransaction();

		if (txn == null) {
			// Like autocommit mode
			runnable.run();
			return;
		}

		final Object before = txn.getBag().put(bagKey, bagKey);
		if (before == null) {
			runnable.run();
		}
	}
}
