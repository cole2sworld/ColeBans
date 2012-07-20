package com.cole2sworld.colebans.commands;

import org.bukkit.command.CommandSender;

import com.cole2sworld.colebans.ColeBansPlugin;

public class Version implements CBCommand {
	public Version() {
	}
	
	@Override
	public final String run(final String[] args, final CommandSender admin) throws Exception {
		return "v" + ColeBansPlugin.instance.getDescription().getVersion();
	}
	
}
