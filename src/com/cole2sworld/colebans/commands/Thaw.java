package com.cole2sworld.colebans.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cole2sworld.colebans.framework.PermissionSet;
import com.cole2sworld.colebans.framework.RestrictionManager;

public final class Thaw implements CBCommand {
	public Thaw() {
	}
	
	@Override
	public String run(final String[] args, final CommandSender admin) throws Exception {
		if (!new PermissionSet(admin).canFreeze)
			return ChatColor.RED + "You don't have permission to do that.";
		if (args.length < 1) return ChatColor.RED + "Not enough arguments.";
		if (Bukkit.getServer().getPlayer(args[0]) == null)
			return ChatColor.RED + "That player is not online!";
		RestrictionManager.thaw(Bukkit.getServer().getPlayer(args[0]));
		admin.sendMessage(ChatColor.YELLOW + "Thawed.");
		Bukkit.getServer()
				.getPlayer(args[0])
				.sendMessage(
						ChatColor.YELLOW
								+ "Thawed by "
								+ (admin instanceof Player ? ((Player) admin).getDisplayName()
										: admin.getName()) + ".");
		return null;
	}
}
