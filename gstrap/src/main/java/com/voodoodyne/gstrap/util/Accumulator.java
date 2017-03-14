package com.voodoodyne.gstrap.util;

import com.google.common.base.Supplier;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;

/**
 * Basically a map that autocreates values from a supplier.
 */
@RequiredArgsConstructor
public class Accumulator<K, V> extends HashMap<K, V> {
	private final Supplier<V> supplier;

	@Override
	public V get(Object key) {
		V value = super.get(key);
		if (value == null) {
			value = supplier.get();
			put((K)key, value);
		}

		return value;
	}
}
