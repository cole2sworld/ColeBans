package com.cole2sworld.ColeBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.cole2sworld.ColeBans.Main;
/**
 * Static command handler. For handling traditional /&lt;cmd&gt; commands instead of the dynamic /cb &lt;cmd&gt; [args] commands.
 *
 */
//LOWPRI deprecate when applicable
public class CommandHandler {
	/**
	 * @param sender The originator of the command
	 * @param cmd The command
	 * @param cmdLabel The command label
	 * @param args The space-seperated arguments
	 */
	public static boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		Main.debug("Static handler called");
		String error = ChatColor.YELLOW+"An unspecified error has occurred while running this command.";
		Main.debug("PermissionSet allocated");
		if (cmdLabel.equalsIgnoreCase("ban")) {
			Main.debug("Ban command");
			String result = new Ban().run(args, sender);
			if (result == null) return true;
			error = result;
		}
		else if (cmdLabel.equalsIgnoreCase("tempban")) {
			Main.debug("Tempban command");
			String result = new Tempban().run(args, sender);
			if (result == null) return true;
			error = result;
		}
		else if (cmdLabel.equalsIgnoreCase("unban")) {
			Main.debug("Unban command");
			String result = new Unban().run(args, sender);
			if (result == null) return true;
			error = result;
		}
		else if (cmdLabel.equalsIgnoreCase("lookup") || cmdLabel.equalsIgnoreCase("check")) {
			Main.debug("Lookup/Check command");
			String result = new Check().run(args, sender);
			if (result == null) return true;
			error = result;
		}
		else if (cmdLabel.equalsIgnoreCase("kick")) {
			Main.debug("Kick command");
			String result = new Kick().run(args, sender);
			if (result == null) return true;
			error = result;
		}
		sender.sendMessage(error);
		return true;
	}
}
