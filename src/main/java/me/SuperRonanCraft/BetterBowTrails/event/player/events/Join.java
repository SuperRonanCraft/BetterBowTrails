package me.SuperRonanCraft.BetterBowTrails.event.player.events;

import me.SuperRonanCraft.BetterBowTrails.references.web.Updater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.SuperRonanCraft.BetterBowTrails.Main;

public class Join implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        if (Main.getInstance().getPerms().getUpdate(e.getPlayer()) && !Main.getInstance().getDescription().getVersion().equals(Updater.updatedVersion) && Updater.updatedVersion != null)
            e.getPlayer().sendMessage(Main.getInstance().getText().color(Main.getInstance().getText().getPrefix() +
                    "&7There is currently an update for " + "&6BetterBowTrails &7version &e#" + Updater.updatedVersion + " &7you have version &e#" + Main.getInstance().getDescription().getVersion()));
    }

}
