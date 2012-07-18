package com.cole2sworld.colebans.lib;

import java.util.ArrayList;

public class Future<T> {
	private boolean complete = false;
	private T result;
	private ArrayList<FutureCallback<T>> callbacks = new ArrayList<FutureCallback<T>>();
	/**
	 * Creates a new, true Future with no result and a complete value of false.
	 */
	public Future() {}
	/**
	 * Creates a new Future, as a wrapper.
	 * @param data Data to wrap
	 */
	public Future(T data) {
		complete = true;
		result = data;
	}
	public boolean isComplete() {
		return complete;
	}
	public T getResult() {
		if (!complete) throw new IllegalStateException("Not completed");
		return result;
	}
	protected void setResult(T data) {
		complete = true;
		result = data;
		callCallbacks();
	}
	private void callCallbacks() {
		for (FutureCallback<T> callback : callbacks) {
			callback.futureDone(this);
		}
		callbacks.clear();
	}
	public void addCallback(FutureCallback<T> callback) {
		callbacks.add(callback);
	}
}
