package com.cole2sworld.ColeBans.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.ColeBans.framework.PlayerNotBannedException;

public class YAMLBanHandler extends BanHandler {
	private File file;
	private YamlConfiguration conf;
	@Override
	public BanHandler onEnable(HashMap<String, String> data) {
		file = new File("./plugins/ColeBans/"+data.get("yaml"));
		conf = new YamlConfiguration();
		try {
			conf.load(file);
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
