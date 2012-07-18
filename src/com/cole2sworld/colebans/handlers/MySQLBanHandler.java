package com.cole2sworld.colebans.handlers;

import static com.cole2sworld.colebans.Main.debug;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

import com.unibia.simplemysql.SimpleMySQL;

import com.cole2sworld.colebans.Main;
import com.cole2sworld.colebans.MySQLDatabasePatchManager;
import com.cole2sworld.colebans.framework.GlobalConf;
import com.cole2sworld.colebans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.colebans.framework.PlayerNotBannedException;

public final class MySQLBanHandler extends BanHandler {
	/**
	 * Sanitizes the input to be safe for usage in a SQL query.
	 * 
	 * @param workset
	 *            The string to process
	 * @return The sanitized string
	 */
	public static String addSlashes(final String workset) {
		Main.debug("Sanitizing " + workset);
		final StringBuilder sanitizer = new StringBuilder();
		for (int i = 0; i < workset.length(); i++) {
			if (!isSQLSpecialCharacter(workset.charAt(i))) {
				sanitizer.append("\\");
				sanitizer.append(workset.charAt(i));
			} else {
				sanitizer.append(workset.charAt(i));
			}
		}
		Main.debug("Done - workset is now " + sanitizer.toString());
		return sanitizer.toString();
	}
	
	/**
	 * @param charAt
	 *            Character to check
	 * @return If escaping this character in MySQL will cause strange things to
	 *         happen
	 */
	public static boolean isSQLSpecialCharacter(final Character charAt) {
		final String workset = charAt.toString();
		if ("0".equalsIgnoreCase(workset))
			return true;
		else if ("b".equalsIgnoreCase(workset))
			return true;
		else if ("n".equalsIgnoreCase(workset))
			return true;
		else if ("r".equalsIgnoreCase(workset))
			return true;
		else if ("t".equalsIgnoreCase(workset))
			return true;
		else if ("z".equalsIgnoreCase(workset))
			return true;
		else
			return false;
	}
	
	public static BanHandler onEnable() {
		MySQLDatabasePatchManager.check();
		final Map<String, String> data = Main.getBanHandlerInitArgs();
		return new MySQLBanHandler(data.get("username"), data.get("password"), data.get("host"),
				data.get("db"));
	}
	
	private final String	username;
	private final String	password;
	private final String	host;
	private final String	db;
	private SimpleMySQL		sqlHandler;
	
	/**
	 * Creates a new MySQLBanHandler using a database with the given settings
	 * 
	 * @param username
	 *            - The username to log into the server
	 * @param password
	 *            - The password to log into the server
	 * @param host
	 *            - The hostname to log into the server
	 * @param db
	 *            - The database to use
	 */
	public MySQLBanHandler(final String username, final String password, final String host,
			final String db) {
		System.out.println(Main.PREFIX + "[MySQLBanHandler] Opening connection");
		final long oldtime = System.currentTimeMillis();
		sqlHandler = new SimpleMySQL();
		Main.debug("Got instance");
		sqlHandler.connect(host, username, password);
		Main.debug("Connected. Setting database");
		sqlHandler.use(db);
		final long newtime = System.currentTimeMillis();
		System.out.println(Main.PREFIX + "[MySQLBanHandler] Done. Took " + (newtime - oldtime)
				+ " ms.");
		this.username = username;
		this.password = password;
		this.host = host;
		this.db = db;
	}
	
