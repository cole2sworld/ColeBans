package com.cole2sworld.colebans.handlers;

import java.util.HashMap;
import java.util.Map;

import com.cole2sworld.colebans.handlers.BanHandler.Type;

/**
 * Holds data about a ban.
 * 
 */
public final class BanData {
	private final Map<String, String>	standard	= new HashMap<String, String>(4);
	private final Map<String, Object>	custom		= new HashMap<String, Object>(5);
	
	/**
	 * Do nothing, allowing you to use .set* methods
	 */
	public BanData() {
	}
	
	/**
	 * Build a new "NOT_BANNED" object.
	 * 
	 * @param victim
	 *            The player's name
	 */
	public BanData(final String victim) {
		setVictim(victim);
		setReason("");
		setTime(-1L);
		setType(Type.NOT_BANNED);
	}
	
	/**
	 * Build a new temporary ban.
	 * 
	 * @param victim
	 *            The banned player's name
	 * @param time
	 *            The unix time at which this ban expires
	 */
	public BanData(final String victim, final long time, final String reason) {
		setVictim(victim);
		setTime(time);
		setType(Type.TEMPORARY);
		setReason(reason);
	}
	
	/**
	 * Build a new permanent ban.
	 * 
	 * @param victim
	 *            The banned player's name
	 * @param reason
	 *            The reason for the ban
	 */
	public BanData(final String victim, final String reason) {
		setVictim(victim);
		setReason(reason);
		setType(Type.PERMANENT);
		setTime(-1L);
	}
	
	/**
	 * Gets the value of a custom data, or null if it doesn't exist.
	 * 
	 * @return The custom data's value
	 */
	public Object getCustomData(final String key) {
		return custom.get(key);
	}
	
	/**
	 * @return the reason
	 */
	public String getReason() {
		return standard.get("reason");
	}
	
	/**
	 * @return the Unix Timestamp at which the ban will expire
	 */
	public Long getTime() {
		return Long.parseLong(standard.get("time"));
	}
	
	/**
	 * @return the type
	 */
	public Type getType() {
		return Type.valueOf(standard.get("type"));
	}
	
	/**
	 * @return the victim
	 */
	public String getVictim() {
		return standard.get("victim");
	}
	
	/**
	 * Sets the value of a custom data, overwriting any previous values. If data
	 * is <code>null</code>, the custom data will be deleted.
	 * 
	 * @param key
	 *            The key to use later to retrieve this value
	 * @param data
	 *            The value
	 */
	public void setCustomData(final String key, final Object data) {
		if (data == null) {
			custom.remove(key);
		} else {
			custom.put(key, data);
		}
	}
	
	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(final String reason) {
		standard.put("reason", reason);
	}
	
	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(final Long time) {
		standard.put("time", time.toString());
	}
	
	/**
	 * @param victim
	 *            the victim to set
	 */
	public void setVictim(final String victim) {
		standard.put("victim", victim);
	}
	
	/**
	 * @param type
	 *            the type to set
	 */
	protected void setType(final Type type) {
		standard.put("type", type.name());
	}
}
