package me.PatPeter.SQLibrary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * MySQL Inherited subclass for making a connection to a MySQL server.
 * 
 * Date Created: 2011-08-26 19:08
 * 
 * @author PatPeter
 */
public class MySQL extends DatabaseHandler {
	private final String hostname;
	private final String portnmbr;
	private final String username;
	private final String password;
	private final String database;

	public MySQL(final Logger log, final String prefix, final String hostname, final String portnmbr,
			final String database, final String username, final String password) {
		super(log, prefix, "[MySQL] ");
		this.hostname = hostname;
		this.portnmbr = portnmbr;
		this.database = database;
		this.username = username;
		this.password = password;
		open();
	}

	@Override
	public Connection open() {
		if (connection == null) {
			try {
				String url = "jdbc:mysql://" + this.hostname + ":" + this.portnmbr
						+ "/" + this.database;
				connection = DriverManager.getConnection(url, this.username,
						this.password);
			} catch (SQLException e) {
				this.writeError("Could not be resolved because of an SQL Exception: "
								+ e.getMessage() + ".", true);
			}
		} else
			try {
				if (!connection.isValid(5)) {
					connection = null;
					return open();
				}
			} catch (SQLException e) {} 
		return connection;
	}

	@Override
	public void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (Exception e) {
			this.writeError(
					"Failed to close database connection: " + e.getMessage(),
					true);
		}
	}

	@Override
	public boolean checkConnection() {
		if (connection != null) {
			try {
				return connection.isValid(5);
			} catch (SQLException e) {
				return false;
			}
		}
		else return false;
	}

	@Override
	public ResultSet query(final String query) {
		ResultSet result = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();

			if (DatabaseHandler.getStatement(query) == Statements.SELECT) {
				result = statement.executeQuery(query);
			} else {
				statement.executeUpdate(query);
			}
		} catch (SQLException e) {
			this.writeError("Error in SQL query: " + e.getMessage(), false);
		}
		finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					this.writeError("Failed to close statement", true);
				}
			}
		}
		return result;
	}

	@Override
	public boolean createTable(final String query) {
		Statement statement = null;
		try {
			if (query.equals("")) {
				this.writeError("SQL query empty: createTable(" + query + ")",
						true);
				return false;
			}

			statement = connection.createStatement();
			statement.execute(query);
			return true;
		} catch (SQLException e) {
			this.writeError(e.getMessage(), true);
			return false;
		} catch (Exception e) {
			this.writeError(e.getMessage(), true);
			return false;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					this.writeError("Failed to close statement", true);
				}
			}
		}
	}

	@Override
	public boolean checkTable(final String table) {
		ResultSet result = query("SELECT * FROM " + table + " LIMIT 1;");
		try {
			result.close();
		} catch (Exception e) {}
		return result != null;
	}

	@Override
	public boolean wipeTable(final String table) {
		Statement statement = null;
		String query = null;
		try {
			if (!this.checkTable(table)) {
				this.writeError("Error wiping table: \"" + table
						+ "\" does not exist.", true);
				return false;
			}
			statement = connection.createStatement();
			query = "DELETE FROM " + table + ";";
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			if (!e.toString().contains("not return ResultSet"))
				return false;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					this.writeError("Failed to close statement", true);
				}
			}
		}
		return false;
	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}