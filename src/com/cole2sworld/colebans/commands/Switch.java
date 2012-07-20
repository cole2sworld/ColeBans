package com.cole2sworld.colebans.commands;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cole2sworld.colebans.ActionLogManager;
import com.cole2sworld.colebans.ActionLogManager.Type;
import com.cole2sworld.colebans.ColeBansPlugin;
import com.cole2sworld.colebans.Util;
import com.cole2sworld.colebans.framework.GlobalConf;
import com.cole2sworld.colebans.framework.PermissionSet;
import com.cole2sworld.colebans.handlers.BanHandler;

public final class Switch implements CBCommand {
	public Switch() {
	}
	
	@Override
	public String run(final String[] args, final CommandSender admin) {
		final PermissionSet perm = new PermissionSet(admin);
		if (!perm.canSwitch) return ChatColor.RED + "You don't have permission to do that.";
		if ((args.length > 1) || (args.length == 0))
			return ChatColor.RED
					+ "The switch command must be used with only the destination handler as an argument";
		if (ColeBansPlugin.instance.banHandler.getClass().getSimpleName()
				.replace(GlobalConf.get("advanced.suffix").asString(), "").equals(args[0]))
			return ChatColor.YELLOW + "You're already using that ban handler!";
		try {
			final BanHandler dest = Util.lookupHandler(args[0]);
			ColeBansPlugin.LOG.info(ColeBansPlugin.PREFIX
					+ "Starting conversion from "
					+ ColeBansPlugin.instance.banHandler.getClass().getSimpleName()
							.replace(GlobalConf.get("advanced.suffix").asString(), "") + " to "
					+ args[0]);
			if (!perm.console) {
				admin.sendMessage(ChatColor.YELLOW + "Starting conversion...");
			}
			final long oldTime = System.currentTimeMillis() / 1000;
			ColeBansPlugin.instance.banHandler.convert(dest);
			ColeBansPlugin.instance.banHandler.onDisable();
			GlobalConf.set("settings.banHandler", args[0]);
			ColeBansPlugin.instance.saveConfig();
			ColeBansPlugin.instance.banHandler = dest;
			final long timeSince = (System.currentTimeMillis() / 1000) - oldTime;
			ColeBansPlugin.LOG.info(ColeBansPlugin.PREFIX + "Conversion succeeded! Took " + timeSince + " seconds.");
			if (!perm.console) {
				admin.sendMessage(ChatColor.GREEN + "Conversion succeeded! Took " + timeSince
						+ " seconds.");
			}
			ActionLogManager.addEntry(Type.SWITCH, admin.getName(), args[0]);
		} catch (final IllegalArgumentException e) {
			return ChatColor.DARK_RED + "Given ban handler is wierdly implemented";
		} catch (final SecurityException e) {
			return ChatColor.DARK_RED + "Plugin conflict!";
		} catch (final ClassCastException e) {
			return ChatColor.DARK_RED + "Given ban handler is not actually a ban handler";
		} catch (final ClassNotFoundException e) {
			return ChatColor.DARK_RED + "No such ban handler '" + args[0]
					+ "' Make sure you got the caps right!";
		} catch (final IllegalAccessException e) {
			return ChatColor.DARK_RED + "Given ban handler is wierdly implemented";
		} catch (final InvocationTargetException e) {
			return ChatColor.DARK_RED + "Given ban handler is wierdly implemented";
		} catch (final NoSuchMethodException e) {
			return ChatColor.DARK_RED + "Given ban handler is wierdly implemented";
		}
		return null;
	}
	
}
