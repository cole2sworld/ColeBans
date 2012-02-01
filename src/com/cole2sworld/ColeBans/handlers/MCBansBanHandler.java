package com.cole2sworld.ColeBans.handlers;

import com.cole2sworld.ColeBans.framework.EnableData;
import com.cole2sworld.ColeBans.framework.MethodNotSupportedException;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.ColeBans.framework.PlayerNotBannedException;

public class MCBansBanHandler extends BanHandler {
	private String api;
	public MCBansBanHandler(String api) {
		this.api = api;
	}

	@Override
	public BanHandler onEnable(EnableData data) {
		return new MCBansBanHandler(data.api);
	}


	@Override
	public void onDisable() {

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
			throws PlayerAlreadyBannedException, MethodNotSupportedException {
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

}
