package com.cole2sworld.colebans.framework;

import java.util.HashMap;
import java.util.Map;

import com.cole2sworld.colebans.ActionLogManager;

/**
 * Log entry, for Actions.
 * 
 * @author cole2
 * @since v5 Elderberry
 */
public final class LogEntry {
	private final Map<String, String>	map	= new HashMap<String, String>();
	
	public LogEntry() {
		map.put("type", "UNKNOWN");
		map.put("admin", "?");
		map.put("victim", "?");
		map.put("time", "0");
	}
	
	public LogEntry(final ActionLogManager.Type type, final String admin, final String victim,
			final long time) {
		setType(type);
		setAdmin(admin);
		setVictim(victim);
		setTime(time);
	}
	
	public String getAdmin() {
		return map.get("admin");
	}
	
	public long getTime() {
		return Long.valueOf(map.get("time"));
	}
	
	public ActionLogManager.Type getType() {
		return ActionLogManager.Type.valueOf(map.get("type"));
	}
	
	public String getVictim() {
		return map.get("victim");
	}
	
	public void setAdmin(final String admin) {
		map.put("admin", admin);
	}
	
	public void setTime(final long time) {
		map.put("time", Long.toString(time));
	}
	
	public void setType(final ActionLogManager.Type type) {
		map.put("type", type.name());
	}
	
	public void setVictim(final String victim) {
		map.put("victim", victim);
	}
}
