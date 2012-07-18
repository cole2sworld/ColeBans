package com.cole2sworld.colebans.framework;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.Configuration;

import com.cole2sworld.colebans.Main;

/**
 * ColeBans global configuration.
 * 
 * @since v1 Apricot (rewritten in v5 Elderberry)
 * @author cole2
 * 
 */
public class GlobalConf implements MapCallback<String, CastableObject> {
	private static Configuration									def;
	private static Configuration									backConf;
	private static final CallbackHashMap<String, CastableObject>	conf	= new CallbackHashMap<String, CastableObject>();
	private static boolean											loading	= false;
	static {
		conf.addCallback(new GlobalConf());
	}
	
	public static CastableObject get(final String key) {
		return conf.get(key.startsWith("settings.") ? key : "settings." + key);
	}
	
	public static void load() {
		loading = true;
		backConf = Main.instance.getConfig();
		def = backConf.getDefaults();
		for (final Entry<String, Object> entry : def.getValues(true).entrySet()) {
			conf.put(entry.getKey(), new CastableObject(entry.getValue()));
		}
		for (final Entry<String, Object> entry : backConf.getValues(true).entrySet()) {
			conf.put(entry.getKey(), new CastableObject(entry.getValue()));
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
		for (final Entry<String, CastableObject> entry : conf.entrySet()) {
			backConf.set(entry.getKey(), entry.getValue().asObject());
		}
		Main.instance.saveConfig();
	}
	
	public static void set(final String key, final Object value) {
		conf.put(key.startsWith("settings.") ? key : "settings." + key, new CastableObject(value));
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
