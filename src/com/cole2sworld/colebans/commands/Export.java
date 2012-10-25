package com.cole2sworld.colebans.commands;

import java.util.Date;

import net.minecraft.server.BanEntry;
import net.minecraft.server.ServerConfigurationManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;

import com.cole2sworld.colebans.ColeBansPlugin;
import com.cole2sworld.colebans.handlers.BanData;
import com.cole2sworld.colebans.handlers.BanHandler.Type;

/**
 * Export the ColeBans bans to the vanilla banlist.
 * 
 * @author cole2
 * 
 */
public class Export implements CBCommand {
	
	@Override
	public String run(final String[] args, final CommandSender admin) throws Exception {
		final ServerConfigurationManager nms = ((CraftServer) Bukkit.getServer()).getHandle();
		int count = 0;
		for (final BanData d : ColeBansPlugin.instance.banHandler.dump(admin.getName())) {
			final BanEntry nmsBan = new BanEntry(d.getVictim());
			nmsBan.setCreated(new Date());
			nmsBan.setReason(d.getReason());
			if (d.getType() == Type.TEMPORARY) {
				nmsBan.setExpires(new Date(d.getTime()));
			}
			nms.getNameBans().add(nmsBan);
			count++;
		}
		
		return ChatColor.GREEN + Integer.toString(count) + " bans successfully processed.";
	}
	
}
