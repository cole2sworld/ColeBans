package com.cole2sworld.colebans.framework;

/**
 * Thrown when an attempt to do something with a player is made, but the player
 * is offline.
 * 
 */
public final class PlayerOfflineException extends Exception {
	private static final long	serialVersionUID	= 8392525539750353813L;
	private final String		detail;
	
	public PlayerOfflineException(final String d) {
		detail = d;
	}
	
	@Override
	public String getMessage() {
		return detail;
	}
}
