/*
 */

package com.voodoodyne.gstrap.util;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Some extra behavior for guava Lists
 *
 * @author Jeff Schnitzer
 */
public class Lists2
{
	/** Similar to the Guava one, but handles add/set with unapply() */
	private static class TransformingRandomAccessList<F, T> extends AbstractList<T> implements RandomAccess, Serializable {
		private static final long serialVersionUID = 0;

		final List<F> fromList;
		final ReversibleFunction<F, T> function;

		TransformingRandomAccessList(List<F> fromList, ReversibleFunction<F, T> function) {
			this.fromList = checkNotNull(fromList);
			this.function = checkNotNull(function);
		}

		@Override
		public void clear() {
			fromList.clear();
		}

		@Override
		public T get(int index) {
			return function.apply(fromList.get(index));
		}

		@Override
		public boolean isEmpty() {
			return fromList.isEmpty();
		}

		@Override
		public T remove(int index) {
			return function.apply(fromList.remove(index));
		}

		@Override
		public int size() {
			return fromList.size();
		}

		@Override
		public void add(int index, T element) {
			fromList.add(index, function.unapply(element));
		}

		@Override
		public T set(int index, T element) {
			return function.apply(fromList.set(index, function.unapply(element)));
		}
	}

	/**
	 * Like Lists.transform(), but includes the add(), addAll(), and set() methods.
	 * @param fromList must be RandomAccess
	 */
	public static <F, T> List<T> transform(List<F> fromList, ReversibleFunction<F, T> function) {
		assert fromList instanceof RandomAccess;
		return new TransformingRandomAccessList<F, T>(fromList, function);
	}
}