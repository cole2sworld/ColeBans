package com.cole2sworld.ColeBans.handlers;

public interface FileBanHandler {
	/**
	 * Reload the file this ban handler is utilizing.
	 */
	public abstract void reload();
	
	/**
	 * Save the file this ban handler is utilizing.
	 */
	public abstract void save();
}
