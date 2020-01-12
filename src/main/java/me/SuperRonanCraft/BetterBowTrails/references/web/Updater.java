package me.SuperRonanCraft.BetterBowTrails.references.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;

import me.SuperRonanCraft.BetterBowTrails.Main;

public class Updater {
	public static String updatedVersion;

	public Updater(Main pl) {
		try {
			URLConnection con = new URL(getUrl() + project()).openConnection();
			updatedVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
		} catch (Exception ex) {
			Bukkit.getConsoleSender().sendMessage("[AdvancedCustomMenu] Failed to check for an update on spigot");
			updatedVersion = pl.getDescription().getVersion();
		}
	}

	private String getUrl() {
		return "https://api.spigotmc.org/legacy/update.php?resource=";
	}

	private String project() {
		return "38351";
	}
}
