package com.cole2sworld.ColeBans;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.cole2sworld.ColeBans.framework.RestrictionManager;

/**
 * Manages restrictions (copy-pasted for the most part from DragonList)
 * <br/>
 * Yay for code re-use!
 * @author cole2
 *
 */
public class RestrictionListener implements Listener {
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (RestrictionManager.isFrozen(event.getPlayer())) {
			event.getPlayer().teleport(RestrictionManager.getFreezeLoc(event.getPlayer()));
			event.getPlayer().setFallDistance(0);
			event.getPlayer().setFireTicks(0);
		}
	}
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (RestrictionManager.isFrozen(event.getPlayer())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED+"No, bad "+event.getPlayer().getName()+"! You can't use that while frozen!");
		}
	}
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (RestrictionManager.isFrozen(event.getPlayer())) {
			event.getPlayer().sendMessage(ChatColor.RED+"No, bad "+event.getPlayer().getName()+"! You can't do that while frozen!");
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (RestrictionManager.isFrozen(event.getPlayer())) {
			event.getPlayer().sendMessage(ChatColor.RED+"No, bad "+event.getPlayer().getName()+"! You can't do that while frozen!");
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (RestrictionManager.isFrozen(event.getPlayer())) {
			event.getPlayer().sendMessage(ChatColor.RED+"No, bad "+event.getPlayer().getName()+"! You can't do that while frozen!");
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onChat(PlayerChatEvent event) {
		if (RestrictionManager.isFrozen(event.getPlayer())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED+"No, bad "+event.getPlayer().getName()+"! You can't talk while frozen!");
		}
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		RestrictionManager.thaw(event.getPlayer());
	}
	@EventHandler
	public void onKick(PlayerKickEvent event) {
		RestrictionManager.thaw(event.getPlayer());
	}
}
