package com.cole2sworld.ColeBans;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cole2sworld.ColeBans.EventListener;
import com.cole2sworld.ColeBans.commands.CBCommand;
import com.cole2sworld.ColeBans.commands.CommandHandler;
import com.cole2sworld.ColeBans.framework.GlobalConf;
import com.cole2sworld.ColeBans.framework.PlayerOfflineException;
import com.cole2sworld.ColeBans.handlers.BanHandler;
import com.nijiko.permissions.PermissionHandler;

/**
 * The main class for ColeBans.
 * @since v1 Apricot
 */
public final class Main extends JavaPlugin {
	/**
	 * Are we in debug mode? (If this is turned on when compiled, it means the build is a debug build)
	 */
	public static boolean debug = false;
	/**
	 * The Minecraft log.
	 */
	public static final Logger LOG = Logger.getLogger("Minecraft");
	public static final String PREFIX = "[ColeBans] ";
	/**
	 * The Permissions 3/2 (or bridge) that we will use for permissions.
	 */
	public PermissionHandler permissionsHandler = null;
	/**
	 * The instance of Main, for accessing non-static methods.
	 */
	public static Main instance;
	/**
	 * The server that ColeBans got on startup.
	 */
	public Server server;
	/**
	 * The banhandler that will be used for all actions.
	 */
	public BanHandler banHandler;

	/**
	 * Creates a new ColeBans Main class.
	 * <i>Do not use. Only the Bukkit server implementation should instantiate Main. If you need an instance of Main, use Main.instance</i>
	 */

	public Main() {
		super();
		instance = this;
	}

	/**
	 * Called when the plugin is disabled.
	 */
	@Override
	public void onDisable() {
		if (banHandler != null)
			banHandler.onDisable();
		GlobalConf.save();
		System.out.println(PREFIX+"Disabled.");
	}

	/**
	 * Registers events, gets the config, pulls the banhandler, and all that good stuff you need to do when initializing.
	 */
	@Override
	public void onEnable() {
		try {
			System.out.println(PREFIX+"Initalizing...");
			
			server = getServer();
			PluginManager pm = server.getPluginManager();
			System.out.println(PREFIX+"Loading config and ban handler...");
			long oldtime = System.currentTimeMillis();
			GlobalConf.load();
			if (debug) LOG.warning(PREFIX+"Using a debug build. Expect many messages");
			try {
				banHandler = Util.lookupHandler(GlobalConf.get("banHandler").asString());
			} catch (ClassNotFoundException e) {
				onFatal("Non-existant ban handler given in config file");
			} catch (SecurityException e) {
				onFatal("Somehow, a SecurityException occurred. Plugin conflict?");
			} catch (NoSuchMethodException e) {
				onFatal("Bad ban handler given in config file!");
			} catch (IllegalArgumentException e) {
				onFatal("Bad ban handler given in config file!");
			} catch (IllegalAccessException e) {
				onFatal("Bad ban handler given in config file!");
			} catch (InvocationTargetException e) {
				onFatal("Bad ban handler given in config file!");
			} catch (NullPointerException e) {
				onFatal("Bad ban handler given in config file!");
			} catch (ClassCastException e) {
				onFatal("Bad ban handler given in config file!");
			}
			long newtime = System.currentTimeMillis();
			System.out.println(PREFIX+"Done. Took "+(newtime-oldtime)+" ms.");
			System.out.println(PREFIX+"Registering events...");
			oldtime = System.currentTimeMillis();
			pm.registerEvents(new EventListener(), this);
			pm.registerEvents(new BanhammerListener(), this);
			pm.registerEvents(new RestrictionListener(), this);
			newtime = System.currentTimeMillis();
			System.out.println(PREFIX+"Done. Took "+(newtime-oldtime)+" ms.");
		}
		catch (Exception e) {
			LOG.severe(PREFIX+"Aborting operation: "+e.getMessage());
			setEnabled(false);
		}
	}

