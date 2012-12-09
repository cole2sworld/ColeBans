package com.cole2sworld.colebans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.colebans.ActionLogManager;
import com.cole2sworld.colebans.ColeBansPlugin;
import com.cole2sworld.colebans.framework.GlobalConf;
import com.cole2sworld.colebans.framework.PermissionSet;
import com.cole2sworld.colebans.handlers.BanData;
import com.cole2sworld.colebans.handlers.BanHandler.Type;

public class Extend implements CBCommand {
	
	@Override
	public String run(final String[] args, final CommandSender admin) throws Exception {
		if (args.length < 2) return ChatColor.RED + "Not enough arguments. /cb extend <player> <minutes>";
		if (!new PermissionSet(admin).canTempBan) return ChatColor.RED + "You don't have permission to do that.";
		final long time = Long.parseLong(args[1]);
		final BanData bd = ColeBansPlugin.instance.banHandler.getBanData(args[0], admin.getName());
		if (bd.getType() != Type.NOT_BANNED) {
			if (bd.getType() == Type.TEMPORARY) {
				long timeRemaining = bd.getTime() - System.currentTimeMillis();
				timeRemaining /= 1000;
				timeRemaining /= 60;
				ColeBansPlugin.instance.banHandler.unbanPlayer(bd.getVictim(), admin.getName());
				ColeBansPlugin.instance.banHandler.tempBanPlayer(bd.getVictim(), Math.min(2880, timeRemaining + time), bd.getReason(),
						admin.getName());
				if (GlobalConf.get("announceBansAndKicks").asBoolean()) {
					ColeBansPlugin.instance.server.broadcastMessage(ChatColor.valueOf(GlobalConf.get("tempBanColor").asString())
							+ bd.getVictim() + "'s tempban was extended! [+" + time + " minutes]");
				}
				ActionLogManager.addEntry(ActionLogManager.Type.EXTEND, admin.getName(), bd.getVictim());
			} else
				return ChatColor.RED + "That player isn't tempbanned.";
		} else
			return ChatColor.RED + "That player isn't banned!";
		return null;
	}
	
}
