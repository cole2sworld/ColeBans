package com.cole2sworld.colebans.framework;

import org.bukkit.Location;

import com.cole2sworld.colebans.handlers.BanData;

/**
 * Simple wrapper class that allows simple, safe casting of the wrapped object.
 * 
 * @author cole2
 * 
 */
public class CastableObject {
	private final Object	obj;
	
	public CastableObject(final Object object) {
		obj = object;
	}
	
	/**
	 * Get this object, as the type of the passed class, or null if it doesn't
	 * match.
	 * 
	 * @param type
	 *            Class to cast to.
	 * @return Wrapped object, as the type of the passed class, or null if the
	 *         type doesn't match.
	 */
	@SuppressWarnings("unchecked")
	public <T> T as(final Class<T> type) {
		if (obj.getClass().isAssignableFrom(type.getClass())) return (T) obj;
		return null;
	}
	
	/**
	 * 
	 * @return The wrapped object as BanData, or null if it is not a BanData (or
	 *         is null)
	 */
	public BanData asBanData() {
		if (obj instanceof BanData) return (BanData) obj;
		return null;
	}
	
	/**
	 * 
	 * @return The wrapped object as a Boolean, or null if it is not a Boolean
	 *         (or is null)
	 */
	public Boolean asBoolean() {
		if (obj instanceof Boolean) return (Boolean) obj;
		return null;
	}
	
	/**
	 * 
	 * @return The wrapped object as a Byte, or null if it is not a Byte (or is
	 *         null)
	 */
	public Byte asByte() {
		if (obj instanceof Byte) return (Byte) obj;
		return null;
	}
	
	/**
	 * 
	 * @return The wrapped object as a Character, or null if it is not a
	 *         Character (or is null)
	 */
	public Character asCharacter() {
		if (obj instanceof Character) return (Character) obj;
		return null;
	}
	
	/**
	 * 
	 * @return The wrapped object as a Double, or null if it is not a Double (or
	 *         is null)
	 */
	public Double asDouble() {
		if (obj instanceof Double) return (Double) obj;
		return null;
	}
	
	/**
	 * 
	 * @return The wrapped object as a Float, or null if it is not a Float (or
	 *         is null)
	 */
	public Float asFloat() {
		if (obj instanceof Float) return (Float) obj;
		return null;
	}
	
	/**
	 * 
	 * @return The wrapped object as a Integer, or null if it is not a Integer
	 *         (or is null)
	 */
	public Integer asInteger() {
		if (obj instanceof Integer) return (Integer) obj;
		return null;
	}
	
	/**
	 * 
	 * @return The wrapped object as a Location, or null if it is not a Location
	 *         (or is null)
	 */
	public Location asLocation() {
		if (obj instanceof Location) return (Location) obj;
		return null;
	}
	
	/**
	 * 
	 * @return The wrapped object as a LogEntry, or null if it is not a LogEntry
	 *         (or is null)
	 */
	public LogEntry asLogEntry() {
		if (obj instanceof LogEntry) return (LogEntry) obj;
		return null;
	}
	
	/**
	 * 
	 * @return The wrapped object as a Long, or null if it is not a Long (or is
	 *         null)
	 */
	public Long asLong() {
		if (obj instanceof Long) return (Long) obj;
		return null;
	}
	
	/**
	 * 
	 * @return null
	 */
	public Object asNull() {
		return null;
	}
	
	/**
	 * 
	 * @return The wrapped object
	 */
	public Object asObject() {
		return obj;
	}
	
	/**
	 * 
	 * @return The wrapped object as a Short, or null if it is not a Short (or
	 *         is null)
	 */
	public Short asShort() {
		if (obj instanceof Short) return (Short) obj;
		return null;
	}
	
	/**
	 * 
	 * @return The wrapped object as a String, or null if it is not a String (or
	 *         is null)
	 */
	public String asString() {
		if (obj instanceof String) return (String) obj;
		return null;
	}
	
	
	@Override
	public boolean equals(final Object pObj) {
		return obj.equals(pObj);
	}
	
	/**
	 * 
	 * @return The wrapped object
	 */
	public Object get() {
		return obj;
	}
	
	/**
	 * 
	 * @return the classname of the wrapped object
	 */
	public String getType() {
		return obj.getClass().getSimpleName();
	}
	
	/**
	 * 
	 * @return the class of the wrapped object
	 */
	public Class<?> getWrappedClass() {
		return obj.getClass();
	}
	
	@Override
	public int hashCode() {
		return obj.hashCode();
	}
	
	/**
	 * 
	 * @param clazz
	 *            The class whose type is to be checked
	 * @return If the wrapped object is an instance of the classes type
	 */
	public boolean is(final Class<?> clazz) {
		return obj.getClass().isAssignableFrom(clazz);
	}
	
	/**
	 * 
	 * @return if the wrapped object is a BanData
	 */
	public boolean isBanData() {
		return obj instanceof BanData;
	}
	
	/**
	 * 
	 * @return if the wrapped object is a Boolean
	 */
	public boolean isBoolean() {
		return obj instanceof Boolean;
	}
	
	/**
	 * 
	 * @return if the wrapped object is a Byte
	 */
	public boolean isByte() {
		return obj instanceof Byte;
	}
	
	/**
	 * 
	 * @return if the wrapped object is a Character
	 */
	public boolean isCharacter() {
		return obj instanceof Character;
	}
	
	/**
	 * 
	 * @return if the wrapped object is a Double
	 */
	public boolean isDouble() {
		return obj instanceof Double;
	}
	
	/**
	 * 
	 * @return if the wrapped object is a Float
	 */
	public boolean isFloat() {
		return obj instanceof Float;
	}
	
	/**
	 * 
	 * @return if the wrapped object is a Integer
	 */
	public boolean isInteger() {
		return obj instanceof Integer;
	}
	
	/**
	 * 
	 * @return if the wrapped object is a Location
	 */
	public boolean isLocation() {
		return obj instanceof Location;
	}
	
	/**
	 * 
	 * @return if the wrapped object is a LogEntry
	 */
	public boolean isLogEntry() {
		return obj instanceof LogEntry;
	}
	
	/**
	 * 
	 * @return if the wrapped object is a Long
	 */
	public boolean isLong() {
		return obj instanceof Long;
	}
	
	/**
	 * 
	 * @return if the wrapped object is null
	 */
	public boolean isNull() {
		return obj == null;
	}
	
	/**
	 * 
	 * @return true
	 */
	public boolean isObject() {
		return true;
	}
	
	/**
	 * 
	 * @return if the wrapped object is a Short
	 */
	public boolean isShort() {
		return obj instanceof Short;
	}
	
	/**
	 * 
	 * @return if the wrapped object is a String
	 */
	public boolean isString() {
		return obj instanceof String;
	}
	
}