	@Override
	public void banPlayer(final String player, final String reason, final String admin)
			throws PlayerAlreadyBannedException {
		checkConnectionAIDR();
		debug("Banning player " + player);
		if (isPlayerBanned(player, admin))
			throw new PlayerAlreadyBannedException(player + " is already banned!");
		debug("Not already banned");
		final String tbl = GlobalConf.get("mysql.prefix").asString() + "perm";
		if (sqlHandler.checkTable(tbl)) {
			debug("Table exists");
			sqlHandler.query("INSERT INTO " + tbl + " (" + "username, " + "reason" + ") VALUES ("
					+ "'" + addSlashes(player) + "', " + "'" + addSlashes(reason) + "'" + ");");
			debug("Query executed! SUCCESS!");
		} else {
			debug("Table does not exist");
			sqlHandler.query("CREATE  TABLE `" + GlobalConf.get("mysql.db").asString() + "`.`"
					+ tbl + "` (" + "`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,"
					+ "`username` VARCHAR(255) NULL ," + "`reason` VARCHAR(255) NULL ,"
					+ "PRIMARY KEY (`id`) );");
			debug("Table created");
			sqlHandler.query("ALTER TABLE `" + GlobalConf.get("mysql.db").asString() + "`.`" + tbl
					+ "`" + "ADD INDEX `NAMEINDEX` (`username` ASC);");
			debug("Table altered");
			debug("Re-calling");
			banPlayer(player, reason, admin);
		}
	}
	
