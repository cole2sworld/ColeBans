package com.cole2sworld.colebans.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
/**
 * Requester -- sends raw requests to MCBans. <b>This class should not be directly interfaced by classes, instead it should be accessed through the MCBans class.
 * @see MCBans
 */
//TODO check if this thing actually works
final class MCBansRequester extends Thread {
	private String key;
	private String instruction;
	private String result;
	private boolean finished;
	private RequesterHashSet waitFor = null;
	private ArrayList<RequesterCallback> callbacks = new ArrayList<RequesterCallback>();
	public MCBansRequester(String key, String inst) {
		super("MCBans requester thread - '"+inst+"'");
		setKey(key);
		instruction = inst;
	}
	public boolean isFinished() {
		return finished;
	}
	public String getResult() {
		if (finished) return result;
		return null;
	}
	/**
	 * Requests from MCBans with the given instruction and api key.
	 */
	@Override
	public void run() {
		if (waitFor != null) {
			try {
				if (waitFor.getProcessing().size() > 10) waitFor.wait();
			} catch (InterruptedException e) {
				finished = true;
				result = "Aborted";
				callCallbacks();
				return;
			}
		}
		OutputStreamWriter wr = null;
		try {
			URL url;
			url = new URL("http://72.10.39.172/v2/"+key);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(120000);
			conn.setDoOutput(true);
			wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(instruction);
			wr.flush();
			StringBuilder rtrn = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				rtrn.append(line);
			}
			result = rtrn.toString();
			finished = true;
			callCallbacks();
			return;
		}
		catch (MalformedURLException e) {
			//impossible
		}
		catch (IOException e) {
			//doesn't really matter
		}
		finally {
			if (wr != null)
				try {
					wr.close();
				} catch (IOException e) {
					//nothing we can do at this point
				}
		}
		result = null;
		finished = true;
		callCallbacks();
	}
	private void callCallbacks() {
		for (RequesterCallback callback : callbacks) {
			callback.requestFinished(this);
		}
		callbacks.clear();
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
	public void setWaitForSize(RequesterHashSet requesterHashSet) {
		waitFor = requesterHashSet;
	}
	public synchronized final String getInstruction() {
		return instruction;
	}
}
