package com.cole2sworld.colebans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.colebans.ActionLogManager;
import com.cole2sworld.colebans.ColeBansPlugin;
import com.cole2sworld.colebans.framework.GlobalConf;
import com.cole2sworld.colebans.framework.PermissionSet;
import com.cole2sworld.colebans.handlers.BanData;
import com.cole2sworld.colebans.handlers.BanHandler.Type;

public class Elevate implements CBCommand {
	
	@Override
	public String run(final String[] args, final CommandSender admin) throws Exception {
		if (args.length < 1) return ChatColor.RED + "Not enough arguments. /cb elevate <player>";
		if (!new PermissionSet(admin).canBan) return ChatColor.RED + "You don't have permission to do that.";
		final BanData bd = ColeBansPlugin.instance.banHandler.getBanData(args[0], admin.getName());
		if (bd.getType() != Type.NOT_BANNED) {
			if (bd.getType() == Type.TEMPORARY) {
				ColeBansPlugin.instance.banHandler.unbanPlayer(bd.getVictim(), admin.getName());
				ColeBansPlugin.instance.banHandler.banPlayer(bd.getVictim(), bd.getReason(), admin.getName());
				if (GlobalConf.get("announceBansAndKicks").asBoolean()) {
					ColeBansPlugin.instance.server.broadcastMessage(ChatColor.valueOf(GlobalConf.get("banColor").asString())
							+ bd.getVictim() + " was banned! [" + bd.getReason() + "]");
				}
				ActionLogManager.addEntry(ActionLogManager.Type.ELEVATE, admin.getName(), bd.getVictim());
			} else
				return ChatColor.RED + "That player isn't tempbanned.";
		} else
			return ChatColor.RED + "That player isn't banned!";
		return null;
	}
	
}
