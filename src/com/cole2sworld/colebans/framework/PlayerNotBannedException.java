package com.cole2sworld.colebans.framework;

/**
 * Thrown when an attempt is made to unban a player, but they are not banned.
 * 
 */
public final class PlayerNotBannedException extends Exception {
	private static final long	serialVersionUID	= 8392525539750353813L;
	private final String		detail;
	
	public PlayerNotBannedException(final String d) {
		detail = d;
	}
	
	@Override
	public String getMessage() {
		return detail;
	}
}
