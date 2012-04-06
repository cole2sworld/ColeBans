package com.cole2sworld.ColeBans.framework;
/**
 * Thrown when an attempt is made to unban a player, but they are not banned.
 *
 */
public final class PlayerNotBannedException extends Exception {
	private static final long serialVersionUID = 8392525539750353813L;
	private String detail = null;
	public PlayerNotBannedException(String detail) {
		this.detail = detail;
	}
	@Override
	public String getMessage() {
		return detail;
	}
}
