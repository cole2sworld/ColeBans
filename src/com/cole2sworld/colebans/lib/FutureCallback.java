package com.cole2sworld.colebans.lib;

public interface FutureCallback<T> {
	public abstract void futureDone(Future<T> future);
}
