package com.cole2sworld.colebans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.colebans.ActionLogManager;
import com.cole2sworld.colebans.ActionLogManager.Type;
import com.cole2sworld.colebans.ColeBansPlugin;
import com.cole2sworld.colebans.framework.GlobalConf;
import com.cole2sworld.colebans.framework.PermissionSet;
import com.cole2sworld.colebans.framework.PlayerNotBannedException;

/**
 * The Unban command. Handles unbanning players through commands.
 * 
 */
public final class Unban implements CBCommand {
	public Unban() {
	}
	
	@Override
	public String run(final String[] args, final CommandSender admin) {
		if (!(new PermissionSet(admin).canUnBan))
			return ChatColor.RED + "You don't have permission to do that.";
		String error = null;
		if (args.length < 1) {
			error = ChatColor.RED + "You must specify a player";
		} else if (args.length > 1) {
			error = ChatColor.RED + "Too many arguments. Usage: /unban <player>";
		} else {
			final String victim = args[0];
			try {
				ColeBansPlugin.instance.banHandler.unbanPlayer(victim, admin.getName());
				if (GlobalConf.get("announceBansAndKicks").asBoolean()) {
					ColeBansPlugin.instance.server.broadcastMessage(ChatColor.GREEN + victim
							+ " was unbanned!");
				}
				ActionLogManager.addEntry(Type.UNBAN, admin.getName(), victim);
			} catch (final PlayerNotBannedException e) {
				error = ChatColor.DARK_RED + victim + " is not banned!";
			}
		}
		return error;
	}
}
