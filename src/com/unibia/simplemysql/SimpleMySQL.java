/*
 * 
 * $Id$
 * 
 * Software License Agreement (BSD License)
 * 
 * Copyright (c) 2011, The Daniel Morante Company, Inc. All rights reserved.
 * 
 * Redistribution and use of this software in source and binary forms, with or
 * without modification, are permitted provided that the following conditions
 * are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of The Daniel Morante Company, Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission of The Daniel Morante
 * Company, Inc.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * Simple MySQL Java Class Makes it similar to PHP
 */

package com.unibia.simplemysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;
import java.sql.Statement;

import com.mysql.jdbc.CommunicationsException;
import com.mysql.jdbc.exceptions.MySQLNonTransientConnectionException;

import com.cole2sworld.colebans.ColeBansPlugin;

/**
 * 
 * @author Daniel Morante
 */
public final class SimpleMySQL {
	public enum State {
		CONNECTED,
		NEVER_CONNECTED,
		LOST,
		UNKNOWN
	}
	
	private Connection	mysqlConnection;
	
	public SimpleMySQL() {
	}
	
	public State checkConnection() {
		Statement stmt;
		ResultSet mysqlResult;
		try {
			// Execute Query
			stmt = mysqlConnection.createStatement();
			mysqlResult = stmt.executeQuery("SELECT 1 from DUAL WHERE 1=0");
			mysqlResult.close();
			return State.CONNECTED;
		} catch (final CommunicationsException e) {
			return State.LOST;
		} catch (final NullPointerException e) {
			return State.NEVER_CONNECTED;
		} catch (final SQLTransientConnectionException e) {
			return State.LOST;
		} catch (final SQLException e) {
			return State.LOST;
		} finally {
			mysqlResult = null;
		}
	}
	
	public boolean checkTable(final String tbl) {
		ColeBansPlugin.debug("Checking table");
		checkConnection();
		Statement stmt = null;
		try {
			ColeBansPlugin.debug("Creating statement");
			stmt = mysqlConnection.createStatement();
		} catch (final SQLException e) {
			ColeBansPlugin.debug("SQLException creating statement: " + e.getMessage());
			return false;
		}
		ResultSet result = null;
		try {
			ColeBansPlugin.debug("Executing query");
			result = stmt.executeQuery("SELECT * FROM " + tbl + " LIMIT 1;");
		} catch (final SQLException e) {
			ColeBansPlugin.debug("SQLException executing query: SELECT * FROM " + tbl + " LIMIT 1; ("
					+ e.getMessage() + ")");
			return false;
		} finally {
			if (result != null) {
				try {
					ColeBansPlugin.debug("Closing result set");
					result.close();
				} catch (final SQLException e) {
					ColeBansPlugin.debug("SQLException closing result set: " + e.getMessage());
				}
			}
		}
		ColeBansPlugin.debug("Reached end of method, returning true");
		return true;
	}
	
	/**
	 * Closes the current MySQL Connection.
	 * 
	 * @return True on close
	 */
	public boolean close() {
		ColeBansPlugin.debug("Closing connection");
		try {
			mysqlConnection.close();
			ColeBansPlugin.debug("Close succeeded");
			return true;
		} catch (final Exception x) {
			return false;
		}
	}
	
	/**
	 * Connects to the MySQL Server using the given server, username, and
	 * password.
	 * 
	 * @param server
	 * @param username
	 * @param password
	 * @return True on a successful connection
	 */
	public boolean connect(final String server, final String username, final String password) {
		ColeBansPlugin.debug("Connecting");
		String mysqlConnectionURL;
		String mysqlDriver;
		
		try {
			// Load MySQL JDBC Driver
			mysqlDriver = "com.mysql.jdbc.Driver";
			Class.forName(mysqlDriver);
			
			// Open Connection
			mysqlConnectionURL = "jdbc:mysql://" + server;
			mysqlConnection = DriverManager.getConnection(mysqlConnectionURL, username, password);
			ColeBansPlugin.debug("Connected");
			return true;
		} catch (final Exception x) {
			
			return false;
		}
	}
	
	/**
	 * Connects to the MySQL Server using the given server, username, and
	 * password. Auto selects the given database.
	 * 
	 * @param server
	 * @param username
	 * @param password
	 * @param database
	 * @return True on a successful connection.
	 */
	public boolean connect(final String server, final String username, final String password,
			final String database) {
		
		if (connect(server, username, password)) return use(database);
		return false;
	}
	
	public PreparedStatement prepare(final String prepare) {
		try {
			return mysqlConnection.prepareStatement(prepare);
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Executes a simple Query on the MySQL database. You must first connect to
	 * a MySQL database. If a MySQL database is not connected this will return
	 * null.
	 * 
	 * @param query
	 *            The SQL query
	 * @return For SELECT type queries a SimpleMySQLResult. All other type of
	 *         queries will return null
	 * @see #connect(java.lang.String, java.lang.String, java.lang.String)
	 * @see #connect(java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	public ResultSet query(String query) {
		ColeBansPlugin.debug("Running query " + query);
		// Make sure connection is alive
		checkConnection();
		
		// Create Statement and result objects
		Statement stmt;
		ResultSet mysqlResult;
		ResultSet result = null;
		
		// make sure we aren't idiots
		if (!query.endsWith(";")) {
			query = query + ";";
		}
		
		/*
		 * We want to keep things simple, so...
		 * 
		 * Detect whether this is an INSERT, DELETE, or UPDATE statement And use
		 * the executeUpdate() function
		 * 
		 * Or...
		 * 
		 * Detect whether this is a SELECT statment and use the executeQuery()
		 * Function.
		 */
		try {
			ColeBansPlugin.debug("Detecting query type");
			if (query.startsWith("SELECT")) {
				ColeBansPlugin.debug("SELECT");
				// Use the "executeQuery" function becuase we have to retrive
				// data
				// Return the data as a resultset
				
				// Execute Query
				stmt = mysqlConnection.createStatement();
				ColeBansPlugin.debug("Statement created");
				mysqlResult = stmt.executeQuery(query);
				ColeBansPlugin.debug("Query executed");
				result = mysqlResult;
				ColeBansPlugin.debug("Result created");
			} else {
				ColeBansPlugin.debug("UPDATE/INSERT/DELETE");
				// It's an UPDATE, INSERT, or DELETE statement
				// Use the "executeUpdate" function and return a null result
				
				// Execute Query
				stmt = mysqlConnection.createStatement();
				ColeBansPlugin.debug("Statement created");
				stmt.executeUpdate(query);
				ColeBansPlugin.debug("Query executed");
			}
		} catch (final NullPointerException y) {
			System.err.println("You are not connected to a MySQL server");
		} catch (final MySQLNonTransientConnectionException e) {
			System.err.println("MySQL server Connection was lost");
		} catch (final Exception x) {
			System.err.println("ERROR: " + x.getLocalizedMessage());
		}
		
		// Return the SimpleMySQLResult Object or null
		return result;
	}
	
	/**
	 * Manually select the database to Query.
	 * 
	 * @param database
	 * @return True on successful operation.
	 */
	public boolean use(final String database) {
		boolean result = true;
		try {
			mysqlConnection.setCatalog(database);
		} catch (final Exception e) {
			result = false;
		}
		return result;
	}
}
