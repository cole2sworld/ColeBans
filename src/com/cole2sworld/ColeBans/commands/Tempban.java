package com.cole2sworld.ColeBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cole2sworld.ColeBans.GlobalConf;
import com.cole2sworld.ColeBans.Main;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
/**
 * The Tempban command. Handles temporarily banning players through commands.
 *
 */
public class Tempban extends CBCommand {
	@Override
	public String run(String[] args, CommandSender admin) {
		String error = null;
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
						Main.instance.banHandler.tempBanPlayer(victim, time, admin.getName());
						Player playerObj = Main.instance.server.getPlayer(victim);
						if (playerObj != null) {
							playerObj.kickPlayer(ChatColor.valueOf(GlobalConf.tempBanColor)+"Temporarily banned for "+time+" minute"+Main.getPlural(time, true)+".");
							if (GlobalConf.fancyEffects) {
								World world = playerObj.getWorld();
								world.createExplosion(playerObj.getLocation(), 0);
							}
						}
						if (GlobalConf.announceBansAndKicks) Main.instance.server.broadcastMessage(ChatColor.valueOf(GlobalConf.tempBanColor)+victim+" was temporarily banned! ["+time+" minute"+Main.getPlural(time, true)+"]");
					} catch (PlayerAlreadyBannedException e) {
						error = ChatColor.DARK_RED+victim+" is already banned!";
					} catch (UnsupportedOperationException e) {
						error = ChatColor.DARK_RED+"Temporary bans are disabled!";
					}
				}
			}
			catch (NumberFormatException e) {
				error = ChatColor.RED+"Expected number for minutes, got String (or number too high)";
			}
		}
		return error;
	}
}
