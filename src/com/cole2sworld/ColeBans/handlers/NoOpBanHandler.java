package com.cole2sworld.ColeBans.handlers;

import java.util.Vector;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;

import com.cole2sworld.ColeBans.GlobalConf;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.ColeBans.framework.PlayerNotBannedException;

public class NoOpBanHandler extends BanHandler {

	public final static BanHandler onEnable() throws OperationNotSupportedException {
		noOp();
		return new NoOpBanHandler();
	}
	
	@Override
	public final void banPlayer(String player, String reason, String admin)
			throws PlayerAlreadyBannedException {
		noOp();
	}

	@Override
	public final void tempBanPlayer(String player, long time, String admin)
			throws PlayerAlreadyBannedException, UnsupportedOperationException {
		noOp();
	}

	@Override
	public final void unbanPlayer(String player, String admin)
			throws PlayerNotBannedException {
		noOp();
	}

	@Override
	public final boolean isPlayerBanned(String player, String admin) {
		noOp();
		return false;
	}

	@Override
	public final BanData getBanData(String player, String admin) {
		noOp();
		return new BanData(player);
	}

	@Override
	public final void onDisable() {
		noOp();
	}

	@Override
	public final void convert(BanHandler handler) {
		noOp();
	}

	@Override
	public final Vector<BanData> dump(String admin) {
		noOp();
		return new Vector<BanData>(0);
	}

	@Override
	public final Vector<String> listBannedPlayers(String admin) {
		noOp();
		return new Vector<String>(0);
	}

	protected static final void noOp() {
		Logger.getLogger("Minecraft").severe(GlobalConf.logPrefix+"Using No-Operation ban handler. Please change to a finished ban handler.");
	}
}
