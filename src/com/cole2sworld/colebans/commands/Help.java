package com.cole2sworld.colebans.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class Help implements CBCommand {
	private static final Map<String, String>	HELP	= new HashMap<String, String>();
	static {
		HELP.put("/ban", "Ban a player");
		HELP.put("/check", "Check if a player is banned");
		HELP.put("/kick", "Kick a player");
		HELP.put("/tempban", "Temporarily ban a player");
		HELP.put("/unban", "Unban a player");
		HELP.put("/cb actions", "Print logged actions made by/on a player");
		HELP.put("/cb count", "Count all bans ever made by this server");
		HELP.put("/cb debug", "Toggle debug mode");
		HELP.put("/cb export", "Dump all bans to banned-players.txt");
		HELP.put("/cb extend", "Extend a tempban");
		HELP.put("/cb elevate", "Elevate a tempban to a ban");
		HELP.put("/cb help", "Print this help");
		HELP.put("/cb reload", "Reload all ban handlers");
		HELP.put("/cb switch", "Switch ban handlers");
		HELP.put("/cb version", "Print the ColeBans version");
		HELP.put("/cb hammer", "Get a banhammer");
		HELP.put("/cb freeze", "Freeze a player");
		HELP.put("/cb thaw", "Thaw a player");
	}
	
	public Help() {
	}
	
	@Override
	public String run(final String[] args, final CommandSender admin) {
		admin.sendMessage(ChatColor.YELLOW + "== ColeBans Help ==");
		for (final Entry<String, String> entry : HELP.entrySet()) {
			admin.sendMessage(ChatColor.AQUA + entry.getKey() + " - " + entry.getValue());
		}
		return "Go to " + ChatColor.AQUA + "http://c2wr.com/cbwk" + ChatColor.WHITE + " for more help.";
	}
}
