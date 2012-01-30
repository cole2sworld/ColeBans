package com.cole2sworld.ColeBans;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;

public class EventRegistar {
	public static void register(PluginManager pm, long startTime) {
		pm.registerEvent(Type.PLAYER_PRELOGIN, new CBPlayerListener(), Priority.Highest, Main.instance);
		pm.registerEvent(Type.PLUGIN_ENABLE, new CBServerListener(startTime), Priority.Monitor, Main.instance);
	}
}
