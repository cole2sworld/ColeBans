package com.cole2sworld.colebans.framework;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cole2sworld.colebans.ColeBansPlugin;

/**
 * Holds all possible permissions for a player using ColeBans, or a Console. If
 * it is a Console, all permissions will be 'true'
 * 
 */
// TODO replace with something more sensible
public final class PermissionSet {
	public final boolean	canBan;
	public final boolean	canTempBan;
	public final boolean	canUnBan;
	public final boolean	canKick;
	public final boolean	canLookup;
	public final boolean	canSwitch;
	public final boolean	canReload;
	public final boolean	canCount;
	public final boolean	canLog;
	public final boolean	canBanhammer;
	public final boolean	canFreeze;
	public final boolean	canLookupIps;
	public final boolean	console;
	
	public PermissionSet(final CommandSender sender) {
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			canBan = ColeBansPlugin.hasPermission(player, "colebans.ban");
			canTempBan = ColeBansPlugin.hasPermission(player, "colebans.tempban");
			canUnBan = ColeBansPlugin.hasPermission(player, "colebans.unban");
			canLookup = ColeBansPlugin.hasPermission(player, "colebans.lookup")
					| ColeBansPlugin.hasPermission(player, "colebans.check");
			canKick = ColeBansPlugin.hasPermission(player, "colebans.kick");
			canSwitch = ColeBansPlugin.hasPermission(player, "colebans.switch");
			canReload = ColeBansPlugin.hasPermission(player, "colebans.reload");
			canCount = ColeBansPlugin.hasPermission(player, "colebans.count");
			canLog = ColeBansPlugin.hasPermission(player, "colebans.viewlog");
			canBanhammer = ColeBansPlugin.hasPermission(player, "colebans.banhammer");
			canFreeze = ColeBansPlugin.hasPermission(player, "colebans.freeze");
			canLookupIps = ColeBansPlugin.hasPermission(player, "colebans.iplookup");
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
			canLookupIps = true;
			console = true;
		}
	}
}
