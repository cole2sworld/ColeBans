package com.cole2sworld.ColeBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.ColeBans.GlobalConf;
import com.cole2sworld.ColeBans.Main;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;

public class Ban extends CBCommand {
	public String run(String[] args, CommandSender admin) {
		String error = null;
		if (args.length < 2) error = ChatColor.RED+"You must specify a player and reason.";
		else {
			String victim = args[0];
			StringBuilder reasonBuilder = new StringBuilder();
			reasonBuilder.append(args[1]);
			for (int i = 2; i<args.length; i++) {
				reasonBuilder.append(" ");
				reasonBuilder.append(args[i]);
			}
			String reason = reasonBuilder.toString();
			try {
				Main.banHandler.banPlayer(victim, reason, admin.getName());
				if (GlobalConf.announceBansAndKicks) Main.server.broadcastMessage(ChatColor.valueOf(GlobalConf.banColor)+victim+" was banned! ["+reason+"]");
			} catch (PlayerAlreadyBannedException e) {
				error = ChatColor.DARK_RED+victim+" is already banned!";
			}
		}
		return error;
	}
}
