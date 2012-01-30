package com.cole2sworld.ColeBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cole2sworld.ColeBans.GlobalConf;
import com.cole2sworld.ColeBans.Main;
import com.cole2sworld.ColeBans.framework.MethodNotSupportedException;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.ColeBans.framework.PlayerNotBannedException;
import com.cole2sworld.ColeBans.framework.PlayerOfflineException;
import com.cole2sworld.ColeBans.handlers.BanData;
import com.cole2sworld.ColeBans.handlers.BanHandler;

public class CommandHandler {
	public static boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		String error = "An unspecified error has occured while running this command.";
		boolean canBan = false;
		boolean canTempBan = false;
		boolean canUnBan = false;
		boolean canKick = false;
		boolean canLookup = false;
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (GlobalConf.mcbansNodes) {
				canBan = Main.instance.hasPermission(player, "mcbans.ban") | Main.instance.hasPermission(player, "mcbans.ban.local") | Main.instance.hasPermission(player, "mcbans.ban.global");
				canTempBan = Main.instance.hasPermission(player, "mcbans.ban.temp");
				canUnBan = Main.instance.hasPermission(player, "mcbans.unban");
				canLookup = Main.instance.hasPermission(player, "mcbans.lookup");
				canKick = Main.instance.hasPermission(player, "mcbans.kick");
			}
			else {
				canBan = Main.instance.hasPermission(player, "colebans.ban");
				canTempBan = Main.instance.hasPermission(player, "colebans.tempban");
				canUnBan = Main.instance.hasPermission(player, "colebans.unban");
				canLookup = Main.instance.hasPermission(player, "colebans.lookup") | Main.instance.hasPermission(player, "colebans.check");
				canKick = Main.instance.hasPermission(player, "colebans.kick");
			}
		}
		else {
			canBan = true;
			canTempBan = true;
			canUnBan = true;
			canLookup = true;
			canKick = true;
		}
		if (cmdLabel.equalsIgnoreCase("ban")) {
			if (canBan) {
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
						Main.banHandler.banPlayer(victim, reason);
						if (GlobalConf.announceBansAndKicks) Main.server.broadcastMessage(ChatColor.valueOf(GlobalConf.banColor)+victim+" was banned! ["+reason+"]");
						return true;
					} catch (PlayerAlreadyBannedException e) {
						error = ChatColor.DARK_RED+victim+" is already banned!";
					}
				}
			}
			else error = ChatColor.RED+"You do not have permission to use the "+cmdLabel+" command.";
		}
		else if (cmdLabel.equalsIgnoreCase("tempban")) {
			if (canTempBan) {
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
								Main.banHandler.tempBanPlayer(victim, time);
								if (GlobalConf.announceBansAndKicks) Main.server.broadcastMessage(ChatColor.valueOf(GlobalConf.tempBanColor)+victim+" was temporarily banned! ["+time+" minute"+Main.getPlural(time)+"]");
								return true;
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
			}
			else error = ChatColor.RED+"You do not have permission to use the "+cmdLabel+" command.";
		}
		else if (cmdLabel.equalsIgnoreCase("unban")) {
			if (canUnBan) {
				if (args.length < 1) error = ChatColor.RED+"You must specify a player";
				else if (args.length > 1) error = ChatColor.RED+"Too many arguments. Usage: /unban <player>";
				else {
					String victim = args[0];
					try {
						Main.banHandler.unbanPlayer(victim);
						if (GlobalConf.announceBansAndKicks) Main.server.broadcastMessage(ChatColor.GREEN+victim+" was unbanned!");
						return true;
					} catch (PlayerNotBannedException e) {
						error = ChatColor.DARK_RED+victim+" is not banned!";
					}
				}
			}
			else error = ChatColor.RED+"You do not have permission to use the "+cmdLabel+" command.";
		}
		else if (cmdLabel.equalsIgnoreCase("lookup") | cmdLabel.equalsIgnoreCase("check")) {
			if (canLookup) {
				if (args.length < 1) error = ChatColor.RED+"You must specify a player";
				else if (args.length > 1) error = ChatColor.RED+"Too many arguments. Usage: /lookup <player>";
				else {
					String victim = args[0];
					BanData bd = Main.banHandler.getBanData(victim);
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
					return true;
				}
			}
			else error = ChatColor.RED+"You do not have permission to use the "+cmdLabel+" command.";
		}
		else if (cmdLabel.equalsIgnoreCase("kick")) {
			if (canKick) {
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
			}
			else error = ChatColor.RED+"You do not have permission to use the "+cmdLabel+" command.";
		}
		sender.sendMessage(error);
		return true;
	}
}
