package com.cole2sworld.ColeBans.handlers;
import com.cole2sworld.ColeBans.handlers.BanHandler.Type;
/**
 * Holds data about a ban.
 *
 */
public class BanData {
	private Type type = Type.NOT_BANNED;
	private String reason = "";
	private String victim = "";
	private Long time = -1L;
	/**
	 * Build a new permanent ban.
	 * @param victim The banned player's name
	 * @param reason The reason for the ban
	 */
	public BanData(String victim, String reason) {
		setVictim(victim);
		setReason(reason);
		setType(Type.PERMANENT);
		setTime(-1L);
	}
	/**
	 * Build a new temporary ban.
	 * @param victim The banned player's name
	 * @param time The unix time at which this ban expires
	 */
	public BanData(String victim, long time) {
		setVictim(victim);
		setTime(time);
		setType(Type.TEMPORARY);
		setReason("Temporary Ban");
	}
	/**
	 * Build a new "NOT_BANNED" object.
	 * @param victim The player's name
	 */
	public BanData(String victim) {
		setVictim(victim);
		setReason("");
		setTime(-1L);
		setType(Type.NOT_BANNED);
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the victim
	 */
	public String getVictim() {
		return victim;
	}
	/**
	 * @param victim the victim to set
	 */
	public void setVictim(String victim) {
		this.victim = victim;
	}
	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	protected void setType(Type type) {
		this.type = type;
	}
	/**
	 * @return the Unix Timestamp at which the ban will expire
	 */
	public Long getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(Long time) {
		this.time = time;
	}
}
