package com.cole2sworld.colebans.framework;

/**
 * Thrown when an attempt is made to ban a player, but they are already banned.
 * 
 */
public final class PlayerAlreadyBannedException extends Exception {
	private static final long	serialVersionUID	= 8392525539750353813L;
	private final String		detail;
	
	public PlayerAlreadyBannedException(final String d) {
		this.detail = d;
	}
	
	@Override
	public String getMessage() {
		return detail;
	}
}
