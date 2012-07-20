package com.cole2sworld.colebans.framework;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Manages restrictions.
 * 
 */
public final class RestrictionManager {
	private static Map<Player, Location>	frozen	= new HashMap<Player, Location>();
	
	public static void freeze(final Player player) {
		if (isFrozen(player)) return;
		frozen.put(player, player.getLocation());
	}
	
	public static Location getFreezeLoc(final Player player) {
		return frozen.get(player).clone();
	}
	
	public static boolean isFrozen(final Player player) {
		return frozen.containsKey(player);
	}
	
	public static void thaw(final Player player) {
		if (!isFrozen(player)) return;
		player.teleport(frozen.get(player));
		frozen.remove(player);
	}
	
	private RestrictionManager() {
	}
}
