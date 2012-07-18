package com.cole2sworld.colebans;

import static com.cole2sworld.colebans.handlers.MySQLBanHandler.addSlashes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.unibia.simplemysql.SimpleMySQL;

import com.cole2sworld.colebans.framework.LogEntry;

/**
 * Manages logging of actions.
 * 
 * @author cole2
 * 
 */
public class ActionLogManager {
	public static enum Type {
		BAN,
		UNBAN,
		TEMPBAN,
		KICK,
		SWITCH,
		LOCAL_BAN,
		OTHER,
		UNKNOWN,
		BANHAMMER_BAN,
		BANHAMMER_KICK;
		public static Type forOrdinal(final int ordinal) {
			if ((ordinal < 0) || (ordinal >= values().length)) return UNKNOWN;
			return values()[ordinal];
		}
	}
	
	private static boolean		triedInit	= false;
	private static String		tablePrefix;
	private static SimpleMySQL	sql;
	private static String		tbl;
	private static boolean		initialized	= false;
	
	/**
	 * Add an entry to the log, using the current time.<br/>
	 * <i>Ban handlers should <b>never</b> call this, unless logging a 'UNKNOWN'
	 * or 'OTHER' action. All the command classes will log for you.</i>
	 * 
	 * @param type
	 *            Type of action being logged (LogManager.Type)
	 * @param admin
	 *            Admin that took the action
	 * @param victim
	 *            Victim of the action
	 */
	public static void addEntry(final Type type, final String admin, final String victim) {
		if (!Main.instance.banHandler.getTruncatedName().equals("mysql")) return;
		verify();
		final PreparedStatement stmt = sql.prepare("INSERT INTO " + tbl
				+ " (type, admin, victim, time) VALUES (?, ?, ?, ?);");
		try {
			Main.debug("Logging " + admin.toLowerCase() + " doing something to "
					+ victim.toLowerCase());
			stmt.setInt(1, type.ordinal());
			stmt.setString(2, admin.toLowerCase());
			stmt.setString(3, victim.toLowerCase());
			stmt.setLong(4, System.currentTimeMillis());
			stmt.execute();
		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * De-initialize LogManager - just disconnects from MySQL.
	 */
	public static void deinitialize() {
		if (!initialized) return;
		sql.close();
		sql = null;
		initialized = false;
	}
	
	/**
	 * Get all actions limiting to <code>limit</code>
	 * 
	 * @param limit
	 *            Amount of entries to limit to, or -1 to get all entires
	 * @return All results
	 */
	public static List<LogEntry> getAll(final int limit) {
		if (!Main.instance.banHandler.getTruncatedName().equals("mysql")) return null;
		verify();
		final ArrayList<LogEntry> entries = new ArrayList<LogEntry>();
		final PreparedStatement stmt = sql.prepare("SELECT * FROM " + tbl + " ORDER BY time DESC "
				+ (limit == -1 ? ";" : "LIMIT " + limit + ";"));
		try {
			final ResultSet result = stmt.executeQuery();
			for (; result.next();) {
				entries.add(new LogEntry(Type.forOrdinal(result.getInt("type")), result
						.getString("admin"), result.getString("victim"), result.getLong("time")));
			}
			return Util.reverseList(entries);
		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Get all actions made by a specific admin
	 * 
	 * @param admin
	 *            The admin to lookup
	 * @return All results
	 */
	public static List<LogEntry> getBy(final String admin) {
		if (!Main.instance.banHandler.getTruncatedName().equals("mysql")) return null;
		verify();
		final ArrayList<LogEntry> entries = new ArrayList<LogEntry>();
		final PreparedStatement stmt = sql.prepare("SELECT * FROM " + tbl + " WHERE admin=?");
		try {
			stmt.setString(1, admin.toLowerCase());
			final ResultSet result = stmt.executeQuery();
			for (; result.next();) {
				entries.add(new LogEntry(Type.forOrdinal(result.getInt("type")), result
						.getString("admin"), result.getString("victim"), result.getLong("time")));
			}
			return entries;
		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Get all actions made on a specific victim, by a specific admin
	 * 
	 * @param admin
	 *            Admin to lookup
	 * @param victim
	 *            Victim to lookup
	 * @return All results
	 */
	public static List<LogEntry> getByOn(final String admin, final String victim) {
		if (!Main.instance.banHandler.getTruncatedName().equals("mysql")) return null;
		verify();
		final ArrayList<LogEntry> entries = new ArrayList<LogEntry>();
		final String st = "SELECT * FROM " + tbl + " WHERE ("
				+ (admin.equals("*") ? "" : "admin='" + addSlashes(admin.toLowerCase()) + "'")
				+ (victim.equals("*") || admin.equals("*") ? "" : ", ")
				+ (victim.equals("*") ? "" : "victim='" + addSlashes(victim.toLowerCase()) + "'")
				+ ");";
		Main.debug(st);
		final PreparedStatement stmt = sql.prepare(st);
		try {
			final ResultSet result = stmt.executeQuery();
			for (; result.next();) {
				entries.add(new LogEntry(Type.forOrdinal(result.getInt("type")), result
						.getString("admin"), result.getString("victim"), result.getLong("time")));
			}
			return entries;
		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Get all actions made on a specific victim
	 * 
	 * @param victim
	 *            The victim to lookup
	 * @return All results
	 */
	public static List<LogEntry> getTo(final String victim) {
		if (!Main.instance.banHandler.getTruncatedName().equals("mysql")) return null;
		verify();
		final ArrayList<LogEntry> entries = new ArrayList<LogEntry>();
		final PreparedStatement stmt = sql.prepare("SELECT * FROM " + tbl + " WHERE victim=?");
		try {
			stmt.setString(1, victim.toLowerCase());
			final ResultSet result = stmt.executeQuery();
			for (; result.next();) {
				entries.add(new LogEntry(Type.forOrdinal(result.getInt("type")), result
						.getString("admin"), result.getString("victim"), result.getLong("time")));
			}
			return entries;
		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Initialize LogManager - connect to MySQL, set up variables, etc.
	 */
	public static void initialize() {
		if (initialized) return;
		if (!Main.instance.banHandler.getTruncatedName().equals("mysql")) {
			if (!triedInit) {
				Main.LOG.warning(Main.PREFIX
						+ "[ActionLogManager] Could not initialize - current ban handler is not MySQL.");
				triedInit = true;
			}
			return;
		}
		final Map<String, String> data = Main.getBanHandlerInitArgs();
		sql = new SimpleMySQL();
		sql.connect(data.get("host"), data.get("username"), data.get("password"));
		sql.use(data.get("db"));
		tablePrefix = data.get("prefix");
		tbl = tablePrefix + "log";
		initialized = true;
	}
	
	/**
	 * Cut a list of LogEntrys to a certain time
	 * 
	 * @param timeMillis
	 *            Time to cut to
	 * @return Modified list
	 */
	public static List<LogEntry> since(final long timeMillis, final List<LogEntry> oldlist) {
		if (!Main.instance.banHandler.getTruncatedName().equals("mysql")) return null;
		checkConnectionAIDR();
		final ArrayList<LogEntry> list = new ArrayList<LogEntry>();
		for (final LogEntry entry : oldlist) {
			if (entry.getTime() >= timeMillis) {
				list.add(entry);
			}
		}
		return list;
	}
	
	/**
	 * Check the connection, and <b>i</b>f <b>d</b>isconnected,
	 * <b>r</b>econnect.
	 * 
	 * @return How long the operation took
	 */
	private static long checkConnectionAIDR() {
		if (!Main.instance.banHandler.getTruncatedName().equals("mysql")) return 0;
		final long oldtime = System.currentTimeMillis();
		final SimpleMySQL.State st = sql.checkConnection();
		final boolean didSomething = false;
		if (st != SimpleMySQL.State.CONNECTED) {
			System.out.println(Main.PREFIX + "[LogManager] Re-initalizing connection");
			final Map<String, String> data = Main.getBanHandlerInitArgs();
			sql = new SimpleMySQL();
			sql.connect(data.get("host"), data.get("username"), data.get("password"));
			sql.use(data.get("db"));
			tablePrefix = data.get("prefix");
			tbl = tablePrefix + "log";
		}
		final long newtime = System.currentTimeMillis();
		if (didSomething) {
			System.out.println(Main.PREFIX + "[LogManager] Done. Took " + (newtime - oldtime)
					+ " ms.");
		}
		return newtime - oldtime;
	}
	
	private static void verify() {
		if (!Main.instance.banHandler.getTruncatedName().equals("mysql")) return;
		initialize();
		checkConnectionAIDR();
		if (!sql.checkTable(tbl)) {
			sql.query("CREATE  TABLE " + tbl + " (" + "`type` INT UNSIGNED NOT NULL ,"
					+ "`admin` VARCHAR(45) NULL ," + "`victim` VARCHAR(45) NULL ,"
					+ "`time` BIGINT PK UNSIGNED NULL ," + "INDEX `main` (`time` ASC) );");
		}
	}
}
