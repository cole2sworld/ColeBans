package com.cole2sworld.ColeBans.handlers;

import java.util.Vector;
import java.util.logging.Logger;

import com.cole2sworld.ColeBans.GlobalConf;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.ColeBans.framework.PlayerNotBannedException;

public class NoOpBanHandler extends BanHandler {

	@Override
	public final void banPlayer(String player, String reason, String admin)
			throws PlayerAlreadyBannedException {
		NoOp();
	}

	@Override
	public final void tempBanPlayer(String player, long time, String admin)
			throws PlayerAlreadyBannedException, UnsupportedOperationException {
		NoOp();
	}

	@Override
	public final void unbanPlayer(String player, String admin)
			throws PlayerNotBannedException {
		NoOp();
	}

	@Override
	public final boolean isPlayerBanned(String player, String admin) {
		NoOp();
		return false;
	}

	@Override
	public final BanData getBanData(String player, String admin) {
		NoOp();
		return new BanData(player);
	}

	@Override
	public final void onDisable() {
		NoOp();
	}

	@Override
	public final void convert(BanHandler handler) {
		NoOp();
	}

	@Override
	public final Vector<BanData> dump(String admin) {
		NoOp();
		return new Vector<BanData>(0);
	}

	@Override
	public final Vector<String> listBannedPlayers(String admin) {
		NoOp();
		return new Vector<String>(0);
	}

	private final void NoOp() {
		Logger.getLogger("Minecraft").severe(GlobalConf.logPrefix+"Using No-Operation ban handler. Please change to a finished ban handler.");
	}
}
