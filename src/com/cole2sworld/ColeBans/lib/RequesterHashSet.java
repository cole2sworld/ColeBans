package com.cole2sworld.ColeBans.lib;

import java.io.Closeable;
import java.io.StringReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.Bukkit;
import org.json.util.JSONArray;
import org.json.util.JSONObject;
import org.json.util.JSONTokener;

import com.cole2sworld.ColeBans.handlers.BanData;

public class RequesterHashSet implements RequesterCallback, Closeable {
	private volatile HashSet<MCBansRequester> requesters = new HashSet<MCBansRequester>();
	private volatile HashMap<String, String> results = new HashMap<String, String>();
	private final RequesterCallback callback;
	private Future<List<BanData>> future = null;
	private boolean closed;
	public RequesterHashSet(RequesterCallback callback) {
		this.callback = callback;
	}
	public synchronized boolean add(MCBansRequester e) {
		verifyNotClosed();
		boolean result = requesters.add(e);
		e.setWaitForSize(this);
		e.start();
		return result;
	}
	public void abort() {
		verifyNotClosed();
		for (MCBansRequester requester : requesters) {
			requester.interrupt();
		}
	}
	public List<String> getResults() {
		return new ArrayList<String>(results.values());
	}
	public Set<MCBansRequester> getProcessing() {
		return requesters;
	}
	public boolean isFinished() {
		return requesters.size() == 0;
	}
	private static BanData convertLookup(String name, String lookupResult) {
		try {
			StringReader reader = new StringReader(lookupResult);
			JSONTokener tokener = new JSONTokener(reader);
			JSONObject jobject = new JSONObject(tokener);
			JSONArray array = jobject.getJSONArray("local");
			HashMap<InetAddress, String> servers = new HashMap<InetAddress, String>();
			String localReason = null;
			for (int i = 0; i<array.length(); i++) {
				Object val = array.get(i);
				if (val instanceof String) {
					String valString = (String) array.get(i);
					String[] splitString = valString.split(" .:. ");
					String barredServer = splitString[0];
					InetAddress barredServerIp = InetAddress.getByName(barredServer);
					String reason = splitString[1];
					if (barredServerIp.getHostAddress().equals(Bukkit.getIp())) {
						if (splitString.length > 1) {
							localReason = reason;
						}
					} else {
						servers.put(barredServerIp, reason);
					}
				}
			}
			BanData rtrn;
			if (localReason == null) rtrn = new BanData(name);
			else rtrn = new BanData(name, localReason);
			rtrn.setCustomData("mcbans-banned-servers", servers);
			return rtrn;
		}
		catch (Exception e) {}
		return null;
	}
	@Override
	public synchronized void requestFinished(MCBansRequester requester) {
		results.put(requester.getInstruction(), requester.getResult());
		requesters.remove(requester);
		this.notify();
		if (isFinished()) {
			callback.requestFinished(null);
			close();
			if (future != null) {
				ArrayList<BanData> data = new ArrayList<BanData>();
				for (Entry<String, String> entry : results.entrySet()) {
					data.add(convertLookup(decodeInstructionToPlayer(entry.getKey()), entry.getValue()));
				}
				future.setResult(data);
			}
		}
	}
	private String decodeInstructionToPlayer(String key) {
		String[] split = key.split("&");
		String playerRaw = null;
		for (String str : split) {
			if (str.startsWith("player=")) {
				playerRaw = str;
				break;
			}
		}
		if (playerRaw == null) return null;
		String[] plySplit = playerRaw.split("=");
		return plySplit[1];
	}
	public void use(Future<List<BanData>> future) {
		verifyNotClosed();
		this.future  = future;
	}
	/**
	 * Closes this RequesterHashSet, therefore making it no longer usable.
	 */
	@Override
	public void close() {
		if (isClosed()) return;
		abort();
		closed = true;
	}
	private void verifyNotClosed() {
		if (closed) throw new IllegalStateException("RequesterHashSet is closed");
	}
	public boolean isClosed() {
		return closed;
	}
}
