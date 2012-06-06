package com.cole2sworld.ColeBans;

import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
/**
 * Manages restrictions.
 *
 */
public class RestrictionManager {
	static HashMap<Player, Location> frozen = new HashMap<Player, Location>();
	public static void freeze(Player player) {
		if (isFrozen(player)) return;
		frozen.put(player, player.getLocation());
	}
	public static void thaw(Player player) {
		if (!isFrozen(player)) return;
		player.teleport(frozen.get(player));
		frozen.remove(player);
	}
	public static boolean isFrozen(Player player) {
		return frozen.containsKey(player);
	}
	public static Location getFreezeLoc(Player player) {
		return frozen.get(player).clone();
	}
}
