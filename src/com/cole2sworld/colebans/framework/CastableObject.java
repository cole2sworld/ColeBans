package com.cole2sworld.colebans.framework;

import org.bukkit.Location;

import com.cole2sworld.colebans.handlers.BanData;
/**
 * Simple wrapper class that allows simple, safe casting of the wrapped object.
 * @author cole2
 *
 */
public class CastableObject {
	private final Object obj;
	public CastableObject(Object object) {
		obj = object;
	}
	public Class<?> getWrappedClass() {
		return obj.getClass();
	}
	public String getType() {
		return obj.getClass().getSimpleName();
	}
	public Object asNull() {
		return null;
	}
	public Object asObject() {
		return obj;
	}
	public String asString() {
		if (obj instanceof String) return (String)obj;
		return null;
	}
	public Integer asInteger() {
		if (obj instanceof Integer) return (Integer)obj;
		return null;
	}
	public Long asLong() {
		if (obj instanceof Long) return (Long)obj;
		return null;
	}
	public Short asShort() {
		if (obj instanceof Short) return (Short)obj;
		return null;
	}
	public Character asCharacter() {
		if (obj instanceof Character) return (Character)obj;
		return null;
	}
	public Double asDouble() {
		if (obj instanceof Double) return (Double)obj;
		return null;
	}
	public Float asFloat() {
		if (obj instanceof Float) return (Float)obj;
		return null;
	}
	public BanData asBanData() {
		if (obj instanceof BanData) return (BanData)obj;
		return null;
	}
	public LogEntry asLogEntry() {
		if (obj instanceof LogEntry) return (LogEntry)obj;
		return null;
	}
	public Location asLocation() {
		if (obj instanceof Location) return (Location)obj;
		return null;
	}
	public Boolean asBoolean() {
		if (obj instanceof Boolean) return (Boolean)obj;
		return null;
	}
	public Byte asByte() {
		if (obj instanceof Byte) return (Byte)obj;
		return null;
	}
	@SuppressWarnings("unchecked")
	public <T> T as(T type) {
		if (obj.getClass().isAssignableFrom(type.getClass())) {
			return (T)obj;
		}
		return null;
	}
	
}
