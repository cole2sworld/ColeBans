package com.cole2sworld.ColeBans.lib;

public interface FutureCallback<T> {
	public abstract void futureDone(Future<T> future);
}
