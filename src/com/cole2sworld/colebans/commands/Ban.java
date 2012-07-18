package com.cole2sworld.colebans.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cole2sworld.colebans.ActionLogManager;
import com.cole2sworld.colebans.Main;
import com.cole2sworld.colebans.ActionLogManager.Type;
import com.cole2sworld.colebans.framework.GlobalConf;
import com.cole2sworld.colebans.framework.PermissionSet;
import com.cole2sworld.colebans.framework.PlayerAlreadyBannedException;
/**
 * The Ban command. Handles banning players through commands.
 *
 */
public final class Ban implements CBCommand {
	@Override
	public String run(String[] args, CommandSender admin) {
		if (!(new PermissionSet(admin).canBan)) return ChatColor.RED+"You don't have permission to do that.";
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
				Player playerObj = Main.instance.server.getPlayerExact(victim);
				if (playerObj != null) {
					playerObj.kickPlayer(ChatColor.valueOf(GlobalConf.get("banColor").asString())+"BANNED: "+reason);
					if (GlobalConf.get("fancyEffects").asBoolean()) {
						World world = playerObj.getWorld();
						world.createExplosion(playerObj.getLocation(), 0);
					}
				}
				if (GlobalConf.get("announceBansAndKicks").asBoolean()) Main.instance.server.broadcastMessage(ChatColor.valueOf(GlobalConf.get("banColor").asString())+victim+" was banned! ["+reason+"]");
				ActionLogManager.addEntry(Type.BAN, admin.getName(), victim);
			} catch (PlayerAlreadyBannedException e) {
				error = ChatColor.DARK_RED+victim+" is already banned!";
			}
		}
		return error;
	}
}
