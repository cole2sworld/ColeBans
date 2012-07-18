package com.cole2sworld.colebans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cole2sworld.colebans.Main;
/**
 * Allows switching on and off of debug mode.
 * @author cole2
 *
 */
public final class Debug implements CBCommand {

	@Override
	public String run(String[] args, CommandSender admin) {
		if (admin instanceof Player) return ChatColor.RED+"Only the console can toggle debug mode.";
		Main.debug = !Main.debug;
		return ChatColor.AQUA+"Debug mode set to "+Main.debug;
	}

}
