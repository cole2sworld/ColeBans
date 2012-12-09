package com.cole2sworld.colebans.commands;

import java.net.InetAddress;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.colebans.ColeBansPlugin;
import com.cole2sworld.colebans.IPLogManager;
import com.cole2sworld.colebans.framework.PermissionSet;
import com.cole2sworld.colebans.handlers.BanData;
import com.cole2sworld.colebans.handlers.BanHandler;

/**
 * The Check command. Handles checking if players are banned through commands.
 * 
 */
public final class Check implements CBCommand {
	public Check() {
	}
	
	@Override
	public String run(final String[] args, final CommandSender sender) {
		if (!new PermissionSet(sender).canLookup) return ChatColor.RED + "You don't have permission to do that.";
		String error = null;
		if (args.length < 1) {
			error = ChatColor.RED + "You must specify a player";
		} else if (args.length > 1) {
			error = ChatColor.RED + "Too many arguments. Usage: /lookup <player>";
		} else {
			final String victim = args[0];
			final InetAddress ip = IPLogManager.lookupByName(victim);
			final BanData bd = ColeBansPlugin.instance.banHandler.getBanData(victim, sender.getName());
			final boolean canIps = new PermissionSet(sender).canLookupIps;
			if (bd.getType() == BanHandler.Type.PERMANENT) {
				sender.sendMessage(ChatColor.RED + "-- " + ChatColor.DARK_RED + victim + ChatColor.RED + " --");
				sender.sendMessage(ChatColor.RED + "Ban Type: " + ChatColor.DARK_RED + "Permanent");
				sender.sendMessage(ChatColor.RED + "Reason: " + ChatColor.DARK_RED + bd.getReason());
				if (bd.getCustomData("mysqlId") != null) {
					sender.sendMessage(ChatColor.RED + "Ban ID: " + ChatColor.DARK_RED + bd.getCustomData("mysqlId"));
				}
				if (canIps) {
					sender.sendMessage(ChatColor.RED + "IP Address: " + ChatColor.DARK_RED + (ip == null ? "Unknown" : ip.getHostAddress()));
				}
			} else if (bd.getType() == BanHandler.Type.TEMPORARY) {
				sender.sendMessage(ChatColor.YELLOW + "-- " + ChatColor.GOLD + victim + ChatColor.YELLOW + " --");
				sender.sendMessage(ChatColor.YELLOW + "Ban Type: " + ChatColor.GOLD + "Temporary");
				long timeRemaining = bd.getTime() - System.currentTimeMillis();
				timeRemaining /= 1000;
				timeRemaining /= 60;
				sender.sendMessage(ChatColor.YELLOW + "Time (Minutes): " + ChatColor.GOLD + timeRemaining + "/" + bd.getOriginalTime());
				sender.sendMessage(ChatColor.YELLOW + "Reason: " + ChatColor.GOLD + bd.getReason());
				if (bd.getCustomData("mysqlId") != null) {
					sender.sendMessage(ChatColor.YELLOW + "Ban ID: " + ChatColor.GOLD + bd.getCustomData("mysqlId"));
				}
				if (canIps) {
					sender.sendMessage(ChatColor.YELLOW + "IP Address: " + ChatColor.GOLD + (ip == null ? "Unknown" : ip.getHostAddress()));
				}
			} else if (bd.getType() == BanHandler.Type.NOT_BANNED) {
				sender.sendMessage(ChatColor.AQUA + "-- " + ChatColor.DARK_AQUA + victim + ChatColor.AQUA + " --");
				sender.sendMessage(ChatColor.AQUA + "Ban Type: " + ChatColor.DARK_AQUA + "Not Banned");
				if (canIps) {
					sender.sendMessage(ChatColor.AQUA + "IP Address: " + ChatColor.DARK_AQUA
							+ (ip == null ? "Unknown" : ip.getHostAddress()));
				}
			}
		}
		return error;
	}
}
