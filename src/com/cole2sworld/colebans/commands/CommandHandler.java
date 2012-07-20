package com.cole2sworld.colebans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.cole2sworld.colebans.ColeBansPlugin;

/**
 * Static command handler. For handling traditional /&lt;cmd&gt; commands
 * instead of the dynamic /cb &lt;cmd&gt; [args] commands.
 * 
 */
// LOWPRI deprecate when applicable
public class CommandHandler {
	/**
	 * @param sender
	 *            The originator of the command
	 * @param cmd
	 *            The command
	 * @param cmdLabel
	 *            The command label
	 * @param args
	 *            The space-seperated arguments
	 */
	@SuppressWarnings("unused")
	public static boolean onCommand(final CommandSender sender, final Command cmd,
			final String cmdLabel, final String[] args) {
		ColeBansPlugin.debug("Static handler called");
		String error = ChatColor.YELLOW
				+ "An unspecified error has occurred while running this command.";
		ColeBansPlugin.debug("PermissionSet allocated");
		if (cmdLabel.equalsIgnoreCase("ban")) {
			ColeBansPlugin.debug("Ban command");
			final String result = new Ban().run(args, sender);
			if (result == null) return true;
			error = result;
		}
		else if (cmdLabel.equalsIgnoreCase("tempban")) {
			ColeBansPlugin.debug("Tempban command");
			final String result = new Tempban().run(args, sender);
			if (result == null) return true;
			error = result;
		}
		else if (cmdLabel.equalsIgnoreCase("unban")) {
			ColeBansPlugin.debug("Unban command");
			final String result = new Unban().run(args, sender);
			if (result == null) return true;
			error = result;
		}
		else if (cmdLabel.equalsIgnoreCase("lookup") || cmdLabel.equalsIgnoreCase("check")) {
			ColeBansPlugin.debug("Lookup/Check command");
			final String result = new Check().run(args, sender);
			if (result == null) return true;
			error = result;
		}
		else if (cmdLabel.equalsIgnoreCase("kick")) {
			ColeBansPlugin.debug("Kick command");
			final String result = new Kick().run(args, sender);
			if (result == null) return true;
			error = result;
		}
		sender.sendMessage(error);
		return true;
	}
	
	public CommandHandler() {
	}
}
