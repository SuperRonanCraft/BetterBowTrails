package me.SuperRonanCraft.BetterBowTrails.inventories;

import java.util.List;

import me.SuperRonanCraft.BetterBowTrails.Main;
import me.SuperRonanCraft.BetterBowTrails.event.player.events.Click;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Confirm {
    private Main pl;
    private String invName, acceptName, denyName, sec = "Confirm";
    private List<String> acceptLore, denyLore;
    private String[] acceptItem, denyItem;
    private int acceptSlot, denySlot;
    private int slots;

    public Confirm(Main pl) {
        this.pl = pl;
    }

    private void loadItems() {
        ConfigurationSection settings = pl.getConfig().getConfigurationSection("Economy.Confirm");
        invName = settings.getString("Title");
        acceptName = settings.getString("Accept.Name");
        acceptItem = settings.getString("Accept.Item").split(":");
        acceptLore = settings.getStringList("Accept.Lore");
        acceptSlot = settings.getInt("Accept.Slot");
        denyName = settings.getString("Deny.Name");
        denyItem = settings.getString("Deny.Item").split(":");
        denyLore = settings.getStringList("Deny.Lore");
        denySlot = settings.getInt("Deny.Slot");
        slots = settings.getInt("Rows") * 9;
    }

    public void createMenu(Player player) {
        if (invName == null)
            loadItems();
        Inventory inv = Bukkit.getServer().createInventory(null, slots, pl.getText().color(invName));
        List<String> acceptLore = this.acceptLore;
        for (int i = 0; i < acceptLore.size(); i++) {
            if (acceptLore.get(i).contains("%price%"))
                acceptLore.set(i, acceptLore.get(i).replaceAll("%price%", Click.confirm.get(player)[1]));
            if (acceptLore.get(i).contains("%particle%"))
                acceptLore.set(i, acceptLore.get(i).replaceAll("%particle%", Click.confirm.get(player)[0]));
            if (acceptLore.get(i).contains("%particleName%")) {
                ConfigurationSection sec = pl.menu.getConfigurationSection("Menu." + Click.confirm.get(player)[0]);
                acceptLore.set(i, acceptLore.get(i).replaceAll("%particleName%",
                        pl.getText().color(sec.getString("Name"))));
            }
        }
        inv.setItem(acceptSlot, pl.getPhd().getItem(player, acceptItem, acceptName, acceptLore));
        inv.setItem(denySlot, pl.getPhd().getItem(player, denyItem, denyName, denyLore));
        Click.invs.put(player, inv);
        Click.type.put(player, sec);
        player.openInventory(inv);
    }
}
