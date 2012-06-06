package com.cole2sworld.ColeBans.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cole2sworld.ColeBans.framework.GlobalConf;

public class Hammer implements CBCommand {
	public String run(String[] args, CommandSender admin) throws Exception {
		if (admin instanceof Player) {
			((Player)admin).getInventory().addItem(new ItemStack(Material.GOLD_HOE, 1, Short.MIN_VALUE));
			admin.sendMessage(ChatColor.AQUA+"Left click: "+GlobalConf.get("banhammer.leftClickAction").asString().toLowerCase()+"; "+"Right click: "+GlobalConf.get("banhammer.rightClickAction").asString().toLowerCase());
		} else {
			return ChatColor.RED+"You don't have an inventory.";
		}
		return null;
	}
}
