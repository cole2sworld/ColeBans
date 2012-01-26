package com.cole2sworld.ColeBans;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;

import com.cole2sworld.ColeBans.handlers.BanData;
import com.cole2sworld.ColeBans.handlers.BanHandler.Type;

public class CBPlayerListener extends PlayerListener {
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		BanData bd = Main.banHandler.getBanData(event.getName());
		Type banType = bd.getType();
		String player = event.getName();
		if (banType == Type.PERMANENT) {
			String banReason = bd.getReason();
			event.setResult(Result.KICK_BANNED);
			event.setKickMessage(ChatColor.valueOf(Main.banColor)+Main.banMessage.replace("%reason", banReason));
			return;
		}
		if (Main.allowTempBans) {
			Long tempBanTime = Main.banHandler.getBanData(player).getTime();
			if (tempBanTime > -1) {
				Long tempBanMins = tempBanTime-System.currentTimeMillis();
				tempBanMins /= 1000;
				tempBanMins /= 60;
				event.setResult(Result.KICK_BANNED);
				event.setKickMessage(ChatColor.valueOf(Main.tempBanColor)+Main.tempBanMessage.replace("%time", tempBanMins.toString()).replace("%plural", Main.getPlural(tempBanMins)));
				return;
			}
		}
		event.setResult(Result.ALLOWED);
	}
}
