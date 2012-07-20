package com.cole2sworld.colebans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map.Entry;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;

import com.cole2sworld.colebans.framework.GlobalConf;

/**
 * Manager for the iplog.yml
 * 
 */
public final class IPLogManager implements Listener {
	/**
	 * The IP log configuration.
	 */
	public static YamlConfiguration	ipLog		= new YamlConfiguration();
	/**
	 * The IP log file.
	 */
	public static File				logFile;
	/**
	 * Is the IP log initalized?
	 */
	public static boolean			initalized	= false;
	
	/**
	 * Initalize the IP log.
	 */
	public static void initalize() {
		if (initalized) return;
		if (!GlobalConf.get("allowIPLog").asBoolean()) {
			System.out.println(ColeBansPlugin.PREFIX
					+ "[IPLogManager] Skipping initalization as IP logging is disabled.");
			return;
		}
		System.out.println(ColeBansPlugin.PREFIX + "[IPLogManager] Initalizing...");
		final long oldTime = System.currentTimeMillis();
		final File dataFolder = ColeBansPlugin.instance.getDataFolder();
		dataFolder.mkdirs();
		logFile = new File(dataFolder.getPath() + "/iplog.yml");
		try {
			logFile.createNewFile();
		} catch (final IOException e) {
			ColeBansPlugin.LOG.severe((ColeBansPlugin.PREFIX + "[IPLogManager] Error creating IP log file! (" + e
					.getMessage()) != null ? e.getMessage() : "??" + ")");
		}
		ipLog = new YamlConfiguration();
		try {
			ipLog.load(logFile);
		} catch (final FileNotFoundException e) {
			ColeBansPlugin.LOG.severe(ColeBansPlugin.PREFIX + "[IPLogManager] IP log file not found!");
		} catch (final IOException e) {
			ColeBansPlugin.LOG.severe((ColeBansPlugin.PREFIX + "[IPLogManager] Error reading IP log file! (" + e
					.getMessage()) != null ? e.getMessage() : "??" + ")");
		} catch (final InvalidConfigurationException e) {
			ColeBansPlugin.LOG.severe(ColeBansPlugin.PREFIX + "[IPLogManager] IP log file is invalid!");
		}
		System.out.println(ColeBansPlugin.PREFIX + "[IPLogManager] Done. Took "
				+ (System.currentTimeMillis() - oldTime) + " ms.");
		initalized = true;
	}
	
	/**
	 * Look up a player's name.
	 * 
	 * @param ip
	 *            The IP to look up.
	 * @return Null if the player has not been logged before, or a String if
	 *         they have been logged.
	 */
	public static String lookupByIP(final InetAddress ip) {
		if (!GlobalConf.get("allowIPLog").asBoolean()) return null;
		initalize();
		final String newIP = ip.getHostAddress();
		for (final Entry<String, Object> entry : ipLog.getValues(true).entrySet()) {
			if (entry.getValue().equals(newIP)) return entry.getKey();
		}
		return null;
	}
	
	/**
	 * Lookup a player's IP address.
	 * 
	 * @param name
	 *            The player name to look up.
	 * @return Null if the player has not been logged before, or an InetAddress
	 *         if they have been logged.
	 */
	public static InetAddress lookupByName(final String name) {
		if (!GlobalConf.get("allowIPLog").asBoolean()) return null;
		initalize();
		try {
			return ipLog.get(name) == null ? null : InetAddress.getByAddress(Util.processIp(ipLog
					.getString(name)));
		} catch (final UnknownHostException e) {
			ipLog.set(name, null);
			try {
				ipLog.save(logFile);
			} catch (final IOException e1) {
				ColeBansPlugin.LOG.severe((ColeBansPlugin.PREFIX + "[IPLogManager] Error saving IP log file! (" + e1
						.getMessage()) == null ? "??" : e1.getMessage() + ")");
			}
			return null;
		}
	}
	
	public static void save() {
		if (!GlobalConf.get("allowIPLog").asBoolean()) return;
		initalize();
		try {
			ipLog.save(logFile);
		} catch (final IOException e) {
			ColeBansPlugin.LOG.severe((ColeBansPlugin.PREFIX + "[IPLogManager] Error saving IP log file! (" + e
					.getMessage()) == null ? "??" : e.getMessage() + ")");
		}
	}
	
	protected IPLogManager() {
	}
	
	@SuppressWarnings("static-method")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPreLogin(final PlayerPreLoginEvent event) {
		ipLog.set(event.getName(), event.getAddress().getHostAddress());
		try {
			ipLog.save(logFile);
		} catch (final IOException e) {
			ColeBansPlugin.LOG.severe((ColeBansPlugin.PREFIX + "[IPLogManager] Error saving IP log file! (" + e
					.getMessage()) == null ? "" : e.getMessage() + ")");
		}
	}
}
