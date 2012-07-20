package com.cole2sworld.colebans;

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

import com.cole2sworld.colebans.framework.RestrictionManager;

/**
 * Manages restrictions (copy-pasted for the most part from DragonList) <br/>
 * Yay for code re-use!
 * 
 * @author cole2
 * 
 */
@SuppressWarnings("static-method")
public final class RestrictionListener implements Listener {
	protected RestrictionListener() {
	}
	
	
	@EventHandler
	public void onBreak(final BlockBreakEvent event) {
		if (RestrictionManager.isFrozen(event.getPlayer())) {
			event.getPlayer().sendMessage(
					ChatColor.RED + "No, bad " + event.getPlayer().getName()
							+ "! You can't do that while frozen!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onChat(final PlayerChatEvent event) {
		if (RestrictionManager.isFrozen(event.getPlayer())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(
					ChatColor.RED + "No, bad " + event.getPlayer().getName()
							+ "! You can't talk while frozen!");
		}
	}
	
	@EventHandler
	public void onCommand(final PlayerCommandPreprocessEvent event) {
		if (RestrictionManager.isFrozen(event.getPlayer())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(
					ChatColor.RED + "No, bad " + event.getPlayer().getName()
							+ "! You can't use that while frozen!");
		}
	}
	
	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		if (RestrictionManager.isFrozen(event.getPlayer())) {
			event.getPlayer().sendMessage(
					ChatColor.RED + "No, bad " + event.getPlayer().getName()
							+ "! You can't do that while frozen!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onKick(final PlayerKickEvent event) {
		RestrictionManager.thaw(event.getPlayer());
	}
	
	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		if (RestrictionManager.isFrozen(event.getPlayer())) {
			event.getPlayer().teleport(RestrictionManager.getFreezeLoc(event.getPlayer()));
			event.getPlayer().setFallDistance(0);
			event.getPlayer().setFireTicks(0);
		}
	}
	
	@EventHandler
	public void onPlace(final BlockPlaceEvent event) {
		if (RestrictionManager.isFrozen(event.getPlayer())) {
			event.getPlayer().sendMessage(
					ChatColor.RED + "No, bad " + event.getPlayer().getName()
							+ "! You can't do that while frozen!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		RestrictionManager.thaw(event.getPlayer());
	}
}
