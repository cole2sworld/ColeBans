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
/**
 * Event listener for ColeBans.
 * @since v1 Apricot
 */
final class EventListener implements Listener {
	/**
	 * Checks if the player is banned, if so it will kick them out.
	 * @param event The PlayerPreLoginEvent created by Bukkit.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerPreLogin(final PlayerPreLoginEvent event) {
		BanData bd = Main.instance.banHandler.getBanData(event.getName(), BanHandler.SYSTEM_ADMIN_NAME);
		Type banType = bd.getType();
		String player = event.getName();
		if (banType == Type.PERMANENT) {
			String banReason = bd.getReason();
			event.setResult(Result.KICK_BANNED);
			event.setKickMessage(ChatColor.valueOf(GlobalConf.get("banColor").asString())+GlobalConf.get("banMessage").asString().replace("%reason", banReason));
			return;
		}
		if (GlobalConf.get("allowTempBans").asBoolean()) {
			Long tempBanTime = Main.instance.banHandler.getBanData(player, BanHandler.SYSTEM_ADMIN_NAME).getTime();
			if (tempBanTime > -1) {
				Long tempBanMins = tempBanTime-System.currentTimeMillis();
				tempBanMins /= 1000;
				tempBanMins /= 60;
				event.setResult(Result.KICK_BANNED);
				event.setKickMessage(ChatColor.valueOf(GlobalConf.get("tempBanColor").asString())+GlobalConf.get("tempBanMessage").asString().replace("%time", tempBanMins.toString()).replace("%plural", Util.getPlural(tempBanMins, true)));
				return;
			}
		}
		event.setResult(Result.ALLOWED);
	}
	/**
	 * Monitors for when Permissions is initialized.
	 * @param event The PluginEnableEvent created by Bukkit.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public final void onPluginEnable(PluginEnableEvent event) {
    	if (Main.instance.permissionsHandler == null) {
    		Plugin permissions = Main.instance.getServer().getPluginManager().getPlugin("Permissions");
    		if (permissions != null) {
    			if (permissions.isEnabled() && permissions.getClass().getName().equals("com.nijikokun.bukkit.Permissions.Permissions")) {
    				Main.instance.permissionsHandler = ((Permissions)permissions).getHandler();
    				System.out.println(Main.PREFIX+"Hooked into Nijikokun-like permissions.");
    			}
    		}
    	}
    }
}
