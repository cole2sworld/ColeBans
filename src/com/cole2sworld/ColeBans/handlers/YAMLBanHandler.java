package com.cole2sworld.ColeBans.handlers;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.ColeBans.framework.PlayerNotBannedException;

public class YAMLBanHandler extends BanHandler {
	private File file;
	@Override
	public BanHandler onEnable(HashMap<String, String> data) {
		
		return null;
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

}
