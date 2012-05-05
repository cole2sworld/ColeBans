package com.cole2sworld.ColeBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Help implements CBCommand {

	@Override
	public String run(String[] args, CommandSender admin) {
		return "Go to "+ChatColor.AQUA+"http://c2wr.com/cbwk"+ChatColor.WHITE+" for help.";
	}

}
