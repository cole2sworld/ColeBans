package com.cole2sworld.ColeBans.handlers;

import java.util.Map;
import com.cole2sworld.ColeBans.GlobalConf;

public class MCBansBanHandler extends NoOpBanHandler {
	//private String api;
	public MCBansBanHandler(String api) {
		//this.api = api;
	}

	public static BanHandler onEnable(Map<String, String> data) {
		MCBansBanHandler handler = new MCBansBanHandler(data.get("api"));
		System.out.println(GlobalConf.logPrefix + "[MCBansBanHandler] Verifying connection...");
	
		return handler;
	}

}
