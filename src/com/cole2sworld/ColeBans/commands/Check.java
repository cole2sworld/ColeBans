package com.cole2sworld.ColeBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.ColeBans.Main;
import com.cole2sworld.ColeBans.handlers.BanData;
import com.cole2sworld.ColeBans.handlers.BanHandler;
/**
 * The Check command. Handles checking if players are banned through commands.
 *
 */
public class Check extends CBCommand {
	@Override
	public String run(String[] args, CommandSender sender) {
		String error = null;
		if (args.length < 1) error = ChatColor.RED+"You must specify a player";
		else if (args.length > 1) error = ChatColor.RED+"Too many arguments. Usage: /lookup <player>";
		else {
			String victim = args[0];
			BanData bd = Main.instance.banHandler.getBanData(victim, sender.getName());
			if (bd.getType() == BanHandler.Type.PERMANENT) {
				sender.sendMessage(ChatColor.RED+"-- "+ChatColor.DARK_RED+victim+ChatColor.RED+" --");
				sender.sendMessage(ChatColor.RED+"Ban Type: "+ChatColor.DARK_RED+"Permanent");
				sender.sendMessage(ChatColor.RED+"Reason: "+ChatColor.DARK_RED+bd.getReason());
			}
			else if (bd.getType() == BanHandler.Type.TEMPORARY) {
				sender.sendMessage(ChatColor.YELLOW+"-- "+ChatColor.GOLD+victim+ChatColor.YELLOW+" --");
				sender.sendMessage(ChatColor.YELLOW+"Ban Type: "+ChatColor.GOLD+"Temporary");
				long timeRemaining = bd.getTime()-System.currentTimeMillis();
				timeRemaining /= 1000;
				timeRemaining /= 60;
				sender.sendMessage(ChatColor.YELLOW+"Time Remaining (Minutes): "+ChatColor.GOLD+timeRemaining);
			}
			else if (bd.getType() == BanHandler.Type.NOT_BANNED) {
				sender.sendMessage(ChatColor.AQUA+"-- "+ChatColor.DARK_AQUA+victim+ChatColor.AQUA+" --");
				sender.sendMessage(ChatColor.AQUA+"Ban Type: "+ChatColor.DARK_AQUA+"Not Banned");
			}
		}
		return error;
	}
}
