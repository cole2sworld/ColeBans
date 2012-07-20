package com.cole2sworld.colebans.lib;

import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.util.JSONArray;
import org.json.util.JSONException;
import org.json.util.JSONObject;
import org.json.util.JSONTokener;

import com.cole2sworld.colebans.ColeBansPlugin;
import com.cole2sworld.colebans.Util;
import com.cole2sworld.colebans.framework.GlobalConf;
import com.cole2sworld.colebans.handlers.BanData;

/**
 * Send requests to the MCBans service.
 * 
 * @author cole2
 * 
 */
public final class MCBans implements RequesterCallback {
	private final String	key;
	
	public MCBans(final String k) {
		key = k;
	}
	
	public Future<List<BanData>> dump(final String admin) {
		try {
			final MCBansRequester requester = new MCBansRequester(key, "exec=backup");
			requester.start();
			try {
				requester.join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			final String a = requester.getResult();
			final String[] c = a.split(",");
			final List<BanData> data = new ArrayList<BanData>();
			if (!GlobalConf.get("mcbans.fullBackups").asBoolean()) {
				for (final String player : c) {
					data.add(new BanData(player, "Imported from MCBans"));
				}
			} else {
				// lets see what the MCBans server can do!
				// LOWPRI Make this more like a backup, and less like a DDoS
				// attack.
				final RequesterHashSet requesters = new RequesterHashSet(this);
				for (final String player : c) {
					requesters.add(new MCBansRequester(key, "exec=playerLookup&player=" + player
							+ "&admin=" + admin));
				}
				final Future<List<BanData>> future = new Future<List<BanData>>();
				requesters.use(future);
				return future;
			}
			return new Future<List<BanData>>(data);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void globalBan(final String player, final String reason, final String admin) {
		final MCBansRequester requester = new MCBansRequester(key, "exec=globalBan&player="
				+ player + "&reason=" + reason + "&admin=" + admin);
		requester.start();
	}
	
	/**
	 * Makes sure the key is valid and usable.
	 * 
	 * @return if the key is valid
	 */
	public boolean isValid() {
		final MCBansRequester requester = new MCBansRequester(key, "");
		requester.start();
		try {
			requester.join();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		if (requester.isFinished()) {
			if (requester.getResult().contains("You need to specify a function")) return true;
		}
		return false;
	}
	
	public void localBan(final String player, final String reason, final String admin) {
		final MCBansRequester requester = new MCBansRequester(key, "exec=localBan&player=" + player
				+ "&reason=" + reason + "&admin=" + admin);
		requester.start();
	}
	
	public BanData lookup(final String player) {
		final MCBansRequester requester = new MCBansRequester(key, "exec=playerLookup");
		requester.start();
		try {
			requester.join();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		if (requester.isFinished()) {
			try {
				final StringReader reader = new StringReader(requester.getResult());
				final JSONTokener tokener = new JSONTokener(reader);
				final JSONObject jobject = new JSONObject(tokener);
				final double reputation = jobject.getDouble("reputation");
				if (reputation < GlobalConf.get("mcbans.minRep").asInteger())
					return new BanData(player,
							"Your MCBans reputation is too low to play on this server!");
				final JSONArray localBans = jobject.getJSONArray("local");
				for (int i = 0; i < localBans.length(); i++) {
					final Object val = localBans.get(i);
					if (val instanceof String) {
						final String valString = (String) localBans.get(i);
						final String[] splitString = valString.split(" .:. ");
						final String ip = InetAddress.getByAddress(Util.processIp(splitString[0]))
								.toString();
						if (ip.equals(ColeBansPlugin.instance.server.getIp())) {
							if (splitString.length > 1) return new BanData(player, splitString[1]);
						}
					}
				}
			} catch (final JSONException e) {
				e.printStackTrace();
				return new BanData(player);
			} catch (final UnknownHostException e) {
				e.printStackTrace();
				return new BanData(player);
			}
		}
		return new BanData(player);
	}
	
	@Override
	public void requestFinished(final MCBansRequester requester) {
		if (requester != null) return; // sanity check, when we are called by a
										// RequesterHashSet it will pass null.
			
	}
	
	public void tempBan(final String player, final long time, final String reason,
			final String admin) {
		final MCBansRequester requester = new MCBansRequester(key, "exec=tempBan&player=" + player
				+ "&reason=" + reason + "&admin=" + admin + "&duration=" + time
				+ "&measure=Minutes");
		requester.start();
	}
}
