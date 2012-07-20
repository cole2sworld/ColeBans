package com.cole2sworld.colebans.handlers;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;

import com.cole2sworld.colebans.ColeBansPlugin;

public class NoOpBanHandler extends BanHandler {
	
	public final static BanHandler onEnable() throws OperationNotSupportedException {
		noOp();
		return new NoOpBanHandler();
	}
	
	protected static final void noOp() {
		Logger.getLogger("Minecraft")
				.severe(ColeBansPlugin.PREFIX
						+ "Using No-Operation ban handler. Please change to a finished ban handler.");
	}
	
	@Override
	public final void convert(final BanHandler handler) {
		noOp();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cole2sworld.colebans.handlers.BanHandler#countBans(java.lang.String)
	 */
	@Override
	public long countBans(final String admin) {
		noOp();
		return -1;
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
	public final List<String> listBannedPlayers(final String admin) {
		noOp();
		return new Vector<String>(0);
	}
	
	@Override
	public final void onDisable() {
		noOp();
	}
	
	@Override
	protected final void handleBanPlayer(final String player, final String reason,
			final String admin) {
		noOp();
	}
	
	@Override
	protected final void handleTempBanPlayer(final String player, final long time,
			final String reason,
			final String admin) {
		noOp();
	}
	
	@Override
	protected final void handleUnbanPlayer(final String player, final String admin) {
		noOp();
	}
}