	/**
	 * Manages the dynamic command handler and the static command handler.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		debug("Executing command "+cmdLabel);
		if (cmdLabel.equalsIgnoreCase("cb") || cmdLabel.equalsIgnoreCase("colebans")) {
			debug("It's /cb");
			if (args.length < 1) return false;
			try {
				String cmdName = args[0].substring(1);
				debug("cmdName = "+cmdName);
				Character firstChar = args[0].charAt(0);
				debug("firstChar = "+firstChar);
				cmdName = Character.toUpperCase(firstChar)+cmdName.toLowerCase(Locale.ENGLISH);
				debug("cmdName = "+cmdName);
				Object rawObject = Class.forName("com.cole2sworld.ColeBans.commands."+cmdName).newInstance();
				debug("rawObject = "+rawObject.getClass().getSimpleName());
				if (rawObject instanceof CBCommand) {
					debug("rawObject is a CBCommand");
					CBCommand cmdObj = (CBCommand) rawObject;
					Vector<String> newArgs = new Vector<String>(args.length);
					for (int i = 1; i<args.length; i++) {
						newArgs.add(args[i]);
					}
					String error = cmdObj.run(newArgs.toArray(new String[newArgs.size()]), sender);
					if (error != null) {
						sender.sendMessage(error);
					}
					return true;
				}
			}
			catch (ClassNotFoundException e) {
				debug("ClassNotFoundException (invalid subcommand)");
			} catch (InstantiationException e) {
				debug("InstantiationException (???)");
			} catch (IllegalAccessException e) {
				debug("IllegalAccessException (non-public class)");
			} catch (Exception e) {
				return true;
			}
		}
		else {
			debug("Requires static handling. Passing to CommandHandler");
			return CommandHandler.onCommand(sender, cmd, cmdLabel, args);
		}
		sender.sendMessage(ChatColor.RED+"Invalid sub-command.");
		return true;
	}

	/**
	 * Kicks a player out of the game, with a fancy effect if enabled.
	 * @param player The player to kick (name)
	 * @param reason The reason for the kick (shown to the victim)
	 * @throws PlayerOfflineException If the player is offline
	 */
	public void kickPlayer(String player, String reason) throws PlayerOfflineException {
		Player playerObj = server.getPlayer(player);
		if (playerObj != null && playerObj.isOnline()) {
			if (GlobalConf.get("fancyEffects").asBoolean()) {
				World world = playerObj.getWorld();
				world.playEffect(playerObj.getLocation(), Effect.SMOKE, 1);
				world.playEffect(playerObj.getLocation(), Effect.SMOKE, 2);
				world.playEffect(playerObj.getLocation(), Effect.SMOKE, 3);
				world.playEffect(playerObj.getLocation(), Effect.SMOKE, 4);
				world.playEffect(playerObj.getLocation(), Effect.SMOKE, 5);
				world.playEffect(playerObj.getLocation(), Effect.SMOKE, 6);
			}
			if (reason != null) {
				playerObj.kickPlayer(ChatColor.valueOf(GlobalConf.get("kickColor").asString())+"KICKED: "+reason);
			} else {
				playerObj.kickPlayer(ChatColor.valueOf(GlobalConf.get("kickColor").asString())+"Kicked!");
			}
			if (GlobalConf.get("announceBansAndKicks").asBoolean() && reason != null) {
				server.broadcastMessage(ChatColor.valueOf(GlobalConf.get("kickColor").asString())+player+" was kicked! ["+reason+"]");
			} else if (GlobalConf.get("announceBansAndKicks").asBoolean()) {
				server.broadcastMessage(ChatColor.valueOf(GlobalConf.get("kickColor").asString())+player+" was kicked!");
			}
		}
		else throw new PlayerOfflineException(player+" is offline!");
	}
	/**
	 * @param player Player to check (name)
	 * @param permissionNode Node to check
	 * @return If there is a permissionsHandler, whether or not the given player has the node. If there isn't, if the player is an operator.
	 */
	public boolean hasPermission(Player player, String permissionNode)
	{
		if (permissionsHandler == null) return player.hasPermission(new Permission(permissionNode)) || player.isOp();
		return permissionsHandler.has(player, permissionNode);
	}

	/**
	 * Called when something really bad happens.
	 */
	protected void onFatal(String reason) throws Exception {
		throw new Exception(reason);
	}

	public static Map<String, String> getBanHandlerInitArgs() {
		HashMap<String, String> data = new HashMap<String, String>(15);
		data.put("username", GlobalConf.get("mysql.user").asString());
		data.put("password", GlobalConf.get("mysql.pass").asString());
		data.put("host", GlobalConf.get("mysql.host").asString());
		data.put("prefix", GlobalConf.get("mysql.prefix").asString());
		data.put("db", GlobalConf.get("mysql.db").asString());
		data.put("yaml", GlobalConf.get("yaml.fileName").asString());
		data.put("apiKey", GlobalConf.get("mcbans.apiKey").asString());
		return data;
	}

	public static final void debug(String msg) {
		if (debug) {
			String caller = "null";
			try {
				throw new Exception("Getting caller");
			} catch(Exception e) {
				try {
					caller = Class.forName(e.getStackTrace()[1].getClassName()).getSimpleName();
				} catch (ClassNotFoundException e1) {
					//we don't really care
				}
			}
			System.out.println(PREFIX+"[DEBUG] ["+caller+"] "+msg);
		}
	}

}
