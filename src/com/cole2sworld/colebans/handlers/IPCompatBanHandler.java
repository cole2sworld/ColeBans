package com.cole2sworld.colebans.handlers;

import java.net.InetAddress;

import com.cole2sworld.colebans.IPLogManager;

public abstract class IPCompatBanHandler extends BanHandler {
	/**
	 * Ban an IP address.
	 * 
	 * @param ip
	 *            IP to ban.
	 */
	public abstract void banIP(InetAddress ip);
	
	/**
	 * Ban an IP address by the player's name. <i>Note: This method is
	 * implemented in IPCompatBanHandler to make a simple wrapper to
	 * banIP(InetAddress) using IPLogManager.</i>
	 * 
	 * @param player
	 *            Player to ban
	 * @throws IllegalArgumentException
	 *             If the player has not been logged before
	 */
	public void banIP(final String player) {
		final InetAddress addr = IPLogManager.lookupByName(player);
		if (addr == null)
			throw new IllegalArgumentException("That player hasn't been logged before!");
		this.banIP(addr);
	}
	
	/**
	 * Get ban data for the given IP. The 'NAME' value for the BanData will be
	 * either a looked up player name or the InetAddress converted to a string.
	 * The BanData returned will always have a custom InetAddress value called
	 * 'ip' that contains the passed IP.
	 * 
	 * @param ip
	 *            IP to look up
	 * @return Relevant BanData
	 */
	public abstract BanData getBanDataForIP(InetAddress ip);
	
	/**
	 * Unban an IP address.
	 * 
	 * @param ip
	 *            IP to ban.
	 */
	public abstract void unbanIP(InetAddress ip);
	
	/**
	 * Unban an IP address by the player's name. <i>Note: This method is
	 * implemented in IPCompatBanHandler to make a simple wrapper to
	 * unbanIP(InetAddress) using IPLogManager.</i>
	 * 
	 * @param player
	 *            Player to unban
	 * @throws IllegalArgumentException
	 *             If the player has not been logged before
	 */
	public void unbanIP(final String player) {
		final InetAddress addr = IPLogManager.lookupByName(player);
		if (addr == null)
			throw new IllegalArgumentException("That player hasn't been logged before!");
		this.unbanIP(addr);
	}
}
