package me.SuperRonanCraft.BetterBowTrails.event.player.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.SuperRonanCraft.BetterBowTrails.Main;
import me.SuperRonanCraft.BetterBowTrails.event.arrow.Arrow;

public class Listeners implements Listener {

    private boolean click, join;
    private Leave leave = new Leave();
    private Arrow arrow;

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public void registerClick() {
        if (!click) {
            click = true;
            Bukkit.getPluginManager().registerEvents(new Click(), Main.getInstance());
        }
    }

    public void registerJoin() {
        if (!join) {
            join = true;
            Bukkit.getPluginManager().registerEvents(new Join(), Main.getInstance());
        }
    }

    @EventHandler
    public void leave(PlayerQuitEvent e) {
        leave.leave(e);
    }

    @EventHandler
    public void arrow(EntityShootBowEvent e) {
        if (arrow == null)
            arrow = new Arrow(Main.getInstance());
        arrow.arrow(e);
    }
}
