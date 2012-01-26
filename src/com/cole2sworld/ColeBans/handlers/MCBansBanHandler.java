package com.cole2sworld.ColeBans.handlers;

import java.io.File;

import com.cole2sworld.ColeBans.framework.MethodNotSupportedException;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.ColeBans.framework.PlayerNotBannedException;

public class MCBansBanHandler extends BanHandler {
	
	public MCBansBanHandler(String api) {

	}

	@Override
	public BanHandler onEnable(String username, String password, String host,
			String port, String prefix, String db, File yaml, File json,
			String api) {
		return new MCBansBanHandler(api);
	}

	@Override
	public void banPlayer(String player, String reason) throws PlayerAlreadyBannedException {

	}

	@Override
	public void tempBanPlayer(String player, long time) throws PlayerAlreadyBannedException, MethodNotSupportedException {

	}

	@Override
	public void unbanPlayer(String player) throws PlayerNotBannedException {

	}

	@Override
	public boolean isPlayerBanned(String player) {
		return false;
	}

	@Override
	public BanData getBanData(String player) {
		return null;
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void convert(BanHandler handler) {
		// TODO Auto-generated method stub
		
	}

}
