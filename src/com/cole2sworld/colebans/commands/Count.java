package com.cole2sworld.colebans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.colebans.ColeBansPlugin;
import com.cole2sworld.colebans.Util;
import com.cole2sworld.colebans.framework.PermissionSet;

public final class Count implements CBCommand {
	public Count() {
	}
	
	@Override
	public String run(final String[] args, final CommandSender admin) {
		if (!(new PermissionSet(admin).canCount))
			return ChatColor.RED + "You do not have permission to do that.";
		final long count = ColeBansPlugin.instance.banHandler.countBans(admin.getName());
		return "This server has made " + count + " ban" + Util.getPlural(count, true) + ".";
	}
	
}
