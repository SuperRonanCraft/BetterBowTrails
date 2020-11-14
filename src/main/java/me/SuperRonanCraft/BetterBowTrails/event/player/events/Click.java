package me.SuperRonanCraft.BetterBowTrails.event.player.events;

import me.SuperRonanCraft.BetterBowTrails.inventories.Menu;
import me.SuperRonanCraft.BetterBowTrails.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Click implements Listener {
    private Main pl;
    private ConfigurationSection settings;
    public static HashMap<Player, Integer> page = new HashMap<>();
    public static HashMap<Player, Inventory> invs = new HashMap<>();
    public static HashMap<Player, String> type = new HashMap<>();
    public static HashMap<Player, String[]> confirm = new HashMap<>();

    public Click() {
        this.pl = Main.getInstance();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!validClick(e))
            return;
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        assert item != null;
        String itemName = item.getItemMeta().getDisplayName();
        if (settings == null)
            settings = pl.menu.getConfigurationSection("Settings");
        // Clicked NextPage
        if (type.get(player).equals("Menu")) {
            //System.out.println(e.getSlot() + " " + Menu.explosionPos);
            if (item.equals(pl.getPhd().getNext(player)))
                clickedNextPage(player);
                // Clicked LastPage
            else if (item.equals(pl.getPhd().getLast(player)))
                clickedLastPage(player, e);
                // Clicked ToggleTrailType
            else if (e.getSlot() == Menu.togglePos)
                clickedType(player);
                // Clicked Remove
            else if (e.getSlot() == Menu.removePos)
                clickedRemove(player);
                // Clicked ParticleAmount
            else if (e.getSlot() == Menu.amountPos)
                clickedAmount(player, e);
                // Clicked Explosion
            else if (e.getSlot() == Menu.explosionPos)
                clickedExplosion(player);
                // Clicked a Particle
            else
                clickedParticle(player, e);
        }
        // Clicked a Confirm
        else if (type.get(player).equals("Confirm")) {
            if (itemName.equals(pl.getText().color(
                    pl.getConfig().getString("Economy.Confirm.Accept.Name"))))
                confirm(player);
            pl.menuInv.createMenu(player);
        }
    }

    private void confirm(Player player) {
        pl.files.setConfirm(player);
    }

    private void clickedType(Player player) {
        if (!pl.getPerms().getMenuType(player))
            return;
        pl.files.setType(player, true);
    }

    private void clickedRemove(Player player) {
        if (!pl.getPerms().getMenuRemove(player))
            return;
        pl.files.setRemove(player, true);
    }

    private void clickedExplosion(Player player) {
        if (!pl.getPerms().getMenuExplosion(player))
            return;
        pl.files.setExplosion(player, true);
    }

    private void clickedParticle(Player player, InventoryClickEvent e) {
        pl.files.setParticle(player, e);
    }

    private void clickedAmount(Player player, InventoryClickEvent e) {
        if (!pl.getPerms().getMenuAmount(player))
            return;
        if (e.isRightClick())
            pl.files.setAmount(player, false, true);
        else
            pl.files.setAmount(player, true, true);
    }

    private void clickedLastPage(Player player, InventoryClickEvent e) {
        int pageNum = page.get(player) - 1;
        page.put(player, pageNum);
        pl.menuInv.createMenu(player);
        return;
    }

    private void clickedNextPage(Player player) {
        int pageNum = page.get(player) + 1;
        page.put(player, pageNum);
        pl.menuInv.createMenu(player);
        return;
    }

    // Test if a material type is numeric or text
    public static boolean isNumeric(String str) {
        try {
            @SuppressWarnings("unused")
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private boolean validClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player) || e.isCancelled())
            return false;
        // Clicks the inventory
        if (!e.getInventory().equals(invs.get(e.getWhoClicked())))
            return false;
        // Checks if click is valid
        try {
            e.getCurrentItem().getItemMeta().getDisplayName();
        } catch (NullPointerException ex) {
            return false;
        }
        // Clicks their own inventory
        if (!e.getClickedInventory().equals(invs.get(e.getWhoClicked()))) {
            e.setCancelled(true);
            return false;
        }
        return true;
    }
}
