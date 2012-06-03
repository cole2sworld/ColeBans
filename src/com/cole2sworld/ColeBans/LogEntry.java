package com.cole2sworld.ColeBans;

import java.util.HashMap;

public class LogEntry {
	private final HashMap<String, String> map = new HashMap<String, String>();
	public LogEntry() {
		map.put("type", "UNKNOWN");
		map.put("admin", "?");
		map.put("victim", "?");
		map.put("time", "0");
	}
	public LogEntry(LogManager.Type type, String admin, String victim, long time) {
		setType(type);
		setAdmin(admin);
		setVictim(victim);
		setTime(time);
	}
	public void setType(LogManager.Type type) {
		map.put("type", type.name());
	}
	public LogManager.Type getType() {
		return LogManager.Type.valueOf(map.get("type"));
	}
	public void setAdmin(String admin) {
		map.put("admin", admin);
	}
	public String getAdmin() {
		return map.get("admin");
	}
	public void setVictim(String victim) {
		map.put("victim", victim);
	}
	public String getVictim() {
		return map.get("victim");
	}
	public void setTime(long time) {
		map.put("time", Long.toString(time));
	}
	public long getTime() {
		return Long.valueOf(map.get("time"));
	}
}
