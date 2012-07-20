package com.cole2sworld.colebans.framework;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.Configuration;

import com.cole2sworld.colebans.ColeBansPlugin;

/**
 * ColeBans global configuration.
 * 
 * @since v1 Apricot (rewritten in v5 Elderberry)
 * @author cole2
 * 
 */
public final class GlobalConf implements MapCallback<String, CastableObject> {
	private static Configuration									def;
	private static Configuration									backConf;
	private static final CallbackHashMap<String, CastableObject>	CONFIG	= new CallbackHashMap<String, CastableObject>();
	private static boolean											loading	= false;
	static {
		CONFIG.addCallback(new GlobalConf());
	}
	
	public static CastableObject get(final String key) {
		return CONFIG.get(key.startsWith("settings.") ? key : "settings." + key);
	}
	
	public static void load() {
		loading = true;
		backConf = ColeBansPlugin.instance.getConfig();
		def = backConf.getDefaults();
		for (final Entry<String, Object> entry : def.getValues(true).entrySet()) {
			CONFIG.put(entry.getKey(), new CastableObject(entry.getValue()));
		}
		for (final Entry<String, Object> entry : backConf.getValues(true).entrySet()) {
			CONFIG.put(entry.getKey(), new CastableObject(entry.getValue()));
		}
		save();
		loading = false;
		boolean didSomething = false;
		if (!get("tempBanMessage").asString().contains("%reason")) {
			set("tempBanMessage", "You are tempbanned for %reason! %time minute%plural remaining!");
			didSomething = true;
		}
		if (get("advanced.package").asString().equals("com.cole2sworld.ColeBans.handlers")) {
			set("advanced.package", "com.cole2sworld.colebans.handlers");
			didSomething = true;
		}
		if (didSomething) {
			save();
		}
	}
	
	public static void save() {
		// make sure our changes are committed
		for (final Entry<String, CastableObject> entry : CONFIG.entrySet()) {
			backConf.set(entry.getKey(), entry.getValue().asObject());
		}
		ColeBansPlugin.instance.saveConfig();
	}
	
	public static void set(final String key, final Object value) {
		CONFIG.put(key.startsWith("settings.") ? key : "settings." + key, new CastableObject(value));
	}
	
	private GlobalConf() {
	}
	
	@Override
	public void onMapModify(final Map<String, CastableObject> map, final String key,
			final CastableObject value, final MapAction action) {
		if (loading) return;
		for (final String iKey : backConf.getKeys(true)) {
			backConf.set(iKey, map.get(iKey));
		}
	}
}
