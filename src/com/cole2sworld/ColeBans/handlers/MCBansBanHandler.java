package com.cole2sworld.ColeBans.handlers;

/*import java.util.Map;
import java.util.Vector;

import com.cole2sworld.ColeBans.GlobalConf;
import com.cole2sworld.ColeBans.Main;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.ColeBans.framework.PlayerNotBannedException;
import com.cole2sworld.ColeBans.lib.MCBans;*/

public final class MCBansBanHandler extends NoOpBanHandler {
/*	private String api;
	private MCBans mcb;
	public MCBansBanHandler(String api) {
		this.api = api;
		mcb = new MCBans(api);
		System.out.println(GlobalConf.logPrefix + "[MCBansBanHandler] Verifying connection...");
		boolean valid = mcb.isValid();
		if (!valid) throw new IllegalArgumentException("API key not valid!");
		System.out.println(GlobalConf.logPrefix + "[MCBansBanHandler] API key & connection OK. Initalized.");
	}

	public static BanHandler onEnable() {
		Map<String, String> data = Main.getBanHandlerInitArgs();
		MCBansBanHandler handler = new MCBansBanHandler(data.get("api"));
		return handler;
	}

	@Override
	public void banPlayer(String player, String reason, String admin) throws PlayerAlreadyBannedException {
		if (isPlayerBanned(player, admin)) throw new PlayerAlreadyBannedException(player+" is already banned!");
		if (reason.startsWith("g ")) {
			mcb.globalBan(player, reason.substring(2), admin);
		} else {
			mcb.localBan(player, reason, admin);
		}
	}

	public synchronized final String getApiKey() {
		return api;
	}

	@Override
	public void tempBanPlayer(String player, long time, String admin) throws PlayerAlreadyBannedException, UnsupportedOperationException {
		if (isPlayerBanned(player, admin)) throw new PlayerAlreadyBannedException(player+" is already banned!");
		if (!GlobalConf.allowTempBans) throw new UnsupportedOperationException("Temp bans are disabled!");
		mcb.tempBan(player, time, admin);
	}

	@Override
	public void unbanPlayer(String player, String admin) throws PlayerNotBannedException {
		if (!isPlayerBanned(player, admin)) throw new PlayerNotBannedException(player+" is not banned!");
		mcb.
	}

	@Override
	public boolean isPlayerBanned(String player, String admin) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BanData getBanData(String player, String admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void convert(BanHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector<BanData> dump(String admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> listBannedPlayers(String admin) {
		// TODO Auto-generated method stub
		return null;
	}
*/
}
