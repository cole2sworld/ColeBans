package com.cole2sworld.colebans.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cole2sworld.colebans.Main;
import com.cole2sworld.colebans.Util;
import com.cole2sworld.colebans.framework.GlobalConf;
import com.cole2sworld.colebans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.colebans.framework.PlayerNotBannedException;

public final class YAMLBanHandler extends BanHandler implements FileBanHandler {
	public static BanHandler onEnable() {
		final Map<String, String> data = Main.getBanHandlerInitArgs();
		final File tFile = new File("./plugins/ColeBans/" + data.get("yaml"));
		try {
			tFile.createNewFile();
		} catch (final IOException e1) {
			e1.printStackTrace();
			Main.LOG.severe(Main.PREFIX
					+ "[YAMLBanHandler] IOException when creating file. Plugin will fail to operate correctly.");
			return null;
		}
		final YamlConfiguration tConf = new YamlConfiguration();
		try {
			tConf.load(tFile);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			Main.LOG.severe(Main.PREFIX
					+ "[YAMLBanHandler] FileNotFoundException when loading. Plugin will fail to operate correctly.");
			return null;
		} catch (final IOException e) {
			e.printStackTrace();
			Main.LOG.severe(Main.PREFIX
					+ "[YAMLBanHandler] IOException when loading. Plugin will fail to operate correctly.");
			return null;
		} catch (final InvalidConfigurationException e) {
			e.printStackTrace();
			Main.LOG.severe(Main.PREFIX
					+ "[YAMLBanHandler] InvalidConfigurationException when loading. Plugin will fail to operate correctly.");
			return null;
		}
		int convert = 0;
		for (final Entry<String, Object> entry : tConf.getConfigurationSection("tempBans")
				.getValues(false).entrySet()) {
			if (entry.getValue() instanceof Long) {
				final long time = (Long) entry.getValue();
				tConf.set(entry.getKey(), null);
				final ConfigurationSection s = tConf.createSection("tempBans." + entry.getKey());
				s.set("time", time);
				s.set("reason", "Temporary Ban");
				convert++;
			}
		}
		System.out.println(Main.PREFIX + "[YAMLBanHandler] Converted " + convert
				+ " temporary ban "
				+ (Util.getPlural(convert, true).equals("s") ? "entries" : "entry") + ".");
		return new YAMLBanHandler(tFile, tConf, convert > 0);
	}
	
	private final File				file;
	private final YamlConfiguration	conf;
	
	public YAMLBanHandler(final File file, final YamlConfiguration conf, final boolean save) {
		this.file = file;
		this.conf = conf;
		if (save) {
			save();
		}
		System.out.println(Main.PREFIX + "[YAMLBanHandler] Done loading.");
	}
	
	@Override
	public void banPlayer(final String player, final String reason, final String admin)
			throws PlayerAlreadyBannedException {
		Main.debug("banPlayer called");
		if (isPlayerBanned(player, admin))
			throw new PlayerAlreadyBannedException(player + " is already banned!");
		Main.debug("Not already banned");
		conf.set("permBans." + player, reason);
		Main.debug("Saved");
		save();
	}
	
