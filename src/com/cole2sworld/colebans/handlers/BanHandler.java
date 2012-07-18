package com.cole2sworld.colebans.handlers;

import java.util.Locale;
import java.util.Vector;

import javax.naming.OperationNotSupportedException;

import com.cole2sworld.colebans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.colebans.framework.PlayerNotBannedException;

/**
 * Handles banning and unbanning of players.
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
	}
	
	/**
	 * The preferred name for the admin when an action is initiated without
	 * player intervention
	 */
	public static final String	SYSTEM_ADMIN_NAME	= "[System]";
	
	/**
	 * Do stuff related to getting ready, and then return a new instance of the
	 * BanHandler.
	 * 
	 * @return This ban handler, after proper initialization.
	 * @throws OperationNotSupportedException
	 *             if the ban handler does not properly override this method
	 */
	public static BanHandler onEnable() throws OperationNotSupportedException {
		throw new OperationNotSupportedException("BanHandler is abstract");
	}
	
	/**
	 * Permanently bans a player.
	 * 
	 * @param player
	 *            The player to ban.
	 * @param reason
	 *            The ban reason
	 * @param admin
	 *            The admin that initiated this action
	 * @throws PlayerAlreadyBannedException
	 *             if the player is already banned
	 */
	public abstract void banPlayer(String player, String reason, String admin)
			throws PlayerAlreadyBannedException;
	
	/**
	 * Convert over to a new ban handler.
	 * 
	 * @param handler
	 *            The handler to dump data into.
	 */
	public abstract void convert(BanHandler handler);
	
	/**
	 * Count how many bans are in this handler.
	 * 
	 * @return The amount of bans.
	 */
	public abstract long countBans(String admin);
	
	/**
	 * Does a full dump of the data for this ban handler.
	 */
	public abstract Vector<BanData> dump(String admin);
	
	/**
	 * @param player
	 *            The name of the player to check
	 * @param admin
	 *            The admin that initiated this action
	 * @return BanData object containing details about the ban.
	 */
	public abstract BanData getBanData(String player, String admin);
	
	/**
	 * Returns the name of this class, minus "BanHandler". For use with
	 * detecting what ban handler is in use.
	 * 
	 * @return Truncated name of this class. MySQLBanHandler becomes 'mysql'
	 */
	public final String getTruncatedName() {
		return this.getClass().getSimpleName().replace("BanHandler", "")
				.toLowerCase(Locale.ENGLISH);
	}
	
	/**
	 * Gets whether a player is banned.
	 * 
	 * @param player
	 *            The player to check
	 * @param admin
	 *            The admin that initiated this action
	 * @return If the player is banned (whether it be temp or permanent)
	 */
	public abstract boolean isPlayerBanned(String player, String admin);
	
	/**
	 * Gets a simple list of the banned players, with no reasons.
	 */
	public abstract Vector<String> listBannedPlayers(String admin);
	
	/**
	 * Do stuff needed when disabling. Closing SQL connections, flushing caches,
	 * etc.
	 */
	public abstract void onDisable();
	
	/**
	 * Temporarily bans a player.
	 * 
	 * @param player
	 *            The player to ban
	 * @param time
	 *            Amount of time to stay banned, in minutes.
	 * @param reason
	 *            The reason for the tempban
	 * @param admin
	 *            The admin that initiated this action
	 * @throws PlayerAlreadyBannedException
	 *             if the player is already banned
	 * @throws UnsupportedOperationException
	 *             if temp bans are disabled
	 */
	public abstract void tempBanPlayer(String player, long time, String reason, String admin)
			throws PlayerAlreadyBannedException, UnsupportedOperationException;
	
	/**
	 * Unbans a player, whether they have been temp banned or perm banned
	 * 
	 * @param player
	 *            The player to unban
	 * @param admin
	 *            The admin that initiated this action
	 * @throws PlayerNotBannedException
	 *             If the player is not banned
	 */
	public abstract void unbanPlayer(String player, String admin) throws PlayerNotBannedException;
	
}
