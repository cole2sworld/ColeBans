package com.cole2sworld.ColeBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.ColeBans.GlobalConf;
import com.cole2sworld.ColeBans.Main;
import com.cole2sworld.ColeBans.framework.MethodNotSupportedException;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;

public class TempBan {
	public static String run(String[] args, CommandSender admin) {
		String error = null;
		if (args.length < 2) error = ChatColor.RED+"You must specify a player and time (in minutes).";
		else if (args.length > 2) error = ChatColor.RED+"Too many arguments. Usage: /tempban <player> <minutes>";
		else {
			String victim = args[0];
			try {
				Long time = new Long(args[1]);
				if (time > 2880) {
					error = ChatColor.RED+"You cannot temp ban for more than 2 days!";
				}
				else {
					try {
						Main.banHandler.tempBanPlayer(victim, time, admin.getName());
						if (GlobalConf.announceBansAndKicks) Main.server.broadcastMessage(ChatColor.valueOf(GlobalConf.tempBanColor)+victim+" was temporarily banned! ["+time+" minute"+Main.getPlural(time)+"]");
					} catch (PlayerAlreadyBannedException e) {
						error = ChatColor.DARK_RED+victim+" is already banned!";
					} catch (MethodNotSupportedException e) {
						error = ChatColor.DARK_RED+"Temporary bans are disabled!";
					}
				}
			}
			catch (NumberFormatException e) {
				error = ChatColor.RED+"Expected number for minutes, got String (or number too high)";
			}
		}
		return error;
	}
}
