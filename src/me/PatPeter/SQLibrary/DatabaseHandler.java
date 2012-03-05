
package me.PatPeter.SQLibrary;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Logger;
/**
 * Database Handler
 * Abstract superclass for all subclass database files.
 * 
 * Date Created: 2011-08-26 19:08
 * @author PatPeter
 */
public abstract class DatabaseHandler {
	protected Logger log;
	protected final String PREFIX;
	protected final String DATABASE_PREFIX;
	protected Connection connection;
	protected enum Statements {
		SELECT, INSERT, UPDATE, DELETE, DO, REPLACE, LOAD, HANDLER, CALL, // Data manipulation statements
		CREATE, ALTER, DROP, TRUNCATE, RENAME  // Data definition statements
	}
	
	/*
	 *  MySQL, SQLLite
	 */
	
	public DatabaseHandler(final Logger log, final String prefix, final String dp) {
		this.log = log;
		this.PREFIX = prefix;
		this.DATABASE_PREFIX = dp;
	}
	
	/**
	 * <b>writeInfo</b><br>
	 * <br>
	 * &nbsp;&nbsp;Writes information to the console.
	 * <br>
	 * <br>
	 * @param toWrite - the <a href="http://download.oracle.com/javase/6/docs/api/java/lang/String.html">String</a>
	 * of content to write to the console.
	 */
	protected void writeInfo(final String toWrite) {
		if (toWrite != null) {
			this.log.info(this.PREFIX + this.DATABASE_PREFIX + toWrite);
		}
	}
	
	/**
	 * <b>writeError</b><br>
	 * <br>
	 * &nbsp;&nbsp;Writes either errors or warnings to the console.
	 * <br>
	 * <br>
	 * @param toWrite - the <a href="http://download.oracle.com/javase/6/docs/api/java/lang/String.html">String</a>
	 * written to the console.
	 * @param severe - whether console output should appear as an error or warning.
	 */
	protected void writeError(final String toWrite, final boolean severe) {
		if (toWrite != null) {
			if (severe) {
				this.log.severe(this.PREFIX + this.DATABASE_PREFIX + toWrite);
			} else {
				this.log.warning(this.PREFIX + this.DATABASE_PREFIX + toWrite);
			}
		}
	}
	
	/**
	 * <b>open</b><br>
	 * <br>
	 * &nbsp;&nbsp;Opens a connection with the database.
	 * <br>
	 * <br>
	 * @return the success of the method.
	 */
	public abstract Connection open();
	
	/**
	 * <b>close</b><br>
	 * <br>
	 * &nbsp;&nbsp;Closes a connection with the database.
	 * <br>
	 * <br>
	 */
	public abstract void close();
	
	/**
	 * <b>checkConnection</b><br>
	 * <br>
	 * Checks the connection between Java and the database engine.
	 * <br>
	 * <br>
	 * @return the status of the connection, true for up, false for down.
	 */
	public abstract boolean checkConnection();
	
	/**
	 * <b>query</b><br>
	 * &nbsp;&nbsp;Sends a query to the SQL database.
	 * <br>
	 * <br>
	 * @param query - the SQL query to send to the database.
	 * @return the table of results from the query.
	 */
	public abstract ResultSet query(final String query);
	
	/**
	 * <b>getStatement</b><br>
	 * &nbsp;&nbsp;Determines the name of the statement and converts it into an enum.
	 * <br>
	 * <br>
	 */
	protected static Statements getStatement(final String query) {
		Statements rtrn;
		try {
			rtrn = Statements.valueOf(query.trim().split(" ")[0]);
		}
		catch (IllegalArgumentException e) {
			rtrn = Statements.SELECT;
		}
		return rtrn;
	}
	
	/**
	 * <b>createTable</b><br>
	 * <br>
	 * &nbsp;&nbsp;Creates a table in the database based on a specified query.
	 * <br>
	 * <br>
	 * @param query - the SQL query for creating a table.
	 * @return the success of the method.
	 */
	public abstract boolean createTable(final String query);
	
	/**
	 * <b>checkTable</b><br>
	 * <br>
	 * &nbsp;&nbsp;Checks a table in a database based on the table's name.
	 * <br>
	 * <br>
	 * @param table - name of the table to check.
	 * @return success of the method.
	 */
	public abstract boolean checkTable(final String table);
	
	/**
	 * <b>wipeTable</b><br>
	 * <br>
	 * &nbsp;&nbsp;Wipes a table given its name.
	 * <br>
	 * <br>
	 * @param table - name of the table to wipe.
	 * @return success of the method.
	 */
	public abstract boolean wipeTable(final String table);
}