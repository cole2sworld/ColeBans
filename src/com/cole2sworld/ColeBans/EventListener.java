package com.cole2sworld.ColeBans;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import com.cole2sworld.ColeBans.handlers.BanData;
import com.cole2sworld.ColeBans.handlers.BanHandler;
import com.cole2sworld.ColeBans.handlers.BanHandler.Type;
import com.nijikokun.bukkit.Permissions.Permissions;

public class EventListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		BanData bd = Main.banHandler.getBanData(event.getName(), BanHandler.SYSTEM_ADMIN_NAME);
		Type banType = bd.getType();
		String player = event.getName();
		if (banType == Type.PERMANENT) {
			String banReason = bd.getReason();
			event.setResult(Result.KICK_BANNED);
			event.setKickMessage(ChatColor.valueOf(GlobalConf.banColor)+GlobalConf.banMessage.replace("%reason", banReason));
			return;
		}
		if (GlobalConf.allowTempBans) {
			Long tempBanTime = Main.banHandler.getBanData(player, BanHandler.SYSTEM_ADMIN_NAME).getTime();
			if (tempBanTime > -1) {
				Long tempBanMins = tempBanTime-System.currentTimeMillis();
				tempBanMins /= 1000;
				tempBanMins /= 60;
				event.setResult(Result.KICK_BANNED);
				event.setKickMessage(ChatColor.valueOf(GlobalConf.tempBanColor)+GlobalConf.tempBanMessage.replace("%time", tempBanMins.toString()).replace("%plural", Main.getPlural(tempBanMins)));
				return;
			}
		}
		event.setResult(Result.ALLOWED);
	}
	@EventHandler(priority = EventPriority.MONITOR)
    public void onPluginEnable(PluginEnableEvent event) {
    	if (Main.instance.permissionsHandler == null) {
    		Plugin permissions = Main.instance.getServer().getPluginManager().getPlugin("Permissions");
    		if (permissions != null) {
    			if (permissions.isEnabled() & permissions.getClass().getName().equals("com.nijikokun.bukkit.Permissions.Permissions")) {
    				Main.instance.permissionsHandler = ((Permissions)permissions).getHandler();
    				System.out.println(GlobalConf.logPrefix+"Hooked into Nijikokun-like permissions.");
    			}
    		}
    	}
    }
}
