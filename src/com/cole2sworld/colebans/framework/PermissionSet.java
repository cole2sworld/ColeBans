package com.cole2sworld.colebans.framework;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cole2sworld.colebans.Main;
/**
 * Holds all possible permissions for a player using ColeBans, or a Console. If it is a Console, all permissions will be 'true'
 *
 */
public final class PermissionSet {
	public final boolean canBan;
	public final boolean canTempBan;
	public final boolean canUnBan;
	public final boolean canKick;
	public final boolean canLookup;
	public final boolean canSwitch;
	public final boolean canReload;
	public final boolean canCount;
	public final boolean canLog;
	public final boolean canBanhammer;
	public final boolean canFreeze;
	public final boolean console;
	public PermissionSet(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			canBan = Main.instance.hasPermission(player, "colebans.ban");
			canTempBan = Main.instance.hasPermission(player, "colebans.tempban");
			canUnBan = Main.instance.hasPermission(player, "colebans.unban");
			canLookup = Main.instance.hasPermission(player, "colebans.lookup") | Main.instance.hasPermission(player, "colebans.check");
			canKick = Main.instance.hasPermission(player, "colebans.kick");
			canSwitch = Main.instance.hasPermission(player, "colebans.switch");
			canReload = Main.instance.hasPermission(player, "colebans.reload");
			canCount = Main.instance.hasPermission(player, "colebans.count");
			canLog = Main.instance.hasPermission(player, "colebans.viewlog");
			canBanhammer = Main.instance.hasPermission(player, "colebans.banhammer");
			canFreeze = Main.instance.hasPermission(player, "colebans.freeze");
			console = false;
		}
		else {
			canBan = true;
			canTempBan = true;
			canUnBan = true;
			canLookup = true;
			canKick = true;
			canSwitch = true;
			canReload = true;
			canCount = true;
			canLog = true;
			canBanhammer = true;
			canFreeze = true;
			console = true;
		}
	}
}
