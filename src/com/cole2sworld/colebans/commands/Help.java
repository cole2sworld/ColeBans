package com.cole2sworld.colebans.commands;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class Help implements CBCommand {
	private static final HashMap<String, String> help = new HashMap<String, String>();
	@Override
	public String run(String[] args, CommandSender admin) {
		admin.sendMessage(ChatColor.YELLOW+"== ColeBans Help ==");
		for (Entry<String, String> entry : help.entrySet()) {
			admin.sendMessage(ChatColor.AQUA+entry.getKey()+" - "+entry.getValue());
		}
		return "Go to "+ChatColor.AQUA+"http://c2wr.com/cbwk"+ChatColor.WHITE+" for more help.";
	}
	static {
		help.put("/ban", "Ban a player");
		help.put("/check", "Check if a player is banned");
		help.put("/kick", "Kick a player");
		help.put("/tempban", "Temporarily ban a player");
		help.put("/unban", "Unban a player");
		help.put("/cb actions", "Print logged actions made by/on a player");
		help.put("/cb count", "Count all bans ever made by this server");
		help.put("/cb debug", "Toggle debug mode");
		help.put("/cb help", "Print this help");
		help.put("/cb lban", "Ban a player from this server using a seperate file");
		help.put("/cb reload", "Reload all ban handlers");
		help.put("/cb switch", "Switch ban handlers");
		help.put("/cb version", "Print the ColeBans version");
		help.put("/cb hammer", "Get a banhammer");
		help.put("/cb freeze", "Freeze a player");
		help.put("/cb thaw", "Thaw a player");
	}
}
