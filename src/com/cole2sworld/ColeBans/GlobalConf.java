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
	/**
	 * Configuration we are using.
	 */
	public static FileConfiguration conf;
	/**
	 * The main ConfigurationSection
	 */
	public static ConfigurationSection settings;
	/**
	 * Do we allow tempbans to be made?
	 */
	public static boolean allowTempBans = true;
	/**
	 * Unused.
	 */
	public static Material banHammer = Material.BLAZE_ROD;
	/**
	 * Message when somebody tries to log in but is banned.
	 */
	public static String banMessage = "You are banned for %reason!";
	/**
	 * Message when somebody tries to log in but is tempbanned.
	 */
	public static String tempBanMessage = "You are tempbanned! %time seconds remaining!";
	/**
	 * Unused.
	 */
	public static boolean allowBanhammer = true;
	/**
	 * Show fancy effects?
	 */
	public static boolean fancyEffects = true;
	/**
	 * Color of the disconnect message when banned.
	 */
	public static String banColor = "DARK_RED";
	/**
	 * Color of the disconnect message when kicked.
	 */
	public static String kickColor = "YELLOW";
	/**
	 * Color of the disconnect message when tempbanned.
	 */
	public static String tempBanColor = "RED";
	/**
	 * Announce when somebody is banned/kicked to the entire server?
	 */
	public static boolean announceBansAndKicks = true;
	/**
	 * Prefix to use in the console.
	 */
	public static String logPrefix = "[ColeBans] ";
	/**
	 * Which banhandler?
	 */
	public static String banHandlerConf = "MySQL";
	/**
	 * Configuration section for SQL.
	 *
	 */
	public static class sql {
		/**
		 * The raw section.
		 */
		public static ConfigurationSection section;
		/**
		 * Username for the database.
		 */
		public static String user = "minecraft";
		/**
		 * Password for the database.
		 */
		public static String pass = "password";
		/**
		 * Host for the database.
		 */
		public static String host = "localhost";
		/**
		 * Port for the database.
		 */
		public static String port = "3306";
		/**
		 * Database name.
		 */
		public static String db = "minecraft";
		/**
		 * Table prefix.
		 */
		public static String prefix = "cb_";
	}
	/**
	 * Configuration section for MCBans.
	 *
	 */
	public static class mcbans {
		/**
		 * The raw section.
		 */
		public static ConfigurationSection section;
		/**
		 * The MCBans API key.
		 */
		public static String apiKey = "yourAPIKeyHere";
		/**
		 * Whether or not to make full dumps of the banlist, including reasons.
		 */
		public static boolean fullBackups = false;
	}
	/**
	 * Configuration section for YAML.
	 *
	 */
	public static class yaml {
		/**
		 * The raw section.
		 */
		public static ConfigurationSection section;
		/**
		 * The file to use.
		 */
		public static String file;
	}
	/**
	 * Configuration section for YAML.
	 *
	 */
	public static class json {
		/**
		 * The raw section.
		 */
		public static ConfigurationSection section;
		/**
		 * The file to use.
		 */
		public static String file;
	}
	/**
	 * Stuff the user shouldn't touch unless they know what they are doing.
	 * @author cole2
	 *
	 */
	public static class advanced {
		/**
		 * The raw section.
		 */
		public static ConfigurationSection section;
		/**
		 * Which package do we get the banhandlers?
		 */
		public static String pkg;
	}
	/**
	 * Loads up the config from disk, or creates it if it does not exist.
	 */
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
					String defaultConfig = ""+
							"# For information on how to configure ColeBans, go to http://c2wr.com/cbconf"+
							"settings:\n"+
							"    allowTempBans: true\n"+
							"    banMessage: You are banned for %reason!\n"+
							"    tempBanMessage: You are tempbanned! %time minute%plural remaining!\n"+
							"    fancyEffects: true\n"+
							"    banColor: DARK_RED\n"+
							"    kickColor: YELLOW\n"+
							"    tempBanColor: RED\n"+
							"    announceBansAndKicks: true\n"+
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
							"        # Set this to the BanHandler you want to use for the backups, or \"None\" to turn off backups.\n"+
							"        backup: true\n"+
							"        fullBackups: false\n"+
							"    yaml:\n"+
							"        fileName: banlist.yml\n"+
							"    json:\n"+
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
