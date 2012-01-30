package com.cole2sworld.ColeBans;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cole2sworld.ColeBans.framework.MethodNotSupportedException;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.ColeBans.framework.PlayerNotBannedException;
import com.cole2sworld.ColeBans.framework.PlayerOfflineException;
import com.cole2sworld.ColeBans.handlers.BanData;
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
		String error = "An unspecified error has occured while running this command.";
		boolean canBan = false;
		boolean canTempBan = false;
		boolean canUnBan = false;
		boolean canKick = false;
		boolean canLookup = false;
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (GlobalConf.mcbansNodes) {
				canBan = hasPermission(player, "mcbans.ban") | hasPermission(player, "mcbans.ban.local") | hasPermission(player, "mcbans.ban.global");
				canTempBan = hasPermission(player, "mcbans.ban.temp");
				canUnBan = hasPermission(player, "mcbans.unban");
				canLookup = hasPermission(player, "mcbans.lookup");
				canKick = hasPermission(player, "mcbans.kick");
			}
			else {
				canBan = hasPermission(player, "colebans.ban");
				canTempBan = hasPermission(player, "colebans.tempban");
				canUnBan = hasPermission(player, "colebans.unban");
				canLookup = hasPermission(player, "colebans.lookup") | hasPermission(player, "colebans.check");
				canKick = hasPermission(player, "colebans.kick");
			}
		}
		else {
			canBan = true;
			canTempBan = true;
			canUnBan = true;
			canLookup = true;
			canKick = true;
		}
		if (cmdLabel.equalsIgnoreCase("ban")) {
			if (canBan) {
				if (args.length < 2) error = ChatColor.RED+"You must specify a player and reason.";
				else {
					String victim = args[0];
					StringBuilder reasonBuilder = new StringBuilder();
					reasonBuilder.append(args[1]);
					for (int i = 2; i<args.length; i++) {
						reasonBuilder.append(" ");
						reasonBuilder.append(args[i]);
					}
					String reason = reasonBuilder.toString();
					try {
						banHandler.banPlayer(victim, reason);
						if (GlobalConf.announceBansAndKicks) server.broadcastMessage(ChatColor.valueOf(GlobalConf.banColor)+victim+" was banned! ["+reason+"]");
						return true;
					} catch (PlayerAlreadyBannedException e) {
						error = ChatColor.DARK_RED+victim+" is already banned!";
					}
				}
			}
			else error = ChatColor.RED+"You do not have permission to use the "+cmdLabel+" command.";
		}
		else if (cmdLabel.equalsIgnoreCase("tempban")) {
			if (canTempBan) {
				if (args.length < 2) error = ChatColor.RED+"You must specify a player and time (in minutes).";
				else if (args.length > 2) error = ChatColor.RED+"Too many arguments. Usage: /tempban <player> <minutes>";
				else {
					String victim = args[0];
					try {
						Long time = new Long(args[1]);
						if (time > 2880) {
							error = ChatColor.RED+"You cannot temp ban for more than 2 days!";
						}
						else {
							try {
								banHandler.tempBanPlayer(victim, time);
								if (GlobalConf.announceBansAndKicks) server.broadcastMessage(ChatColor.valueOf(GlobalConf.tempBanColor)+victim+" was temporarily banned! ["+time+" minute"+getPlural(time)+"]");
								return true;
							} catch (PlayerAlreadyBannedException e) {
								error = ChatColor.DARK_RED+victim+" is already banned!";
							} catch (MethodNotSupportedException e) {
								error = ChatColor.DARK_RED+"Temporary bans are disabled!";
							}
						}
					}
					catch (NumberFormatException e) {
						error = ChatColor.RED+"Expected number for minutes, got String (or number too high)";
					}
				}
			}
			else error = ChatColor.RED+"You do not have permission to use the "+cmdLabel+" command.";
		}
		else if (cmdLabel.equalsIgnoreCase("unban")) {
			if (canUnBan) {
				if (args.length < 1) error = ChatColor.RED+"You must specify a player";
				else if (args.length > 1) error = ChatColor.RED+"Too many arguments. Usage: /unban <player>";
				else {
					String victim = args[0];
					try {
						banHandler.unbanPlayer(victim);
						if (GlobalConf.announceBansAndKicks) server.broadcastMessage(ChatColor.GREEN+victim+" was unbanned!");
						return true;
					} catch (PlayerNotBannedException e) {
						error = ChatColor.DARK_RED+victim+" is not banned!";
					}
				}
			}
			else error = ChatColor.RED+"You do not have permission to use the "+cmdLabel+" command.";
		}
		else if (cmdLabel.equalsIgnoreCase("lookup") | cmdLabel.equalsIgnoreCase("check")) {
			if (canLookup) {
				if (args.length < 1) error = ChatColor.RED+"You must specify a player";
				else if (args.length > 1) error = ChatColor.RED+"Too many arguments. Usage: /lookup <player>";
				else {
					String victim = args[0];
					BanData bd = banHandler.getBanData(victim);
					if (bd.getType() == BanHandler.Type.PERMANENT) {
						sender.sendMessage(ChatColor.RED+"-- "+ChatColor.DARK_RED+victim+ChatColor.RED+" --");
						sender.sendMessage(ChatColor.RED+"Ban Type: "+ChatColor.DARK_RED+"Permanent");
						sender.sendMessage(ChatColor.RED+"Reason: "+ChatColor.DARK_RED+bd.getReason());
					}
					else if (bd.getType() == BanHandler.Type.TEMPORARY) {
						sender.sendMessage(ChatColor.YELLOW+"-- "+ChatColor.GOLD+victim+ChatColor.YELLOW+" --");
						sender.sendMessage(ChatColor.YELLOW+"Ban Type: "+ChatColor.GOLD+"Temporary");
						long timeRemaining = bd.getTime()-System.currentTimeMillis();
						timeRemaining /= 1000;
						timeRemaining /= 60;
						sender.sendMessage(ChatColor.YELLOW+"Time Remaining (Minutes): "+ChatColor.GOLD+timeRemaining);
					}
					else if (bd.getType() == BanHandler.Type.NOT_BANNED) {
						sender.sendMessage(ChatColor.AQUA+"-- "+ChatColor.DARK_AQUA+victim+ChatColor.AQUA+" --");
						sender.sendMessage(ChatColor.AQUA+"Ban Type: "+ChatColor.DARK_AQUA+"Not Banned");
					}
					return true;
				}
			}
			else error = ChatColor.RED+"You do not have permission to use the "+cmdLabel+" command.";
		}
		else if (cmdLabel.equalsIgnoreCase("kick")) {
			if (canKick) {
				if (args.length < 2) error = ChatColor.RED+"You must specify a player and reason.";
				else {
					String victim = args[0];
					StringBuilder reasonBuilder = new StringBuilder();
					reasonBuilder.append(args[1]);
					for (int i = 2; i<args.length; i++) {
						reasonBuilder.append(" ");
						reasonBuilder.append(args[i]);
					}
					String reason = reasonBuilder.toString();
					try {
						kickPlayer(victim, reason);
						if (GlobalConf.announceBansAndKicks) server.broadcastMessage(ChatColor.valueOf(GlobalConf.kickColor)+victim+" was kicked! ["+reason+"]");
					} catch (PlayerOfflineException e) {
						error = ChatColor.DARK_RED+victim+" is not online!";
					}
				}
			}
			else error = ChatColor.RED+"You do not have permission to use the "+cmdLabel+" command.";
		}
		sender.sendMessage(error);
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
