package com.cole2sworld.colebans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.colebans.Main;
import com.cole2sworld.colebans.framework.GlobalConf;
import com.cole2sworld.colebans.framework.PermissionSet;
import com.cole2sworld.colebans.handlers.FileBanHandler;

public final class Reload implements CBCommand {
	
	@Override
	public String run(final String[] args, final CommandSender admin) throws Exception {
		if (!(new PermissionSet(admin).canReload))
			return ChatColor.RED + "You don't have permission to do that.";
		Main.instance.reloadConfig();
		GlobalConf.load();
		if (Main.instance.banHandler instanceof FileBanHandler) {
			((FileBanHandler) Main.instance.banHandler).reload();
		}
		admin.sendMessage("Configuration reloaded.");
		return null;
	}
	
}
