package com.cole2sworld.colebans.framework;

import java.util.Map;

public interface MapCallback<K, V> {
	public static enum MapAction {
		PUT, REMOVE, CLEAR;
	}

	public void onMapModify(Map<K, V> map, K key, V value, MapAction action);
}
