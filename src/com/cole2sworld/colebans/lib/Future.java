package com.cole2sworld.colebans.lib;

import java.util.ArrayList;
import java.util.List;

public final class Future<T> {
	private boolean							complete	= false;
	private T								result;
	private final List<FutureCallback<T>>	callbacks	= new ArrayList<FutureCallback<T>>();
	
	/**
	 * Creates a new, true Future with no result and a complete value of false.
	 */
	public Future() {
	}
	
	/**
	 * Creates a new Future, as a wrapper.
	 * 
	 * @param data
	 *            Data to wrap
	 */
	public Future(final T data) {
		complete = true;
		result = data;
	}
	
	public void addCallback(final FutureCallback<T> callback) {
		callbacks.add(callback);
	}
	
	public T getResult() {
		if (!complete) throw new IllegalStateException("Not completed");
		return result;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	protected void setResult(final T data) {
		complete = true;
		result = data;
		callCallbacks();
	}
	
	private void callCallbacks() {
		for (final FutureCallback<T> callback : callbacks) {
			callback.futureDone(this);
		}
		callbacks.clear();
	}
}
