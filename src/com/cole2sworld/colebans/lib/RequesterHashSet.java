package com.cole2sworld.colebans.lib;

import java.io.Closeable;
import java.io.StringReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.json.util.JSONArray;
import org.json.util.JSONObject;
import org.json.util.JSONTokener;

import org.bukkit.Bukkit;

import com.cole2sworld.colebans.handlers.BanData;

public class RequesterHashSet implements RequesterCallback, Closeable {
	private static BanData convertLookup(final String name, final String lookupResult) {
		try {
			final StringReader reader = new StringReader(lookupResult);
			final JSONTokener tokener = new JSONTokener(reader);
			final JSONObject jobject = new JSONObject(tokener);
			final JSONArray array = jobject.getJSONArray("local");
			final HashMap<InetAddress, String> servers = new HashMap<InetAddress, String>();
			String localReason = null;
			for (int i = 0; i < array.length(); i++) {
				final Object val = array.get(i);
				if (val instanceof String) {
					final String valString = (String) array.get(i);
					final String[] splitString = valString.split(" .:. ");
					final String barredServer = splitString[0];
					final InetAddress barredServerIp = InetAddress.getByName(barredServer);
					final String reason = splitString[1];
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
			if (localReason == null) {
				rtrn = new BanData(name);
			} else {
				rtrn = new BanData(name, localReason);
			}
			rtrn.setCustomData("mcbans-banned-servers", servers);
			return rtrn;
		} catch (final Exception e) {
			// does not matter really
		}
		return null;
	}
	
	private static String decodeInstructionToPlayer(final String key) {
		final String[] split = key.split("&");
		String playerRaw = null;
		for (final String str : split) {
			if (str.startsWith("player=")) {
				playerRaw = str;
				break;
			}
		}
		if (playerRaw == null) return null;
		final String[] plySplit = playerRaw.split("=");
		return plySplit[1];
	}
	
	private volatile HashSet<MCBansRequester>	requesters	= new HashSet<MCBansRequester>();
	private volatile HashMap<String, String>	results		= new HashMap<String, String>();
	private final RequesterCallback				callback;
	private Future<List<BanData>>				future		= null;
	private boolean								closed;
	
	public RequesterHashSet(final RequesterCallback callback) {
		this.callback = callback;
	}
	
	public void abort() {
		verifyNotClosed();
		for (final MCBansRequester requester : requesters) {
			requester.interrupt();
		}
	}
	
	public synchronized boolean add(final MCBansRequester e) {
		verifyNotClosed();
		final boolean result = requesters.add(e);
		e.setWaitForSize(this);
		e.start();
		return result;
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
	
	public Set<MCBansRequester> getProcessing() {
		return requesters;
	}
	
	public List<String> getResults() {
		return new ArrayList<String>(results.values());
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public boolean isFinished() {
		return requesters.size() == 0;
	}
	
	@Override
	public synchronized void requestFinished(final MCBansRequester requester) {
		results.put(requester.getInstruction(), requester.getResult());
		requesters.remove(requester);
		notify();
		if (isFinished()) {
			callback.requestFinished(null);
			close();
			if (future != null) {
				final ArrayList<BanData> data = new ArrayList<BanData>();
				for (final Entry<String, String> entry : results.entrySet()) {
					data.add(convertLookup(decodeInstructionToPlayer(entry.getKey()),
							entry.getValue()));
				}
				future.setResult(data);
			}
		}
	}
	
	public void use(final Future<List<BanData>> newFuture) {
		verifyNotClosed();
		future = newFuture;
	}
	
	private void verifyNotClosed() {
		if (closed) throw new IllegalStateException("RequesterHashSet is closed");
	}
}