	@Override
	public void convert(final BanHandler handler) {
		final Vector<BanData> dump = dump(BanHandler.SYSTEM_ADMIN_NAME);
		for (final BanData data : dump) {
			if (GlobalConf.get("allowTempBans").asBoolean() && (data.getType() == Type.TEMPORARY)) {
				try {
					handler.tempBanPlayer(data.getVictim(), data.getTime(), data.getReason(),
							BanHandler.SYSTEM_ADMIN_NAME);
				} catch (final UnsupportedOperationException e) {
					// just skip it, tempbans are disabled
				} catch (final PlayerAlreadyBannedException e) {
					// just skip it, they are already banned in the target
				}
			} else if (data.getType() == Type.PERMANENT) {
				try {
					handler.banPlayer(data.getVictim(), data.getReason(),
							BanHandler.SYSTEM_ADMIN_NAME);
				} catch (final PlayerAlreadyBannedException e) {
					// just skip it, they are already banned in the target
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cole2sworld.colebans.handlers.BanHandler#countBans(java.lang.String)
	 */
	@Override
	public long countBans(final String admin) {
		return conf.getConfigurationSection("permBans").getKeys(false).size()
				+ conf.getConfigurationSection("tempBans").getKeys(false).size();
	}
	
	@Override
	public Vector<BanData> dump(final String admin) {
		final Vector<BanData> data = new Vector<BanData>(50);
		final ConfigurationSection temp = conf.getConfigurationSection("tempBans");
		final ConfigurationSection perm = conf.getConfigurationSection("permBans");
		if (temp != null) {
			final Map<String, Object> tempBans = temp.getValues(true);
			final Set<String> keySet = tempBans.keySet();
			for (final String key : keySet) {
				final Object time = tempBans.get(key);
				if (time instanceof ConfigurationSection) {
					data.add(new BanData(key, ((ConfigurationSection) time).getLong("time"),
							((ConfigurationSection) time).getString("reason")));
				}
			}
		}
		if (perm != null) {
			final Map<String, Object> permBans = perm.getValues(true);
			final Set<String> keySet = permBans.keySet();
			for (final String key : keySet) {
				final Object reason = permBans.get(key);
				if (reason instanceof String) {
					data.add(new BanData(key, (String) reason));
				}
			}
		}
		return data;
	}
	
	@Override
	public BanData getBanData(final String player, final String admin) {
		Main.debug("Getting ban data");
		final String permBanned = conf.getString("permBans." + player);
		long tempBanned = conf.getLong("tempBans." + player);
		Main.debug("permBanned = '" + permBanned + "' tempBanned = " + tempBanned);
		if (permBanned != null) {
			Main.debug("permBanned not null, returning PERMANENT data");
			return new BanData(player, permBanned);
		}
		if (GlobalConf.get("allowTempBans").asBoolean()) {
			Main.debug("Temp bans allowed, continuing");
			if ((tempBanned != 0) && (tempBanned <= System.currentTimeMillis())) {
				Main.debug("Temp ban is older than current time, removing");
				conf.set("permBans." + player, null);
				conf.set("tempBans." + player, null); // lazy removal, don't
														// check what is
														// actually there
				Main.debug("Saving");
				save();
			}
			tempBanned = conf.getLong("tempBans." + player + ".time");
			Main.debug("Reassigning tempBanned (" + tempBanned + ")");
			if (tempBanned != 0) {
				Main.debug("tempBanned not zero, returning TEMPORARY data");
				return new BanData(player, tempBanned, conf.getString("tempBans." + player
						+ ".reason"));
			}
		}
		Main.debug("Returning NOT_BANNED data");
		return new BanData(player);
	}
	
	@Override
	public boolean isPlayerBanned(final String player, final String admin) {
		return getBanData(player, admin).getType() != Type.NOT_BANNED;
	}
	
	@Override
	public Vector<String> listBannedPlayers(final String admin) {
		final Vector<String> list = new Vector<String>(50);
		final Vector<BanData> verbData = dump(admin);
		for (final BanData data : verbData) {
			list.add(data.getVictim());
		}
		return list;
	}
	
	@Override
	public void onDisable() {
		save();
		System.out.println(Main.PREFIX + "[YAMLBanHandler] Saved.");
	}
	
	@Override
	public void reload() {
		try {
			conf.load(file);
		} catch (final FileNotFoundException e) {
			// so what
		} catch (final IOException e) {
			// meh
		} catch (final InvalidConfigurationException e) {
			// then how did this work before
		}
	}
	
	@Override
	public void save() {
		try {
			conf.save(file);
		} catch (final IOException e) {
			e.printStackTrace();
			Main.LOG.severe(Main.PREFIX
					+ "[YAMLBanHandler] IOException when saving config. Plugin will fail to operate correctly.");
		}
	}
	
	@Override
	public void tempBanPlayer(final String player, final long primTime, final String reason,
			final String admin) throws PlayerAlreadyBannedException, UnsupportedOperationException {
		Main.debug("tempBanPlayer called");
		if (!GlobalConf.get("allowTempBans").asBoolean())
			throw new UnsupportedOperationException("Temp bans are disabled!");
		Main.debug("TempBans enabled");
		if (isPlayerBanned(player, admin))
			throw new PlayerAlreadyBannedException(player + " is already banned!");
		Main.debug("Tempbanning");
		final long time = System.currentTimeMillis() + ((primTime * 60) * 1000);
		Main.debug("Tempban primTime is " + primTime + ", final time is " + time);
		conf.set("tempBans." + player, null);
		final ConfigurationSection s = conf.createSection("tempBans." + player);
		s.set("time", time);
		s.set("reason", reason);
		Main.debug("Saved");
		save();
	}
	
	@Override
	public void unbanPlayer(final String player, final String admin)
			throws PlayerNotBannedException {
		Main.debug("unbanPlayer called");
		if (!isPlayerBanned(player, admin))
			throw new PlayerNotBannedException(player + " is not banned!");
		Main.debug("Erasing bans");
		conf.set("permBans." + player, null);
		conf.set("tempBans." + player, null); // lazy removal, don't check what
												// is actually there
		Main.debug("Saved");
		save();
	}
	
}
