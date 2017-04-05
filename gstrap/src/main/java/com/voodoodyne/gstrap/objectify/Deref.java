package com.voodoodyne.gstrap.objectify;

import com.google.common.collect.Collections2;
import com.googlecode.objectify.Ref;
import com.voodoodyne.gstrap.util.Lists2;
import com.voodoodyne.gstrap.util.ReversibleFunction;

import java.util.Collection;
import java.util.List;


/**
 * Removes Ref<?>s
 */
public class Deref {

	/** Convenient for working with Guava */
	public static class Func<T> implements ReversibleFunction<Ref<T>, T> {
		public static Func<Object> INSTANCE = new Func<>();

		@Override
		public T apply(Ref<T> ref) {
			return deref(ref);
		}

		@Override
		public Ref<T> unapply(T to) {
			return Ref.create(to);
		}
	}

	/** */
	public static <T> T deref(final Ref<T> ref) {
		return ref == null ? null : ref.get();
	}

	/** null-safe */
	public static <T> Ref<T> enref(final T obj) {
		return obj == null ? null : Ref.create(obj);
	}

	/** */
	@SuppressWarnings("unchecked")
	public static <T> List<T> deref(final List<Ref<T>> reflist) {
		return Lists2.transform(reflist, (Func)Func.INSTANCE);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<Ref<T>> enref(final List<T> list) {
		return Lists2.transform(list, (Func)Func.INSTANCE);
	}

	/** */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> deref(final Collection<Ref<T>> reflist) {
		return Collections2.transform(reflist, (Func)Func.INSTANCE);
	}

	@SuppressWarnings("unchecked")
	public static <T> Collection<Ref<T>> enref(final Collection<T> list) {
		return Collections2.transform(list, (Func)Func.INSTANCE);
	}
}
