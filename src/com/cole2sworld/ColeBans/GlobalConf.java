package com.cole2sworld.ColeBans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public class GlobalConf {
	public static FileConfiguration conf;
	public static ConfigurationSection settings;
	public static boolean allowTempBans = true;
	public static Material banHammer = Material.BLAZE_ROD;
	public static String banMessage = "You are banned for %reason!";
	public static String tempBanMessage = "You are tempbanned! %time seconds remaining!";
	public static boolean allowBanhammer = true;
	public static boolean fancyEffects = true;
	public static String banColor = "DARK_RED";
	public static String kickColor = "YELLOW";
	public static String tempBanColor = "RED";
	public static boolean announceBansAndKicks = true;
	public static boolean mcbansNodes = false;
	public static String logPrefix = "[ColeBans] ";
	public static String banHandlerConf = "MySQL";
	public static class sql {
		public static ConfigurationSection section;
		public static String user = "minecraft";
		public static String pass = "password";
		public static String host = "localhost";
		public static String port = "3306";
		public static String db = "minecraft";
		public static String prefix = "cb_";
	}
	public static class mcbans {
		public static ConfigurationSection section;
		public static String apiKey = "yourAPIKeyHere";
		public static boolean fullBackups = false;
	}
	public static class yaml {
		public static ConfigurationSection section;
		public static String file;
	}
	public static class json {
		public static ConfigurationSection section;
		public static String file;
	}
	public static class advanced {
		public static ConfigurationSection section;
		public static String pkg;
	}
	public static void loadConfig() {
		File confFile = new File("./plugins/ColeBans/config.yml");
		try {
			if (confFile.exists()) {
				conf.load(confFile);
				settings = conf.getConfigurationSection("settings");
				allowTempBans = settings.getBoolean("allowTempBans");
				banHammer = Material.getMaterial(settings.getString("banHammer"));
				banMessage = settings.getString("banMessage");
				tempBanMessage = settings.getString("tempBanMessage");
				allowBanhammer = settings.getBoolean("allowBanhammer");
				fancyEffects = settings.getBoolean("fancyEffects");
				banColor = settings.getString("banColor");
				kickColor = settings.getString("kickColor");
				tempBanColor = settings.getString("tempBanColor");
				announceBansAndKicks = settings.getBoolean("announceBansAndKicks");
				mcbansNodes = settings.getBoolean("mcbansNodes");
				logPrefix = settings.getString("logPrefix")+" ";
				sql.section = settings.getConfigurationSection("mysql");
				sql.user = sql.section.getString("user");
				sql.pass = sql.section.getString("pass");
				sql.host = sql.section.getString("host");
				sql.port = sql.section.getString("port");
				sql.db = sql.section.getString("db");
				sql.prefix = sql.section.getString("prefix");
				mcbans.section = settings.getConfigurationSection("mcbans");
				mcbans.apiKey = mcbans.section.getString("apiKey");
				mcbans.fullBackups = mcbans.section.getBoolean("fullBackups");
			}
			else {
				File dir = new File("./plugins/ColeBans");
				dir.mkdir();
				confFile.createNewFile();
				if (confFile.canWrite()) {
					System.out.println("[ColeBans] No config file exists, generating.");
					FileOutputStream fos = new FileOutputStream(confFile);
					String defaultConfig = "settings:\n"+
							"    banHammer: BLAZE_ROD\n"+
							"    allowBanhammer: true\n"+
							"    allowTempBans: true\n"+
							"    # In the banMessage, %reason is replaced with the reason."+
							"    banMessage: You are banned for %reason!\n"+
							"    # In the tempBanMessage, %time is replaced with the amount of time (in minutes) left for the ban, and %plural turns into an S if the time is > 1."+
							"    tempBanMessage: You are tempbanned! %time minute%plural remaining!\n"+
							"    fancyEffects: true\n"+
							"    banColor: DARK_RED\n"+
							"    kickColor: YELLOW\n"+
							"    tempBanColor: RED\n"+
							"    announceBansAndKicks: true\n"+
							"    mcbansNodes: true\n"+
							"    logPrefix: [ColeBans]\n"+
							"    #banHandler can be MySQL, MCBans, YAML, or JSON.\n"+
							"    banHandler: MySQL\n"+
							"    mysql:\n"+
							"        user: root\n"+
							"        pass: pass\n"+
							"        host: localhost\n"+
							"        port: 3306\n"+
							"        db: minecraft\n"+
							"        prefix: cb_\n"+
							"    mcbans:\n"+
							"        ###### THIS LINE IS VERY VERY IMPORTANT IF YOU CHOSE MCBANS FOR THE BAN HANDLER ######\n"+
							"        apiKey: yourAPIKeyHere\n"+
							"        # Turn on jsonBackup to backup your MCBans bans to a JSON banlist occasionally (frequency depends on size of your banlist)\n"+
							"        jsonBackup: true\n"+
							"        # Full backups include reasons for bans, but they are much slower. False for simple backup (no reasons) or true for full backup.\n"+
							"        fullBackups: false\n"+
							"    yaml:\n"+
							"        fileName: banlist.yml\n"+
							"    json:\n"+
							"        # If you are using MCBans and have jsonBackup on, this is your backup banlist name as well\n"+
							"        fileName: banlist.json\n"+
							"    advanced:\n" +
							"        # The package is where to get the ban handlers. Only change this line if you know what you are doing.\n" +
							"        package: com.cole2sworld.ColeBans.handlers";
					fos.write(defaultConfig.getBytes("utf-8"));
					loadConfig();
					return;
				}
				else {
					Logger.getLogger("Minecraft").severe("[ColeBans] COULD NOT LOAD WORKING CONFIG FILE. Aborting operation.");
					Main.instance.onFatal();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}
