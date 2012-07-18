package com.cole2sworld.colebans.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cole2sworld.colebans.ActionLogManager;
import com.cole2sworld.colebans.Main;
import com.cole2sworld.colebans.Util;
import com.cole2sworld.colebans.ActionLogManager.Type;
import com.cole2sworld.colebans.framework.GlobalConf;
import com.cole2sworld.colebans.framework.PermissionSet;
import com.cole2sworld.colebans.framework.PlayerAlreadyBannedException;

/**
 * The Tempban command. Handles temporarily banning players through commands.
 * 
 */
public final class Tempban implements CBCommand {
	@Override
	public String run(final String[] args, final CommandSender admin) {
		if (!(new PermissionSet(admin).canTempBan))
			return ChatColor.RED + "You don't have permission to do that.";
		String error = null;
		if (args.length < 2) {
			error = ChatColor.RED + "You must specify a player and time (in minutes).";
		} else if (args.length > 2) {
			error = ChatColor.RED + "Too many arguments. Usage: /tempban <player> <minutes>";
		} else {
			final String victim = args[0];
			try {
				final Long time = new Long(args[1]);
				if (time > 2880) {
					error = ChatColor.RED + "You cannot temp ban for more than 2 days!";
				} else {
					try {
						final StringBuilder reasonBuilder = new StringBuilder();
						reasonBuilder.append(args[2]);
						for (int i = 3; i < args.length; i++) {
							reasonBuilder.append(" ");
							reasonBuilder.append(args[i]);
						}
						final String reason = reasonBuilder.toString();
						Main.instance.banHandler.tempBanPlayer(victim, time, reason,
								admin.getName());
						final Player playerObj = Main.instance.server.getPlayer(victim);
						if (playerObj != null) {
							playerObj.kickPlayer(ChatColor.valueOf(GlobalConf.get("tempBanColor")
									.asString())
									+ "Temporarily banned for "
									+ time
									+ " minute"
									+ Util.getPlural(time, true) + " for " + reason);
							if (GlobalConf.get("fancyEffects").asBoolean()) {
								final World world = playerObj.getWorld();
								world.createExplosion(playerObj.getLocation(), 0);
							}
						}
						if (GlobalConf.get("announceBansAndKicks").asBoolean()) {
							Main.instance.server.broadcastMessage(ChatColor.valueOf(GlobalConf.get(
									"tempBanColor").asString())
									+ victim
									+ " was temporarily banned! ["
									+ time
									+ " minute"
									+ Util.getPlural(time, true) + "]");
						}
						ActionLogManager.addEntry(Type.TEMPBAN, admin.getName(), victim);
					} catch (final PlayerAlreadyBannedException e) {
						error = ChatColor.DARK_RED + victim + " is already banned!";
					} catch (final UnsupportedOperationException e) {
						error = ChatColor.DARK_RED + "Temporary bans are disabled!";
					}
				}
			} catch (final NumberFormatException e) {
				error = ChatColor.RED
						+ "Expected number for minutes, got String (or number too high)";
			}
		}
		return error;
	}
}