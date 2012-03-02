package com.cole2sworld.ColeBans.handlers;

import java.util.HashMap;
import java.util.Vector;

import com.cole2sworld.ColeBans.GlobalConf;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.ColeBans.framework.PlayerNotBannedException;

public class MCBansBanHandler extends BanHandler {
	private String api;
	public MCBansBanHandler(String api) {
		this.api = api;
	}

	public static BanHandler onEnable(HashMap<String, String> data) {
		MCBansBanHandler handler = new MCBansBanHandler(data.get("api"));
		System.out.println(GlobalConf.logPrefix + "[MCBansBanHandler] Verifying connection...");
	
		return handler;
	}


	@Override
	public void onDisable() {
		System.out.println(api);
	}

	@Override
	public void convert(BanHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void banPlayer(String player, String reason, String admin)
			throws PlayerAlreadyBannedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tempBanPlayer(String player, long time, String admin)
			throws PlayerAlreadyBannedException, UnsupportedOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unbanPlayer(String player, String admin)
			throws PlayerNotBannedException {
		// TODO Auto-generated method stub
		
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
	public Vector<BanData> dump(String admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> listBannedPlayers(String admin) {
		// TODO Auto-generated method stub
		return null;
	}

}
