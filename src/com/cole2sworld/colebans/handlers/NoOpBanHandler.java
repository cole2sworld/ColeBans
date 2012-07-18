package com.cole2sworld.colebans.handlers;

import java.util.Vector;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;

import com.cole2sworld.colebans.Main;
import com.cole2sworld.colebans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.colebans.framework.PlayerNotBannedException;

public class NoOpBanHandler extends BanHandler {
	
	public final static BanHandler onEnable() throws OperationNotSupportedException {
		noOp();
		return new NoOpBanHandler();
	}
	
	protected static final void noOp() {
		Logger.getLogger("Minecraft")
				.severe(Main.PREFIX
						+ "Using No-Operation ban handler. Please change to a finished ban handler.");
	}
	
	@Override
	public final void banPlayer(final String player, final String reason, final String admin)
			throws PlayerAlreadyBannedException {
		noOp();
	}
	
	@Override
	public final void convert(final BanHandler handler) {
		noOp();
	}
	
	@Override
	public final Vector<BanData> dump(final String admin) {
		noOp();
		return new Vector<BanData>(0);
	}
	
	@Override
	public final BanData getBanData(final String player, final String admin) {
		noOp();
		return new BanData(player);
	}
	
	@Override
	public final boolean isPlayerBanned(final String player, final String admin) {
		noOp();
		return false;
	}
	
	@Override
	public final Vector<String> listBannedPlayers(final String admin) {
		noOp();
		return new Vector<String>(0);
	}
	
	@Override
	public final void onDisable() {
		noOp();
	}
	
	@Override
	public final void tempBanPlayer(final String player, final long time, final String reason,
			final String admin) throws PlayerAlreadyBannedException, UnsupportedOperationException {
		noOp();
	}
	
	@Override
	public final void unbanPlayer(final String player, final String admin)
			throws PlayerNotBannedException {
		noOp();
	}
}
