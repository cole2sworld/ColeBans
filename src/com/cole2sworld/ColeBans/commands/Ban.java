package com.cole2sworld.ColeBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cole2sworld.ColeBans.GlobalConf;
import com.cole2sworld.ColeBans.Main;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
/**
 * The Ban command. Handles banning players through commands.
 *
 */
public final class Ban implements CBCommand {
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
				Main.instance.banHandler.banPlayer(victim, reason, admin.getName());
				Player playerObj = Main.instance.server.getPlayer(victim);
				if (playerObj != null) {
					playerObj.kickPlayer(ChatColor.valueOf(GlobalConf.banColor)+"BANNED: "+reason);
					if (GlobalConf.fancyEffects) {
						World world = playerObj.getWorld();
						world.createExplosion(playerObj.getLocation(), 0);
					}
				}
				if (GlobalConf.announceBansAndKicks) Main.instance.server.broadcastMessage(ChatColor.valueOf(GlobalConf.banColor)+victim+" was banned! ["+reason+"]");
			} catch (PlayerAlreadyBannedException e) {
				error = ChatColor.DARK_RED+victim+" is already banned!";
			}
		}
		return error;
	}
}
