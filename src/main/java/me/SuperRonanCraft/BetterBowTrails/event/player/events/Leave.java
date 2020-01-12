package me.SuperRonanCraft.BetterBowTrails.event.player.events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.SuperRonanCraft.BetterBowTrails.Main;
import me.SuperRonanCraft.BetterBowTrails.event.arrow.Arrow;

public class Leave {

    public void leave(PlayerQuitEvent e) {
        Main.getInstance().getPhd().clearItems(e.getPlayer());
        Player player = e.getPlayer();
        Arrow.online.put(player, false);
        Main pl = Main.getInstance();
        if (pl.files.setup.get(player) != null && pl.files.setup.get(player)) {
            pl.files.fileconfig.remove(player);
            pl.files.file.remove(player);
            pl.files.setup.put(player, null);
        }
    }
}
