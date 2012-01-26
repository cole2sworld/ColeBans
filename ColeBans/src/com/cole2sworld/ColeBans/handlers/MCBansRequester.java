package com.cole2sworld.ColeBans.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.cole2sworld.ColeBans.framework.BadAPIKeyException;

public class MCBansRequester extends Thread {
	private String key;
	public MCBansRequester(String key) {
		setKey(key);
	}
	public String request(String instruction) {
		try {
			URL url;
			url = new URL("http://72.10.39.172/v2/"+key);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(120000);
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(instruction);
			wr.flush();
			StringBuilder rtrn = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				rtrn.append(line);
			}
			return rtrn.toString();
		}
		catch (MalformedURLException e) {
			BadAPIKeyException ex = new BadAPIKeyException(key);
			ex.initCause(e);
		} catch (IOException e) {
			BadAPIKeyException ex = new BadAPIKeyException(key);
			ex.initCause(e);
		}
		return null;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
}
