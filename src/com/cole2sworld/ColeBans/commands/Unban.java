package com.cole2sworld.ColeBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.ColeBans.ActionLogManager;
import com.cole2sworld.ColeBans.Main;
import com.cole2sworld.ColeBans.ActionLogManager.Type;
import com.cole2sworld.ColeBans.framework.GlobalConf;
import com.cole2sworld.ColeBans.framework.PermissionSet;
import com.cole2sworld.ColeBans.framework.PlayerNotBannedException;
/**
 * The Unban command. Handles unbanning players through commands.
 *
 */
public final class Unban implements CBCommand {
	@Override
	public String run(String[] args, CommandSender admin) {
		if (!(new PermissionSet(admin).canUnBan)) return ChatColor.RED+"You don't have permission to do that.";
		String error = null;
		if (args.length < 1) error = ChatColor.RED+"You must specify a player";
		else if (args.length > 1) error = ChatColor.RED+"Too many arguments. Usage: /unban <player>";
		else {
			String victim = args[0];
			try {
				Main.instance.banHandler.unbanPlayer(victim, admin.getName());
				if (GlobalConf.get("announceBansAndKicks").asBoolean()) Main.instance.server.broadcastMessage(ChatColor.GREEN+victim+" was unbanned!");
				ActionLogManager.addEntry(Type.UNBAN, admin.getName(), victim);
			} catch (PlayerNotBannedException e) {
				error = ChatColor.DARK_RED+victim+" is not banned!";
			}
		}
		return error;
	}
}
