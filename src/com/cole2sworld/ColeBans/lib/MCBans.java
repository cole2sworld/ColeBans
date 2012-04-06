package com.cole2sworld.ColeBans.lib;

import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.util.JSONArray;
import org.json.util.JSONException;
import org.json.util.JSONObject;
import org.json.util.JSONTokener;

import com.cole2sworld.ColeBans.GlobalConf;
import com.cole2sworld.ColeBans.Main;
import com.cole2sworld.ColeBans.handlers.BanData;

/**
 * Send requests to the MCBans service.
 * @author cole2
 *
 */
public final class MCBans {
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
				if (reputation < GlobalConf.MCBans.minRep) {
					return new BanData(player, "Your MCBans reputation is too low to play on this server!");
				}
				JSONArray localBans = jobject.getJSONArray("local");
				for (int i = 0; i<localBans.length(); i++) {
					Object val = localBans.get(i);
					if (val instanceof String) {
						String valString = (String) localBans.get(i);
						String[] splitString = valString.split(" .:. ");
						String ip = InetAddress.getByName(splitString[0]).toString();
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
}
