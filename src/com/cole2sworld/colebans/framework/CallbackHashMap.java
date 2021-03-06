package com.cole2sworld.colebans.framework;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CallbackHashMap<K, V> extends HashMap<K, V> {
	private static final long				serialVersionUID	= 4236640905274897413L;
	private final Set<MapCallback<K, V>>	callbacks			= new HashSet<MapCallback<K, V>>();
	
	public final void addCallback(final MapCallback<K, V> callback) {
		callbacks.add(callback);
	}
	
	@Override
	public final void clear() {
		super.clear();
		for (final MapCallback<K, V> callback : callbacks) {
			callback.onMapModify(this, null, null, MapCallback.MapAction.CLEAR);
		}
	}
	
	@Override
	public final V put(final K key, final V value) {
		final V result = super.put(key, value);
		for (final MapCallback<K, V> callback : callbacks) {
			callback.onMapModify(this, key, value, MapCallback.MapAction.PUT);
		}
		return result;
	}
	
	@Override
	public final void putAll(final Map<? extends K, ? extends V> m) {
		super.putAll(m);
		for (final MapCallback<K, V> callback : callbacks) {
			for (final Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
				callback.onMapModify(this, entry.getKey(), entry.getValue(),
						MapCallback.MapAction.PUT);
			}
		}
	}
	
	@Override
	@SuppressWarnings({
			"unused",
			"unchecked"
	})
	public final V remove(final Object key) {
		try { // poor man's instanceof check
			final K derp = (K) key;
		} catch (final Exception e) {
			throw new IllegalArgumentException("Key is not the correct type");
		}
		final V result = super.remove(key);
		for (final MapCallback<K, V> callback : callbacks) {
			callback.onMapModify(this, (K) key, result, MapCallback.MapAction.REMOVE);
		}
		return result;
	}
	
	public final void removeCallback(final MapCallback<K, V> callback) {
		callbacks.remove(callback);
	}
}
