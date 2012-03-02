package com.cole2sworld.ColeBans.framework;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cole2sworld.ColeBans.Main;
/**
 * Holds all possible permissions for a player using ColeBans, or a Console. If it is a Console, all permissions will be 'true'
 *
 */
public class PermissionSet {
	public final boolean canBan;
	public final boolean canTempBan;
	public final boolean canUnBan;
	public final boolean canKick;
	public final boolean canLookup;
	public PermissionSet(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			canBan = Main.instance.hasPermission(player, "colebans.ban");
			canTempBan = Main.instance.hasPermission(player, "colebans.tempban");
			canUnBan = Main.instance.hasPermission(player, "colebans.unban");
			canLookup = Main.instance.hasPermission(player, "colebans.lookup") | Main.instance.hasPermission(player, "colebans.check");
			canKick = Main.instance.hasPermission(player, "colebans.kick");
		}
		else {
			canBan = true;
			canTempBan = true;
			canUnBan = true;
			canLookup = true;
			canKick = true;
		}
	}
}
