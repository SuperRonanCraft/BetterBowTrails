package me.SuperRonanCraft.BetterBowTrails.event.arrow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.SuperRonanCraft.BetterBowTrails.Main;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;

public class Arrow {

	// public static HashMap<Projectile, Player> arrows = new
	// HashMap<Projectile, Player>();
	// public static HashMap<Player, FileConfiguration> yamls = new
	// HashMap<Player, FileConfiguration>();
	public static HashMap<Player, Boolean> online = new HashMap<Player, Boolean>();
	public static HashMap<Player, List<Projectile>> arrows = new HashMap<Player, List<Projectile>>();
	public static List<Projectile> global = new ArrayList<>();
	// static HashMap<Projectile, FileConfiguration> files = new
	// HashMap<Projectile, FileConfiguration>();
	// Set<Projectile> removed = new HashSet<Projectile>();
	// Projectile removed;
	Main pl;

	public Arrow(Main pl) {
		this.pl = pl;
	}

	public void arrow(EntityShootBowEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		Player player = (Player) e.getEntity();
		if (!pl.getPerms().getUse(player) || !worlds(player))
			return;
		List<Projectile> ps;
		if (arrows.get(player) != null)
			ps = arrows.get(player);
		else
			ps = new ArrayList<Projectile>();
		if (pl.files.setup.get(player) == null)
			pl.files.setupFiles(player);
		if (maximums(player)) {
			ps.add((Projectile) e.getProjectile());
			arrows.put(player, ps);
			global.add((Projectile) e.getProjectile());
			online.put(player, true);
			new Particles(pl, player, (Projectile) e.getProjectile());
		}
	}

	private boolean maximums(Player player) {
		int globalMax = pl.getConfig().getInt("Settings.MaxGlobalTrails");
		int localMax = pl.getConfig().getInt("Player.MaxTrails");
		int donorMax = pl.getConfig().getInt("Player.Donator.MaxTrails");
		if (arrows.get(player) == null)
			return true;
		if (globalMax > 0 && global.size() >= globalMax)
			return false;
		else if (pl.getPerms().getDonor(player) && donorMax > 0 && arrows.get(player).size() >= donorMax)
			return false;
		else if (!pl.getPerms().getDonor(player) && localMax > 0 && arrows.get(player).size() >= localMax)
			return false;
		return true;
	}

	private boolean worlds(Player player) {
		List<String> worlds = pl.getConfig().getStringList("Settings.DisabledWorlds");
		if (!worlds.isEmpty())
			for (String world : worlds)
				if (world.equals(player.getWorld().getName()))
					return false;
		return true;
	}
}