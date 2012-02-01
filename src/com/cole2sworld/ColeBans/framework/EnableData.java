package com.cole2sworld.ColeBans.framework;

import java.io.File;

public class EnableData {
	public final String username;
	public final String password;
	public final String host;
	public final String port;
	public final String prefix;
	public final String db;
	public final File yaml;
	public final File json;
	public final String api;
	public EnableData(String username, String password, String host,
			String port, String prefix, String db, File yaml, File json,
			String api) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		this.prefix = prefix;
		this.db = db;
		this.yaml = yaml;
		this.json = json;
		this.api = api;
	}
}
