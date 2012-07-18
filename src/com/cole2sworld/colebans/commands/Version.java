package com.cole2sworld.colebans.commands;

import org.bukkit.command.CommandSender;

import com.cole2sworld.colebans.Main;

public class Version implements CBCommand {

	@Override
	public String run(String[] args, CommandSender admin) throws Exception {
		return "v"+Main.instance.getDescription().getVersion();
	}

}
