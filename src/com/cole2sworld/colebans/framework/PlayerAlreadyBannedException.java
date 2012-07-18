package com.cole2sworld.colebans.framework;
/**
 * Thrown when an attempt is made to ban a player, but they are already banned.
 *
 */
public final class PlayerAlreadyBannedException extends Exception {
	private static final long serialVersionUID = 8392525539750353813L;
	private String detail = null;
	public PlayerAlreadyBannedException(String detail) {
		this.detail = detail;
	}
	@Override
	public String getMessage() {
		return detail;
	}
}
