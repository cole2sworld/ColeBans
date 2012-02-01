package com.cole2sworld.ColeBans.lib;

public class MCBans {
	private String key;
	public MCBans(String key) {
		this.key = key;
	}
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
	public void localBan(String player, String admin) {
		
	}
}
