package com.voodoodyne.gstrap.lang;

import java.util.Collection;
import java.util.Map;


/**
 * Utility methods for comparison and list testing that are typesafe.  Gets around the annoying problem with
 * Set.contains(), Map.get(), etc which doesn't give you compiler checking if you change the type of the
 * value passed in.
 */
public class Safe
{
	/**
	 * Adds and checks for null.
	 */
	public static <T> boolean addNotNull(Collection<T> coll, T element) {
		if (element == null)
			throw new NullPointerException();
		
		return coll.add(element);
	}

	/**
	 * Collection.contains() but with type safety. 
	 */
	public static <T> boolean contains(final Collection<T> coll, T element) {
		return coll.contains(element);
	}
	
	/**
	 * Collection.remove() but with type safety.
	 */
	public static <T> boolean remove(final Collection<T> coll, T element) {
		return coll.remove(element);
	}
	
	/**
	 * Map.remove() but with type safety.
	 */
	public static <K, V> V remove(final Map<K, V> map, K key) {
		return map.remove(key);
	}
	
	/**
	 * Map.get() but with type safety.
	 */
	public static <K, V> V get(final Map<K, V> map, K key) {
		return map.get(key);
	}

	/**
	 * Map.get() but with type safety.
	 * @return the defaultValue if the result of get() is null
	 */
	public static <K, V> V get(final Map<K, V> map, K key, V defaultValue) {
		final V value = get(map, key);
		return value == null ? defaultValue : value;
	}
}