	@Override
	public void convert(final BanHandler handler) {
		checkConnectionAIDR();
		final Vector<BanData> dump = dump(BanHandler.SYSTEM_ADMIN_NAME);
		for (final BanData data : dump) {
			if (GlobalConf.get("allowTempBans").asBoolean() && (data.getType() == Type.TEMPORARY)) {
				try {
					handler.tempBanPlayer(data.getVictim(), data.getTime(), data.getReason(),
							BanHandler.SYSTEM_ADMIN_NAME);
				} catch (final UnsupportedOperationException e) {
					// just skip it, tempbans are off
				} catch (final PlayerAlreadyBannedException e) {
					// just skip it, they are already banned in the target
				}
			} else if (data.getType() == Type.PERMANENT) {
				try {
					handler.banPlayer(data.getVictim(), data.getReason(),
							BanHandler.SYSTEM_ADMIN_NAME);
				} catch (final PlayerAlreadyBannedException e) {
					// just skip it, they are already banned in the target
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cole2sworld.ColeBans.handlers.BanHandler#countBans()
	 */
	@Override
	public long countBans(final String admin) {
		ResultSet result = null;
		try {
			result = sqlHandler.query("SELECT COUNT(username) FROM `"
					+ GlobalConf.get("mysql.db").asString() + "`.`"
					+ (GlobalConf.get("mysql.prefix").asString() + "perm") + "`");
			result.first();
			final long permCount = result.getLong(1);
			result.close();
			result = sqlHandler.query("SELECT COUNT(username) FROM `"
					+ GlobalConf.get("mysql.db").asString() + "`.`"
					+ (GlobalConf.get("mysql.prefix").asString() + "temp") + "`");
			result.first();
			final long tempCount = result.getLong(1);
			result.close();
			return permCount + tempCount;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	@Override
	public Vector<BanData> dump(final String admin) {
		checkConnectionAIDR();
		final Vector<BanData> data = new Vector<BanData>(50);
		final ResultSet temp = sqlHandler.query("SELECT * FROM "
				+ GlobalConf.get("mysql.prefix").asString() + "temp;");
		final ResultSet perm = sqlHandler.query("SELECT * FROM "
				+ GlobalConf.get("mysql.prefix").asString() + "perm;");
		if (temp != null) {
			try {
				for (; temp.next();) {
					final String name = temp.getString("username");
					final long time = temp.getLong("time");
					final String reason = temp.getString("reason");
					data.add(new BanData(name, time, reason));
				}
			} catch (final SQLException e) {
				e.printStackTrace();
				Main.LOG.warning(Main.PREFIX
						+ "Dump on MySQL failed due to SQLException -- dump will be truncated!");
			} finally {
				try {
					temp.close();
				} catch (final SQLException e) {
					// nothing we can do at this point if it fails
				}
			}
		}
		if (perm != null) {
			try {
				for (; perm.next();) {
					final String name = perm.getString("username");
					final String reason = perm.getString("reason");
					data.add(new BanData(name, reason));
				}
			} catch (final SQLException e) {
				e.printStackTrace();
				Main.LOG.warning(Main.PREFIX
						+ "Dump on MySQL failed due to SQLException -- dump will be truncated!");
			} finally {
				try {
					perm.close();
				} catch (final SQLException e) {
					// nothing we can do at this point if it fails
				}
			}
		}
		return data;
	}
	
	
	@Override
	public BanData getBanData(final String player, final String admin) {
		checkConnectionAIDR();
		debug("Getting ban data for " + player);
		final String tbl = GlobalConf.get("mysql.prefix").asString() + "perm";
		if (sqlHandler.checkTable(tbl)) {
			debug(tbl + " exists");
			final ResultSet reasonResult = sqlHandler.query("SELECT reason FROM `"
					+ GlobalConf.get("mysql.db").asString() + "`.`" + tbl + "` WHERE username='"
					+ addSlashes(player) + "';");
			debug("got reasonResult, it is " + reasonResult);
			boolean results = false;
			try {
				results = reasonResult.first();
			} catch (final SQLException e) {
				debug("SQLException getting first reason - " + e.getMessage());
			}
			if (results) {
				debug("There's a reason");
				String reason = "";
				try {
					reason = reasonResult.getString("reason");
					reasonResult.getString(3);
					debug(reason);
				} catch (final SQLException e) {
					debug("SQLException getting reason - " + e.getMessage());
				} finally {
					try {
						reasonResult.close();
					} catch (final SQLException e) {
						debug("SQLException closing ResultSet");
					}
				}
				if (!reason.isEmpty()) {
					debug("Reason not empty");
					return new BanData(player, reason);
				}
			}
		}
		final String tblB = GlobalConf.get("mysql.prefix").asString() + "temp";
		if (sqlHandler.checkTable(tblB)) {
			final ResultSet reasonResultB = sqlHandler.query("SELECT time,reason FROM `"
					+ GlobalConf.get("mysql.db").asString() + "`.`" + tblB + "` WHERE username='"
					+ addSlashes(player) + "';");
			boolean resultsB = false;
			try {
				resultsB = reasonResultB.first();
			} catch (final SQLException e) {
				// we don't care, discard it
			}
			if (resultsB) {
				long time = -1L;
				String reason = null;
				try {
					time = reasonResultB.getLong("time");
					reason = reasonResultB.getString("reason");
					if (reason == null) {
						reason = "Temporary Ban";
					}
					if (time <= System.currentTimeMillis()) {
						if (sqlHandler.checkTable(tblB)) {
							sqlHandler.query("DELETE FROM `"
									+ GlobalConf.get("mysql.db").asString() + "`.`" + tblB
									+ "` WHERE username='" + addSlashes(player) + "';");
						}
						return new BanData(player);
					}
				} catch (final SQLException e) {
					// if there's a sql exception we don't really care
				} finally {
					try {
						reasonResultB.close();
					} catch (final SQLException e) {
						debug("SQLException closing ResultSet");
					}
				}
				if ((time > -1) && (reason != null)) return new BanData(player, time, reason);
			}
		}
		return new BanData(player);
	}
	
	@Override
	public boolean isPlayerBanned(final String player, final String admin) {
		return (getBanData(player, admin).getType()) != Type.NOT_BANNED;
	}
	
	@Override
	public Vector<String> listBannedPlayers(final String admin) {
		// FIXME Don't use dump, be lighter and use MySQL
		checkConnectionAIDR();
		final Vector<String> list = new Vector<String>(50);
		final Vector<BanData> verbData = dump(admin);
		for (final BanData data : verbData) {
			list.add(data.getVictim());
		}
		return list;
	}
	
	@Override
	public void onDisable() {
		System.out.println(Main.PREFIX + "[MySQLBanHandler] Closing connection");
		final long oldtime = System.currentTimeMillis();
		sqlHandler.close();
		final long newtime = System.currentTimeMillis();
		System.out.println(Main.PREFIX + "[MySQLBanHandler] Done. Took " + (newtime - oldtime)
				+ " ms.");
	}
	
	@Override
	public void tempBanPlayer(final String player, final long primTime, final String reason,
			final String admin) throws PlayerAlreadyBannedException, UnsupportedOperationException {
		checkConnectionAIDR();
		if (!GlobalConf.get("allowTempBans").asBoolean())
			throw new UnsupportedOperationException("Temp bans are disabled!");
		if (isPlayerBanned(player, admin))
			throw new PlayerAlreadyBannedException(player + " is already banned!");
		final Long time = System.currentTimeMillis() + ((primTime * 60) * 1000);
		final String tbl = GlobalConf.get("mysql.prefix").asString() + "temp";
		if (sqlHandler.checkTable(tbl)) {
			{
				final String query = "INSERT INTO `%s`.`%s` (username, time, reason) VALUES ('%s', '%s', '%s');";
				sqlHandler.query(String.format(query, GlobalConf.get("mysql.db").asString(), tbl,
						addSlashes(player), time.toString(), reason));
			}
		} else {
			{
				final String query = "CREATE TABLE `%s`.`%s` (`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT , `username` VARCHAR(255) NULL ,`time` VARCHAR(255) NULL, `reason` VARCHAR(255) NULL ,PRIMARY KEY (`id`) );";
				sqlHandler.query(String.format(query, GlobalConf.get("mysql.db").asString(), tbl));
			}
			{
				final String query = "ALTER TABLE `%s`.`%s` ADD INDEX `NAMEINDEX` (`username` ASC);";
				sqlHandler.query(String.format(query, GlobalConf.get("mysql.db").asString(), tbl));
			}
			tempBanPlayer(player, primTime, reason, admin);
		}
	}
	
	@Override
	public void unbanPlayer(final String player, final String admin)
			throws PlayerNotBannedException {
		checkConnectionAIDR();
		final BanData bd = getBanData(player, admin);
		if (bd.getType() == Type.PERMANENT) {
			final String tbl = GlobalConf.get("mysql.prefix").asString() + "perm";
			if (sqlHandler.checkTable(tbl)) {
				sqlHandler.query("DELETE FROM `" + GlobalConf.get("mysql.db").asString() + "`.`"
						+ tbl + "` WHERE username='" + addSlashes(player) + "';");
				return;
			}
		} else if (bd.getType() == Type.TEMPORARY) {
			final String tbl = GlobalConf.get("mysql.prefix").asString() + "temp";
			if (sqlHandler.checkTable(tbl)) {
				sqlHandler.query("DELETE FROM `" + GlobalConf.get("mysql.db").asString() + "`.`"
						+ tbl + "` WHERE username='" + addSlashes(player) + "';");
				return;
			}
		}
		throw new PlayerNotBannedException(player + " is not banned!");
	}
	
	/**
	 * Check the connection, <b>a</b>nd <b>i</b>f <b>d</b>isconnected,
	 * <b>r</b>econnect.
	 * 
	 * @return How long the operation took
	 */
	private long checkConnectionAIDR() {
		final long oldtime = System.currentTimeMillis();
		final SimpleMySQL.State st = sqlHandler.checkConnection();
		boolean didSomething = false;
		if (st != SimpleMySQL.State.CONNECTED) {
			System.out.println(Main.PREFIX + "[MySQLBanHandler] Re-initalizing connection");
			sqlHandler = new SimpleMySQL();
			Main.debug("Got instance");
			sqlHandler.connect(host, username, password);
			Main.debug("Connected. Setting database");
			sqlHandler.use(db);
			didSomething = true;
		}
		final long newtime = System.currentTimeMillis();
		if (didSomething) {
			System.out.println(Main.PREFIX + "[MySQLBanHandler] Done. Took " + (newtime - oldtime)
					+ " ms.");
		}
		return newtime - oldtime;
	}
}
