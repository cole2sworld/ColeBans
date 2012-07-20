package com.cole2sworld.colebans.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cole2sworld.colebans.ColeBansPlugin;
import com.cole2sworld.colebans.Util;
import com.cole2sworld.colebans.framework.GlobalConf;
import com.cole2sworld.colebans.framework.PlayerAlreadyBannedException;

public final class YAMLBanHandler extends BanHandler implements FileBanHandler {
	public static BanHandler onEnable() {
		final Map<String, String> data = ColeBansPlugin.getBanHandlerInitArgs();
		final File tFile = new File("./plugins/ColeBans/" + data.get("yaml"));
		try {
			tFile.createNewFile();
		} catch (final IOException e1) {
			e1.printStackTrace();
			ColeBansPlugin.LOG.severe(ColeBansPlugin.PREFIX
					+ "[YAMLBanHandler] IOException when creating file. Plugin will fail to operate correctly.");
			return null;
		}
		final YamlConfiguration tConf = new YamlConfiguration();
		try {
			tConf.load(tFile);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			ColeBansPlugin.LOG.severe(ColeBansPlugin.PREFIX
					+ "[YAMLBanHandler] FileNotFoundException when loading. Plugin will fail to operate correctly.");
			return null;
		} catch (final IOException e) {
			e.printStackTrace();
			ColeBansPlugin.LOG.severe(ColeBansPlugin.PREFIX
					+ "[YAMLBanHandler] IOException when loading. Plugin will fail to operate correctly.");
			return null;
		} catch (final InvalidConfigurationException e) {
			e.printStackTrace();
			ColeBansPlugin.LOG.severe(ColeBansPlugin.PREFIX
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
		System.out.println(ColeBansPlugin.PREFIX + "[YAMLBanHandler] Converted " + convert
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
		System.out.println(ColeBansPlugin.PREFIX + "[YAMLBanHandler] Done loading.");
	}
	
	
	@Override
	public void convert(final BanHandler handler) {
		final List<BanData> dump = dump(BanHandler.SYSTEM_ADMIN_NAME);
		for (final BanData data : dump) {
			if (GlobalConf.get("allowTempBans").asBoolean() && (data.getType() == Type.TEMPORARY)) {
				try {
					handler.tempBanPlayer(data.getVictim(), data.getTime(), data.getReason(),
							BanHandler.SYSTEM_ADMIN_NAME);
				} catch (final UnsupportedOperationException e) {
					// FIXME do something more sensible - if tempbans are
					// disabled this will cause exception spam for each player
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
	public List<BanData> dump(final String admin) {
		final List<BanData> data = new Vector<BanData>(50);
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
		ColeBansPlugin.debug("Getting ban data");
		final String permBanned = conf.getString("permBans." + player);
		long tempBanned = conf.getLong("tempBans." + player);
		ColeBansPlugin.debug("permBanned = '" + permBanned + "' tempBanned = " + tempBanned);
		if (permBanned != null) {
			ColeBansPlugin.debug("permBanned not null, returning PERMANENT data");
			return new BanData(player, permBanned);
		}
		if (GlobalConf.get("allowTempBans").asBoolean()) {
			ColeBansPlugin.debug("Temp bans allowed, continuing");
			if ((tempBanned != 0) && (tempBanned <= System.currentTimeMillis())) {
				ColeBansPlugin.debug("Temp ban is older than current time, removing");
				conf.set("permBans." + player, null);
				conf.set("tempBans." + player, null); // lazy removal, don't
														// check what is
														// actually there
				ColeBansPlugin.debug("Saving");
				save();
			}
			tempBanned = conf.getLong("tempBans." + player + ".time");
			ColeBansPlugin.debug("Reassigning tempBanned (" + tempBanned + ")");
			if (tempBanned != 0) {
				ColeBansPlugin.debug("tempBanned not zero, returning TEMPORARY data");
				return new BanData(player, tempBanned, conf.getString("tempBans." + player
						+ ".reason"));
			}
		}
		ColeBansPlugin.debug("Returning NOT_BANNED data");
		return new BanData(player);
	}
	
	@Override
	public boolean isPlayerBanned(final String player, final String admin) {
		return getBanData(player, admin).getType() != Type.NOT_BANNED;
	}
	
	@Override
	public List<String> listBannedPlayers(final String admin) {
		final List<String> list = new Vector<String>(50);
		final List<BanData> verbData = dump(admin);
		for (final BanData data : verbData) {
			list.add(data.getVictim());
		}
		return list;
	}
	
	@Override
	public void onDisable() {
		save();
		System.out.println(ColeBansPlugin.PREFIX + "[YAMLBanHandler] Saved.");
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
			ColeBansPlugin.LOG.severe(ColeBansPlugin.PREFIX
					+ "[YAMLBanHandler] IOException when saving config. Plugin will fail to operate correctly.");
		}
	}
	
	@Override
	protected void handleBanPlayer(final String player, final String reason, final String admin) {
		ColeBansPlugin.debug("banPlayer called");
		conf.set("permBans." + player, reason);
		ColeBansPlugin.debug("Saved");
		save();
	}
	
	
	@Override
	protected void handleTempBanPlayer(final String player, final long primTime,
			final String reason,
			final String admin) {
		ColeBansPlugin.debug("tempBanPlayer called");
		ColeBansPlugin.debug("Tempbanning");
		final long time = System.currentTimeMillis() + ((primTime * 60) * 1000);
		ColeBansPlugin.debug("Tempban primTime is " + primTime + ", final time is " + time);
		conf.set("tempBans." + player, null);
		final ConfigurationSection s = conf.createSection("tempBans." + player);
		s.set("time", time);
		s.set("reason", reason);
		ColeBansPlugin.debug("Saved");
		save();
	}
	
	@Override
	protected void handleUnbanPlayer(final String player, final String admin) {
		ColeBansPlugin.debug("unbanPlayer called");
		ColeBansPlugin.debug("Erasing bans");
		conf.set("permBans." + player, null);
		conf.set("tempBans." + player, null); // lazy removal, don't check what
												// is actually there
		ColeBansPlugin.debug("Saved");
		save();
	}
	
}
