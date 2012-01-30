package com.cole2sworld.ColeBans;

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import com.nijikokun.bukkit.Permissions.Permissions;

public class CBServerListener extends ServerListener {
	private long startTime = 0;
	public CBServerListener(long st) {
		startTime = st;
	}
    public void onPluginEnable(PluginEnableEvent event) {
    	if (Main.instance.permissionsHandler == null) {
    		Plugin permissions = Main.instance.getServer().getPluginManager().getPlugin("Permissions");
    		if (permissions != null) {
    			if (permissions.isEnabled() & permissions.getClass().getName().equals("com.nijikokun.bukkit.Permissions.Permissions")) {
    				Main.instance.permissionsHandler = ((Permissions)permissions).getHandler();
    				long newTime = System.currentTimeMillis();
    				System.out.println(GlobalConf.logPrefix+"Hooked into Nijikokun-like permissions. Took "+(newTime-startTime)+" ms to find & hook.");
    			}
    		}
    	}
    }
}
