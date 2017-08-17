/*
 * $Id$
 */

package com.voodoodyne.gstrap.gae;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * From https://github.com/stickfigure/unsuck/blob/master/src/main/java/unsuck/gae/Cache.java
 *
 * Creates type-safe, namespaced interfaces to the memcache.  For any exception
 * that goes wrong when fetching from memcache, just returns null (or empty).
 *
 * @author Jeff Schnitzer
 */
@Slf4j
public class Memcache<K, V> {
	/** */
	public static class Identifiable<V> {
		IdentifiableValue iv;

		public Identifiable(IdentifiableValue iv) {
			this.iv = iv;
		}

		@SuppressWarnings("unchecked")
		public V getValue() {
			return (V)iv.getValue();
		}
	}

	/** */
	private MemcacheService memCache;

	/**
	 * If non-null, expire all entries after this number of seconds
	 */
	private Integer expireSeconds;

	/**
	 * Create cache interface with default namespace
	 */
	public Memcache(String namespace) {
		this.memCache = MemcacheServiceFactory.getMemcacheService(namespace);
		this.memCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.SEVERE));
	}

	/**
	 * Create cache with explicit namespace and an expiration period
	 */
	public Memcache(String namespace, int expireSeconds) {
		this(namespace);
		this.expireSeconds = expireSeconds;
	}

	/**
	 * Create cache interface with namespace defined by a class
	 */
	public Memcache(Class<?> clazzNamespace) {
		this(clazzNamespace.getSimpleName());
	}

	/** */
	public V get(final K key, final Supplier<V> supplier) {
		final V value = get(key);
		if (value != null) {
			return value;
		} else {
			final V supplied = supplier.get();
			put(key, supplied);
			return supplied;
		}
	}

	/** */
	@SuppressWarnings("unchecked")
	public V get(K key) {
		try {
			return (V)this.memCache.get(key);
		} catch (Exception e) {
			log.warn("Exception from memCache: " + e);
			return null;
		}
	}

	/**
	 * Has the same behavior as the raw memcache version - if there is not a valid item in the cache,
	 * it will return null rather than a valid Identifiable.
	 */
	public Identifiable<V> getIdentifiable(K key) {
		try {
			IdentifiableValue iv = this.memCache.getIdentifiable(key);
			return (iv == null) ? null : new Identifiable<V>(iv);
		} catch (Exception e) {
			log.warn("Exception from memCache: " + e);
			return null;
		}
	}

	/**
	 * Returns an Identifiable, even if there was nothing in the cache.  It does
	 * this by immediately putting and refetching a null value.  Note that the return value
	 * can still be null due to a poorly timed cache flush (or memcache disabled).  It only
	 * tries once.
	 *
	 * @return null if the refetch failed too
	 */
	public Identifiable<V> getIdentifiableSafe(K key) {
		Identifiable<V> idable = this.getIdentifiable(key);
		if (idable == null) {
			this.memCache.put(key, null);
			idable = this.getIdentifiable(key);
		}

		return idable;
	}

	/** */
	public void put(K key, V value) {
		if (this.expireSeconds != null)
			this.memCache.put(key, value, Expiration.byDeltaSeconds(this.expireSeconds));
		else
			this.memCache.put(key, value);
	}

	/** */
	@SuppressWarnings("unchecked")
	public void putAll(Map<K, V> values) {
		if (this.expireSeconds != null)
			this.memCache.putAll(values, Expiration.byDeltaSeconds(this.expireSeconds));
		else
			this.memCache.putAll(values);
	}

	/**
	 * Works just like regular putIfUntouched
	 *
	 * @return true if newValue was stored, false if there was a collision
	 */
	public boolean putIfUntouched(K key, Identifiable<V> oldValue, V newValue) {
		if (this.expireSeconds != null)
			return this.memCache.putIfUntouched(key, oldValue.iv, newValue, Expiration.byDeltaSeconds(this.expireSeconds));
		else
			return this.memCache.putIfUntouched(key, oldValue.iv, newValue);
	}

	/** */
	@SuppressWarnings("unchecked")
	public Map<K, V> getAll(Collection<K> keys) {
		try {
			Map<K, V> all = (Map<K, V>)this.memCache.getAll((Collection<Object>)keys);
			return all == null ? Collections.EMPTY_MAP : all;
		} catch (Exception e) {
			log.warn("Exception from memCache: " + e);
			return Collections.EMPTY_MAP;
		}
	}

	/** */
	public boolean remove(K key) {
		return this.memCache.delete(key);
	}

	/** */
	@SuppressWarnings("unchecked")
	public Set<K> removeAll(Collection<K> keys) {
		return (Set<K>)this.memCache.deleteAll((Collection<Object>)keys);
	}

	/** */
	public Long increment(K key, long delta) {
		return this.memCache.increment(key, delta);
	}
}