package com.cole2sworld.ColeBans.handlers;
import java.util.HashMap;

import com.cole2sworld.ColeBans.handlers.BanHandler.Type;
/**
 * Holds data about a ban.
 *
 */
public final class BanData {
	private HashMap<String, String> standard = new HashMap<String, String>(4);
	private HashMap<String, Object> custom = new HashMap<String, Object>(5);
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
	 * Do nothing, allowing you to use .set* methods
	 */
	public BanData() {
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return standard.get("reason");
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		standard.put("reason", reason);
	}
	/**
	 * @return the victim
	 */
	public String getVictim() {
		return standard.get("victim");
	}
	/**
	 * @param victim the victim to set
	 */
	public void setVictim(String victim) {
		standard.put("victim", victim);
	}
	/**
	 * @return the type
	 */
	public Type getType() {
		return Type.valueOf(standard.get("type"));
	}
	/**
	 * @param type the type to set
	 */
	protected void setType(Type type) {
		standard.put("type", type.name());
	}
	/**
	 * @return the Unix Timestamp at which the ban will expire
	 */
	public Long getTime() {
		return Long.parseLong(standard.get("time"));
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(Long time) {
		standard.put("time", time.toString());
	}
	/**
	 * Sets the value of a custom data, overwriting any previous values. If data is <code>null</code>, the custom data will be deleted.
	 * @param key The key to use later to retrieve this value
	 * @param data The value
	 */
	public void setCustomData(String key, Object data) {
		if (data == null) {
			custom.remove(key);
		} else {
			custom.put(key, data);
		}
	}
	/**
	 * Gets the value of a custom data, or null if it doesn't exist.
	 * @return The custom data's value
	 */
	public Object getCustomData(String key) {
		return custom.get(key);
	}
}
