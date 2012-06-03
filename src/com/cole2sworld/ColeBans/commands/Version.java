package com.cole2sworld.ColeBans.commands;

import org.bukkit.command.CommandSender;

import com.cole2sworld.ColeBans.Main;

public class Version implements CBCommand {

	@Override
	public String run(String[] args, CommandSender admin) throws Exception {
		return "v"+Main.instance.getDescription().getVersion();
	}

}
