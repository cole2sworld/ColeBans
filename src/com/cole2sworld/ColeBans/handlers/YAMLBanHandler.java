package com.cole2sworld.ColeBans.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class YAMLBanHandler extends NoOpBanHandler {
	//private File file;
	//private YamlConfiguration conf;
	public static BanHandler onEnable(Map<String, String> data) {
		File tFile = new File("./plugins/ColeBans/"+data.get("yaml"));
		YamlConfiguration tConf = new YamlConfiguration();
		try {
			tConf.load(tFile);
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new YAMLBanHandler(tFile, tConf);
	}

	public YAMLBanHandler(File file, YamlConfiguration conf) {
		//this.file = file;
		//this.conf = conf;
	}

}
