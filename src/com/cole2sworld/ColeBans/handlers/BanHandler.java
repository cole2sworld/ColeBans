package com.cole2sworld.ColeBans.handlers;

import java.io.File;

import org.bukkit.ChatColor;

import com.cole2sworld.ColeBans.Main;
import com.cole2sworld.ColeBans.framework.MethodNotSupportedException;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.ColeBans.framework.PlayerNotBannedException;
/**
 * Abstract class for ban handlers. To make a custom ban handler, simply extend this class and you can make a custom ban handler.
 * @author cole2
 */
public abstract class BanHandler {
	/**
	 * Types of bans
	 *
	 */
	public static enum Type {
		/**
		 * Represents a temporary ban.
		 */
		TEMPORARY,
		/**
		 * Represents a permanent ban.
		 */
		PERMANENT,
		/**
		 * Represents a player not being banned.
		 */
		NOT_BANNED
	};
	public abstract BanHandler onEnable(String username, String password, String host, String port, String prefix, String db, File yaml, File json, String api);
	/**
	 * Permanently bans a player.
	 * @param player The player to ban.
	 * @param reason The ban reason
	 * @throws PlayerAlreadyBannedException if the player is already banned
	 */
	public abstract void banPlayer(String player, String reason) throws PlayerAlreadyBannedException;
	/**
	 * Temporarily bans a player.
	 * @param player The player to ban
	 * @param time Amount of time to stay banned, in minutes.
	 * @throws PlayerAlreadyBannedException if the player is already banned
	 * @throws MethodNotSupportedException if temp bans are disabled
	 */
	public abstract void tempBanPlayer(String player, long time) throws PlayerAlreadyBannedException, MethodNotSupportedException;
	/**
	 * Unbans a player, whether they have been temp banned or perm banned
	 * @param player The player to unban
	 * @throws PlayerNotBannedException If the player is not banned
	 */
	public abstract void unbanPlayer(String player) throws PlayerNotBannedException;
	/**
	 * Gets whether a player is banned.
	 * @param player The player to check
	 * @return If the player is banned (whether it be temp or permanent)
	 * @see BanHandler#getBanData(String)
	 */
	public abstract boolean isPlayerBanned(String player);
	/**
	 * @param player The name of the player to check
	 * @return BanData object containing details about the ban.
	 */
	public abstract BanData getBanData(String player);
	/**
	 * Gets the formatted ban reason for "reason"
	 * @param banReason The reason for the ban
	 * @param banType The type of ban
	 * @param tempBanTime The amount of time in minutes until the ban expires
	 * @return The fancy formatted ban reason, with colors and everything
	 */
	public static String getFormattedBanReason(String banReason, Type banType, Long tempBanTime) {
		if (banType == Type.PERMANENT) {
			return ChatColor.valueOf(Main.banColor)+Main.banMessage.replace("%reason", banReason).replace("%time", "infinite");
		}
		else if (banType == Type.TEMPORARY) {
			return ChatColor.valueOf(Main.tempBanColor)+Main.tempBanMessage.replace("%reason", "Temporary Ban").replace("%time", tempBanTime.toString());
		}
		return "";
	}
	/**
	 * Do stuff needed when disabling. Closing SQL connections, flushing caches, etc.
	 */
	public abstract void onDisable();
	/**
	 * Convert over to a new ban handler.
	 * @param handler The handler to dump data into.
	 */
	public abstract void convert(BanHandler handler);
}