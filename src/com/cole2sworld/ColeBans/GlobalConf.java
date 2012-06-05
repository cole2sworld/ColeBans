package com.cole2sworld.ColeBans;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.Configuration;

/**
 * ColeBans global configuration.
 * @since v1 Apricot (rewritten in v5 Elderberry)
 * @author cole2
 *
 */
public class GlobalConf implements MapCallback<String, CastableObject> {
	private static Configuration def;
	private static Configuration backConf;
	public static final CallbackHashMap<String, CastableObject> conf = new CallbackHashMap<String, CastableObject>();
	private static boolean loading = false;
	@Override
	public void onMapModify(Map<String, CastableObject> map, String key, CastableObject value, MapAction action) {
		if (loading) return;
		for (String iKey : backConf.getKeys(true)) {
			backConf.set(iKey, map.get(iKey));
		}
	}
	public static void load() {
		loading = true;
		backConf = Main.instance.getConfig();
		def = backConf.getDefaults();
		for (Entry<String, Object> entry : def.getValues(true).entrySet()) {
			conf.put(entry.getKey(), new CastableObject(entry.getValue()));
		}
		for (Entry<String, Object> entry : backConf.getValues(true).entrySet()) {
			conf.put(entry.getKey(), new CastableObject(entry.getValue()));
		}
		save();
		loading = false;
	}
	public static void save() {
		// make sure our changes are committed
		for (Entry<String, CastableObject> entry : conf.entrySet()) {
			backConf.set(entry.getKey(), entry.getValue().asObject());
		}
		Main.instance.saveConfig();
	}
	public static CastableObject get(String key) {
		if (!key.startsWith("settings.")) {
			key = "settings."+key;
		}
		return conf.get(key);
	}
	public static void set(String key, Object value) {
		if (!key.startsWith("settings.")) {
			key = "settings."+key;
		}
		conf.put(key, new CastableObject(value));
	}
	static {
		conf.addCallback(new GlobalConf());
	}
}
