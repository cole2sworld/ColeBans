package com.cole2sworld.ColeBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.ColeBans.Main;
import com.cole2sworld.ColeBans.framework.GlobalConf;
import com.cole2sworld.ColeBans.framework.PermissionSet;
import com.cole2sworld.ColeBans.handlers.YAMLBanHandler;

public final class Reload implements CBCommand {

	@Override
	public String run(String[] args, CommandSender admin) throws Exception {
		if (!(new PermissionSet(admin).canReload)) return ChatColor.RED+"You don't have permission to do that.";
		Main.instance.reloadConfig();
		GlobalConf.load();
		if (Main.instance.banHandler instanceof YAMLBanHandler) {
			((YAMLBanHandler) Main.instance.banHandler).reload();
		}
		admin.sendMessage("Configuration reloaded.");
		return null;
	}

}
