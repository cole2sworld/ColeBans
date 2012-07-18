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

import com.cole2sworld.colebans.Main;
import com.cole2sworld.colebans.Util;
import com.cole2sworld.colebans.framework.GlobalConf;
import com.cole2sworld.colebans.handlers.BanData;

/**
 * Send requests to the MCBans service.
 * @author cole2
 *
 */
public final class MCBans implements RequesterCallback {
	private String key;
	public MCBans(String key) {
		this.key = key;
	}
	/**
	 * Makes sure the key is valid and usable.
	 * @return if the key is valid
	 */
	public boolean isValid() {
		MCBansRequester requester = new MCBansRequester(key, "");
		requester.start();
		try {
			requester.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (requester.isFinished()) {
			if (requester.getResult().contains("You need to specify a function")) {
				return true;
			}
		}
		return false;
	}
	public void localBan(String player, String reason, String admin) {
		MCBansRequester requester = new MCBansRequester(key, "exec=localBan&player="+player+"&reason="+reason+"&admin="+admin);
		requester.start();
	}
	public void globalBan(String player, String reason, String admin) {
		MCBansRequester requester = new MCBansRequester(key, "exec=globalBan&player="+player+"&reason="+reason+"&admin="+admin);
		requester.start();
	}
	public void tempBan(String player, long time, String admin) {
		MCBansRequester requester = new MCBansRequester(key, "exec=tempBan&player="+player+"&reason=Temporary Ban&admin="+admin+"&duration="+time+"&measure=Minutes");
		requester.start();
	}
	public BanData lookup(String player) {
		MCBansRequester requester = new MCBansRequester(key, "exec=playerLookup");
		requester.start();
		try {
			requester.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (requester.isFinished()) {
			try {
				StringReader reader = new StringReader(requester.getResult());
				JSONTokener tokener = new JSONTokener(reader);
				JSONObject jobject = new JSONObject(tokener);
				double reputation = jobject.getDouble("reputation");
				if (reputation < GlobalConf.get("mcbans.minRep").asInteger()) {
					return new BanData(player, "Your MCBans reputation is too low to play on this server!");
				}
				JSONArray localBans = jobject.getJSONArray("local");
				for (int i = 0; i<localBans.length(); i++) {
					Object val = localBans.get(i);
					if (val instanceof String) {
						String valString = (String) localBans.get(i);
						String[] splitString = valString.split(" .:. ");
						String ip = InetAddress.getByAddress(Util.processIp(splitString[0])).toString();
						if (ip.equals(Main.instance.server.getIp())) {
							if (splitString.length > 1) {
								return new BanData(player, splitString[1]);
							}
						}
					}
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
				return new BanData(player);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return new BanData(player);
			}
		}
		return new BanData(player);
	}
	public Future<List<BanData>> dump(String admin) {
		try {
			MCBansRequester requester = new MCBansRequester(key, "exec=backup");
			requester.start();
			try {
				requester.join();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			String a = requester.getResult();
			String[] c = a.split(",");
			ArrayList<BanData> data = new ArrayList<BanData>();
			if (!GlobalConf.get("mcbans.fullBackups").asBoolean()) {
				for (String player : c) {
					data.add(new BanData(player, "Imported from MCBans"));
				}
			} else {
				// lets see what the MCBans server can do!
				//LOWPRI Make this more like a backup, and less like a DDoS attack.
				RequesterHashSet requesters = new RequesterHashSet(this);
				for (String player : c) {
					requesters.add(new MCBansRequester(key, "exec=playerLookup&player="+player+"&admin="+admin));
				}
				Future<List<BanData>> future = new Future<List<BanData>>();
				requesters.use(future);
				return future;
			}
			return new Future<List<BanData>>(data);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void requestFinished(MCBansRequester requester) {
		if (requester != null) return; //sanity check, when we are called by a RequesterHashSet it will pass null.
		
	}
}
