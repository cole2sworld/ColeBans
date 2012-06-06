package com.cole2sworld.ColeBans.framework;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CallbackHashMap<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = 4236640905274897413L;
	private HashSet<MapCallback<K, V>> callbacks = new HashSet<MapCallback<K, V>>();
	public V put(K key, V value) {
		V result = super.put(key, value);
		for (MapCallback<K, V> callback : callbacks) {
			callback.onMapModify(this, key, value, MapCallback.MapAction.PUT);
		}
		return result;
	};
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		super.putAll(m);
		for (MapCallback<K, V> callback : callbacks) {
			for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
				callback.onMapModify(this, entry.getKey(), entry.getValue(), MapCallback.MapAction.PUT);
			}
		}
	}
	@Override
	@SuppressWarnings ({"unused", "unchecked"})
	public V remove(Object key) {
		try {
			K derp = (K)key;
		} catch (Exception e) {
			throw new IllegalArgumentException("Key is not the correct type");
		}
		V result = super.remove(key);
		for (MapCallback<K, V> callback : callbacks) {
			callback.onMapModify(this, (K)key, result, MapCallback.MapAction.REMOVE);
		}
		return result;
	}
	@Override
	public void clear() {
		super.clear();
		for (MapCallback<K, V> callback : callbacks) {
			callback.onMapModify(this, null, null, MapCallback.MapAction.CLEAR);
		}
	}
	public void addCallback(MapCallback<K, V> callback) {
		callbacks.add(callback);
	}
	public void removeCallback(MapCallback<K, V> callback) {
		callbacks.remove(callback);
	}
}
