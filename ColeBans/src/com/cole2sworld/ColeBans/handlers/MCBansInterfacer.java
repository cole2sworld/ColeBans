package com.cole2sworld.ColeBans.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.cole2sworld.ColeBans.framework.BadAPIKeyException;

public class MCBansInterfacer {
	private String key;
	protected String raw(String instruction) throws BadAPIKeyException {
		try {
			URL url;
			url = new URL("http://72.10.39.172/v2/736ae69750589d85cacce1e7f63a4849ab28517e");
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(40000);
			conn.setReadTimeout(40000);
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
}
