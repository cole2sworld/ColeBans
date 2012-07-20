package com.cole2sworld.colebans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.colebans.ActionLogManager;
import com.cole2sworld.colebans.ActionLogManager.Type;
import com.cole2sworld.colebans.ColeBansPlugin;
import com.cole2sworld.colebans.framework.PermissionSet;
import com.cole2sworld.colebans.framework.PlayerOfflineException;

/**
 * The Kick command. Handles kicking players through commands.
 * 
 */
public final class Kick implements CBCommand {
	public Kick() {
	}
	
	@Override
	public String run(final String[] args, final CommandSender admin) {
		if (!(new PermissionSet(admin).canKick))
			return ChatColor.RED + "You don't have permission to do that.";
		String error = null;
		if (args.length < 1) {
			error = ChatColor.RED + "You must specify a player, or a player and a reason.";
		} else {
			final String victim = args[0];
			String reason;
			final StringBuilder reasonBuilder = new StringBuilder();
			if (args.length > 1) {
				reasonBuilder.append(args[1]);
				for (int i = 2; i < args.length; i++) {
					reasonBuilder.append(" ");
					reasonBuilder.append(args[i]);
				}
				reason = reasonBuilder.toString();
			} else {
				reason = null;
			}
			try {
				ColeBansPlugin.instance.kickPlayer(victim, reason);
				ActionLogManager.addEntry(Type.KICK, admin.getName(), victim);
			} catch (final PlayerOfflineException e) {
				error = ChatColor.DARK_RED + victim + " is not online!";
			}
		}
		return error;
	}
}
