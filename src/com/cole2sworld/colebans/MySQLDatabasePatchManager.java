/**
 * 
 */
package com.cole2sworld.colebans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.unibia.simplemysql.SimpleMySQL;

/**
 * @author cole2
 * 
 */
public class MySQLDatabasePatchManager {




	// FORM off
	private static final String[][]	patches	= {
						// r1
						new String[] {
									// temp ban reasons
									"ALTER TABLE `{$CB_DATABASE}`.`{$CB_PREFIX}temp` ADD COLUMN `reason` VARCHAR(255) NULL DEFAULT 'Temporary Ban' AFTER `time` ;",
									// meta information
									"CREATE  TABLE `{$CB_DATABASE}`.`{$CB_PREFIX}meta` (" + 
									"  `key` VARCHAR(255) NOT NULL ," + 
									"  `value` VARCHAR(255) NULL ," + 
									"  PRIMARY KEY (`key`) ," + 
									"  UNIQUE INDEX `key_UNIQUE` (`key` ASC) );",
									// revision information
									"INSERT INTO `{$CB_DATABASE}`.`{$CB_PREFIX}meta` (`key`, `value`) VALUES ('revision', '1');"
								}
						};
	//FORM on
	public static final int DATABASE_REVISION = patches.length;
	private static final String REVISION_UPDATE_STATEMENT = "UPDATE `{$CB_DATABASE}`.`{$CB_PREFIX}meta` SET `value`='"+DATABASE_REVISION+"' WHERE `key`='revision';";
	public static void check() {
		final Map<String, String> data = Main.getBanHandlerInitArgs();
		final SimpleMySQL sql = new SimpleMySQL();
		sql.connect(data.get("host"), data.get("username"), data.get("password"));
		sql.use(data.get("db"));
		if (!sql.checkTable(data.get("prefix")+"meta")) {
			// must be r0
			for (final String query : patches[0]) {
				sql.query(query
						.replace("{$CB_DATABASE}", data.get("db"))
						.replace("{$CB_PREFIX}", data.get("prefix"))
						);
			}
		}
		{
			ResultSet result = null;
			try {
				result = sql.query("SELECT value FROM `"+data.get("db")+"`.`"+data.get("prefix")+"meta` WHERE key='revision';");
				result.first();
				final int rev = result.getInt("value");
				if (rev < 0) {
					System.out.println(Main.PREFIX+"[MySQLDatabasePatchManager] Invalid database revision id - overwriting");
				}
				if (rev < DATABASE_REVISION) {
					for (final String query : patches[rev]) {
						sql.query(query
								.replace("{$CB_DATABASE}", data.get("db"))
								.replace("{$CB_PREFIX}", data.get("prefix"))
								);
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				if (result != null) {
					try {
						result.close();
					} catch (final SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		sql.query(REVISION_UPDATE_STATEMENT
				.replace("{$CB_DATABASE}", data.get("db"))
				.replace("{$CB_PREFIX}", data.get("prefix")));
		sql.close();
	}
}
