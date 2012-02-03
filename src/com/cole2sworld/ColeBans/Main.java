package com.cole2sworld.ColeBans;

import java.util.Vector;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cole2sworld.ColeBans.commands.CBCommand;
import com.cole2sworld.ColeBans.commands.CommandHandler;
import com.cole2sworld.ColeBans.framework.PlayerOfflineException;
import com.cole2sworld.ColeBans.handlers.BanHandler;
import com.cole2sworld.ColeBans.handlers.MySQLBanHandler;
import com.nijiko.permissions.PermissionHandler;


public class Main extends JavaPlugin {
    public PermissionHandler permissionsHandler = null;
	public static Main instance;
	public static Server server;
	public static BanHandler banHandler;

	public Main() {
		instance = this;
	}
	
	@Override
	public void onDisable() {
		banHandler.onDisable();
		System.out.println(GlobalConf.logPrefix+"Disabled.");
	}

	@Override
	public void onEnable() {
		System.out.println(GlobalConf.logPrefix+"Initalizing...");
		server = getServer();
		PluginManager pm = server.getPluginManager();
		System.out.println(GlobalConf.logPrefix+"Loading config & ban handler...");
		long oldtime = System.currentTimeMillis();
		GlobalConf.conf = getConfig();
		GlobalConf.loadConfig();
		banHandler = new MySQLBanHandler(GlobalConf.sql.user, GlobalConf.sql.pass, GlobalConf.sql.host, GlobalConf.sql.port, GlobalConf.logPrefix, GlobalConf.sql.db);
		long newtime = System.currentTimeMillis();
		System.out.println(GlobalConf.logPrefix+"Done. Took "+(newtime-oldtime)+" ms.");
		System.out.println(GlobalConf.logPrefix+"Registering events...");
		oldtime = System.currentTimeMillis();
		final long oldTimePerms = System.currentTimeMillis();
		EventRegistar.register(pm, oldTimePerms);
		newtime = System.currentTimeMillis();
		System.out.println(GlobalConf.logPrefix+"Done. Took "+(newtime-oldtime)+" ms.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if (cmdLabel.equalsIgnoreCase("cb")) {
			if (args.length == 0) return false;
			else {
				try {
					String cmdName = args[0].substring(1);
					Character firstChar = args[0].charAt(1);
					cmdName = Character.toUpperCase(firstChar)+cmdName.toLowerCase();
					Object rawObject = Class.forName("com.cole2sworld.ColeBans.commands."+cmdName).newInstance();
					if (rawObject instanceof CBCommand) {
						CBCommand cmdObj = (CBCommand) rawObject;
						Vector<String> newArgs = new Vector<String>(args.length);
						for (int i = 1; i<args.length; i++) {
							newArgs.add(args[i]);
						}
						String error = cmdObj.run((String[]) newArgs.toArray(), sender);
						if (error != null) {
							sender.sendMessage(error);
						}
						return true;
					}
				}
				catch (ClassNotFoundException e) {
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {}
			}
		}
		else {
			CommandHandler.onCommand(sender, cmd, cmdLabel, args);
		}
		sender.sendMessage(ChatColor.RED+"Invalid sub-command.");
		return true;
	}
	
	public void kickPlayer(String player, String reason) throws PlayerOfflineException {
		Player playerObj = server.getPlayer(player);
		if (playerObj != null) {
			if (GlobalConf.fancyEffects) {
				World world = playerObj.getWorld();
				world.playEffect(playerObj.getLocation(), Effect.SMOKE, 1);
				world.playEffect(playerObj.getLocation(), Effect.SMOKE, 2);
				world.playEffect(playerObj.getLocation(), Effect.SMOKE, 3);
				world.playEffect(playerObj.getLocation(), Effect.SMOKE, 4);
				world.playEffect(playerObj.getLocation(), Effect.SMOKE, 5);
				world.playEffect(playerObj.getLocation(), Effect.SMOKE, 6);
			}
			playerObj.kickPlayer(ChatColor.valueOf(GlobalConf.kickColor)+"KICKED: "+reason);
			if (GlobalConf.announceBansAndKicks) server.broadcastMessage(ChatColor.valueOf(GlobalConf.kickColor)+player+" was kicked! ["+reason+"]");
		}
		else throw new PlayerOfflineException(player+" is offline!");
	}
	public static String getPlural(long check) {
		if (check < 0) return "s";
		else if (check == 0) return "s";
		else if (check > 1) return "s";
		else return "";
	}
    public boolean hasPermission(Player player, String permissionNode)
    {
    	if (permissionsHandler == null) return player.isOp();
        return permissionsHandler.has(player, permissionNode);
    }

	public void onFatal() {
		this.onDisable();
		try {
			this.finalize();
		} catch (Throwable e) {}
		this.setEnabled(false);
	}

}
