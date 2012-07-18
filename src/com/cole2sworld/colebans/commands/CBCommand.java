package com.cole2sworld.colebans.commands;

import org.bukkit.command.CommandSender;
/**
 * Abstract class for the dynamic command handler.
 *
 */
public interface CBCommand {
	/**
	 * Called when a CommandSender sends a command relevant to this CBCommand.
	 * @param args The String[] passed by onCommand.
	 * @param admin The CommandSender that initiated this.
	 * @return null if no error occurred, detail message (user-friendly) if an error occured.
	 * @throws Exception 
	 */
	public abstract String run(String[] args, CommandSender admin) throws Exception;
}
