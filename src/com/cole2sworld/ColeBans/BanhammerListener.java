package com.cole2sworld.ColeBans;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import com.cole2sworld.ColeBans.framework.PermissionSet;
import com.cole2sworld.ColeBans.framework.PlayerAlreadyBannedException;
import com.cole2sworld.ColeBans.framework.PlayerOfflineException;
/**
 * Listener for banhammers.
 * @author cole2
 * @since v5 Elderberry
 */
public class BanhammerListener implements Listener {
	public static class ExplosionRunnable implements Runnable {
		private final Location location;
		public ExplosionRunnable(Location loc) {
			location = loc;
		}
		public void run() {
			location.getWorld().createExplosion(location, 0);
		}
	}
	public static class LightningRunnable implements Runnable {
		private final Location location;
		public LightningRunnable(Location loc) {
			location = loc;
		}
		public void run() {
			location.getWorld().strikeLightningEffect(location);
		}
	}
	public static class BanRunnable implements Runnable {
		private final String player;
		private final String admin;
		public BanRunnable(String ply, String adm) {
			player = ply;
			admin = adm;
		}
		public void run() {
			try {
				Main.instance.banHandler.banPlayer(player, GlobalConf.get("banhammer.reason").asString(), admin);
				LogManager.addEntry(LogManager.Type.BANHAMMER_BAN, admin, player);
			} catch (PlayerAlreadyBannedException e) {
				//impossibru
			}
		}
	}
	public static class KickRunnable implements Runnable {
		private final String player;
		private final String admin;
		public KickRunnable(String ply, String adm) {
			player = ply;
			admin = adm;
		}
		public void run() {
			try {
				Main.instance.kickPlayer(player, GlobalConf.get("banhammer.reason").asString());
				LogManager.addEntry(LogManager.Type.BANHAMMER_KICK, admin, player);
			} catch (PlayerOfflineException e) {
				//impossibru
			}
		}
	}
	public static enum BanhammerAction {
		BAN, KICK, NONE;
	}
	@EventHandler (priority=EventPriority.LOWEST)
	public void onAttack(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player attacker = (Player) event.getDamager();
			if (event.getEntity() instanceof Player) {
				Player victim = (Player) event.getEntity();
				PermissionSet pset = new PermissionSet(attacker);
				ItemStack held = attacker.getItemInHand();
				if (held == null) return;
				if (held.getType() != Material.valueOf(GlobalConf.get("banhammer.type").asString())) return;
				if (!pset.canBanhammer) return;
				BanhammerAction action = BanhammerAction.valueOf(GlobalConf.get("banhammer.leftClickAction").asString());
				if (action == BanhammerAction.NONE) return;
				RestrictionManager.freeze(victim);
				BukkitScheduler sched = Bukkit.getScheduler();
				sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation()), 1);
				sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 1, 0)), 2);
				sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 2, 0)), 3);
				sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 3, 0)), 4);
				sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 4, 0)), 5);
				sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 5, 0)), 6);
				sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 6, 0)), 7);
				sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 7, 0)), 8);
				sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 8, 0)), 9);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 1);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 1);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 2);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 2);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 3);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 3);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 4);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 4);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 5);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 5);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 6);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 6);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 7);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 7);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 8);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 8);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 9);
				sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 9);
				if (action == BanhammerAction.BAN) {
					sched.scheduleSyncDelayedTask(Main.instance, new BanRunnable(victim.getName(), attacker.getName()), 11);
				} else {
					sched.scheduleSyncDelayedTask(Main.instance, new KickRunnable(victim.getName(), attacker.getName()), 11);
				}
			}
		}
	}
	private Location getRandomAround(Location loc) {
		Random rand = new Random(System.currentTimeMillis());
		loc = loc.clone();
		loc.add(rand.nextDouble()*(rand.nextBoolean() ? -1D : 1D), 0, rand.nextDouble()*(rand.nextBoolean() ? -1D : 1D));
		return loc;
	}
	@EventHandler (priority=EventPriority.LOWEST)
	public void onInteract(PlayerInteractEntityEvent event) {
		Player attacker = event.getPlayer();
		if (event.getRightClicked() instanceof Player) {
			Player victim = (Player) event.getRightClicked();
			PermissionSet pset = new PermissionSet(attacker);
			ItemStack held = attacker.getItemInHand();
			if (held == null) return;
			if (held.getType() != Material.valueOf(GlobalConf.get("banhammer.type").asString())) return;
			if (!pset.canBanhammer) return;
			BanhammerAction action = BanhammerAction.valueOf(GlobalConf.get("banhammer.rightClickAction").asString());
			if (action == BanhammerAction.NONE) return;
			RestrictionManager.freeze(victim);
			BukkitScheduler sched = Bukkit.getScheduler();
			sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation()), 1);
			sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 1, 0)), 2);
			sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 2, 0)), 3);
			sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 3, 0)), 4);
			sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 4, 0)), 5);
			sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 5, 0)), 6);
			sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 6, 0)), 7);
			sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 7, 0)), 8);
			sched.scheduleSyncDelayedTask(Main.instance, new ExplosionRunnable(victim.getLocation().add(0, 8, 0)), 9);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 1);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 1);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 2);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 2);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 3);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 3);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 4);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 4);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 5);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 5);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 6);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 6);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 7);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 7);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 8);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 8);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 9);
			sched.scheduleSyncDelayedTask(Main.instance, new LightningRunnable(getRandomAround(victim.getLocation())), 9);
			if (action == BanhammerAction.BAN) {
				sched.scheduleSyncDelayedTask(Main.instance, new BanRunnable(victim.getName(), attacker.getName()), 11);
			} else {
				sched.scheduleSyncDelayedTask(Main.instance, new KickRunnable(victim.getName(), attacker.getName()), 11);
			}
		}
	}
}
