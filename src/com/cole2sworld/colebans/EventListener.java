package com.cole2sworld.colebans;

import com.nijikokun.bukkit.Permissions.Permissions;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import com.cole2sworld.colebans.framework.GlobalConf;
import com.cole2sworld.colebans.handlers.BanData;
import com.cole2sworld.colebans.handlers.BanHandler;
import com.cole2sworld.colebans.handlers.BanHandler.Type;

/**
 * Event listener for ColeBans.
 * 
 * @since v1 Apricot
 */
@SuppressWarnings("static-method")
public final class EventListener implements Listener {
	protected EventListener() {
	}
	
	/**
	 * Checks if the player is banned, if so it will kick them out.
	 * 
	 * @param event
	 *            The PlayerPreLoginEvent created by Bukkit.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPreLogin(final PlayerPreLoginEvent event) {
		System.out.println("Pre login");
		ColeBansPlugin.debug("Got pre-login for " + event.getName());
		final BanData bd = ColeBansPlugin.instance.banHandler.getBanData(event.getName(),
				BanHandler.SYSTEM_ADMIN_NAME);
		final Type banType = bd.getType();
		if (banType == Type.PERMANENT) {
			ColeBansPlugin.debug("Permanent");
			final String banReason = bd.getReason();
			event.setResult(Result.KICK_BANNED);
			event.setKickMessage(ChatColor.valueOf(GlobalConf.get("banColor").asString())
					+ GlobalConf.get("banMessage").asString().replace("%reason", banReason));
			return;
		}
		if (GlobalConf.get("allowTempBans").asBoolean()) {
			ColeBansPlugin.debug("Temp ban");
			final Long tempBanTime = bd.getTime();
			if (tempBanTime > -1) {
				ColeBansPlugin.debug("Valid");
				Long tempBanMins = tempBanTime - System.currentTimeMillis();
				tempBanMins /= 1000;
				tempBanMins /= 60;
				event.setResult(Result.KICK_BANNED);
				event.setKickMessage(ChatColor.valueOf(GlobalConf.get("tempBanColor").asString())
						+ GlobalConf.get("tempBanMessage").asString()
								.replace("%time", tempBanMins.toString())
								.replace("%plural", Util.getPlural(tempBanMins, true))
								.replace("%reason", bd.getReason()));
				return;
			}
		}
	}
	
	/**
	 * Monitors for when Permissions is initialized.
	 * 
	 * @param event
	 *            The PluginEnableEvent created by Bukkit.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(@SuppressWarnings("unused") final PluginEnableEvent event) {
		if (ColeBansPlugin.instance.permissionsHandler == null) {
			final Plugin permissions = ColeBansPlugin.instance.getServer().getPluginManager()
					.getPlugin("Permissions");
			if (permissions != null) {
				if (permissions.isEnabled()
						&& permissions.getClass().getName()
								.equals("com.nijikokun.bukkit.Permissions.Permissions")) {
					ColeBansPlugin.instance.permissionsHandler = ((Permissions) permissions).getHandler();
					System.out.println(ColeBansPlugin.PREFIX + "Hooked into Nijikokun-like permissions.");
				}
			}
		}
	}
}
