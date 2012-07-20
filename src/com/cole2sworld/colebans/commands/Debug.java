package com.cole2sworld.colebans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cole2sworld.colebans.ColeBansPlugin;

/**
 * Allows switching on and off of debug mode.
 * 
 * @author cole2
 * 
 */
public final class Debug implements CBCommand {
	public Debug() {
	}
	
	@Override
	public String run(final String[] args, final CommandSender admin) {
		if (admin instanceof Player)
			return ChatColor.RED + "Only the console can toggle debug mode.";
		ColeBansPlugin.debug = !ColeBansPlugin.debug;
		return ChatColor.AQUA + "Debug mode set to " + ColeBansPlugin.debug;
	}
	
}
