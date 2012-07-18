package com.cole2sworld.colebans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.colebans.Main;
import com.cole2sworld.colebans.Util;
import com.cole2sworld.colebans.framework.PermissionSet;

public final class Count implements CBCommand {

	@Override
	public String run(String[] args, CommandSender admin) {
		if (!(new PermissionSet(admin).canCount)) return ChatColor.RED+"You do not have permission to do that.";
		long count = Main.instance.banHandler.listBannedPlayers(admin.getName()).size();
		return "This server has made "+count+" ban"+Util.getPlural(count, true)+".";
	}

}
