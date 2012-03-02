package com.cole2sworld.ColeBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.ColeBans.GlobalConf;
import com.cole2sworld.ColeBans.Main;
import com.cole2sworld.ColeBans.framework.PlayerOfflineException;
/**
 * The Kick command. Handles kicking players through commands.
 *
 */
public class Kick extends CBCommand {
	@Override
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
				Main.instance.kickPlayer(victim, reason);
				if (GlobalConf.announceBansAndKicks) Main.server.broadcastMessage(ChatColor.valueOf(GlobalConf.kickColor)+victim+" was kicked! ["+reason+"]");
			} catch (PlayerOfflineException e) {
				error = ChatColor.DARK_RED+victim+" is not online!";
			}
		}
		return error;
	}
}
