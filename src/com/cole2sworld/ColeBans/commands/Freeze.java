package com.cole2sworld.ColeBans.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.ColeBans.framework.PermissionSet;
import com.cole2sworld.ColeBans.framework.RestrictionManager;

public class Freeze implements CBCommand {
	@Override
	public String run(String[] args, CommandSender admin) throws Exception {
		if (!new PermissionSet(admin).canFreeze) return ChatColor.RED+"You don't have permission to do that.";
		if (args.length < 1) return ChatColor.RED+"Not enough arguments.";
		if (Bukkit.getServer().getPlayer(args[0]) == null) return ChatColor.RED+"That player is not online!";
		RestrictionManager.freeze(Bukkit.getServer().getPlayer(args[0]));
		return null;
	}

}
