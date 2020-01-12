package me.SuperRonanCraft.BetterBowTrails.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.SuperRonanCraft.BetterBowTrails.Main;
import me.SuperRonanCraft.BetterBowTrails.event.player.events.Click;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Menu {

    private Main pl;
    private ConfigurationSection settings;
    private int rows, slots;
    public static Object[] menuList;
    public static HashMap<String, String> nameArray = new HashMap<>();
    private HashMap<String, String> itemPerm = new HashMap<>();
    private HashMap<String, Integer> itemPrice = new HashMap<>();
    private HashMap<String, List<String>> loreArray = new HashMap<>();
    private HashMap<String, String[]> itemArray = new HashMap<>();
    private boolean explosion, toggletrailtype, remove, particleamount;
    private String explosionName, invName, removeName, toggleName, amountName, notAllowedItem, sec = "Menu", defaultPerm;
    private List<String> explosionLore, removeLore, toggleLore, amountLore, allowedLore, notAllowedLore;
    private String[] explosionItem, removeItem, toggleItem, amountItem;
    public static int explosionPos, removePos, togglePos, amountPos;
    public static boolean economy;

    public Menu(Main pl) {
        this.pl = pl;
    }

    public void loadItems() {
        settings = pl.menu.getConfigurationSection("Settings");
        slots = settings.getInt("Menu.Size.GuiRows") * 9;
        rows = settings.getInt("Menu.Size.ParticleRows") * 9;
        menuList = pl.menu.getConfigurationSection("Menu").getKeys(false).toArray();
        List<String> allLores = settings.getStringList("Menu.AllLores");
        for (Object menus : menuList) {
            ConfigurationSection sec = pl.menu.getConfigurationSection("Menu." + menus);
            String name = sec.getString("Name");
            nameArray.put(menus.toString(), name);
            itemArray.put(menus.toString(), sec.getString("Item").split(":"));
            List<String> lore = sec.getStringList("Lore");
            if (lore != null)
                for (String str : lore)
                    lore.set(lore.indexOf(str), lore.get(lore.indexOf(str)).replaceAll("%item%", name));
            if (allLores != null)
                for (String allLore : settings.getStringList("Menu.AllLores"))
                    lore.add(allLore.replaceAll("%item%", nameArray.get(menus.toString())));
            loreArray.put(menus.toString(), lore);
            itemPerm.put(menus.toString(), sec.getString("Permission"));
            itemPrice.put(menus.toString(), sec.getInt("Price"));
        }
        invName = settings.getString("Menu.Title");
        explosion = settings.getBoolean("Explosion.Enabled");
        remove = settings.getBoolean("Remove.Enabled");
        toggletrailtype = settings.getBoolean("ToggleTrailType.Enabled");
        particleamount = settings.getBoolean("ParticleAmount.Enabled");
        defaultPerm = settings.getString("Menu.Permission.DefaultPermission");
        if (explosion) {
            explosionName = settings.getString("Explosion.Name");
            explosionLore = settings.getStringList("Explosion.Lore");
            explosionItem = settings.getString("Explosion.Item").split(":");
            explosionPos = settings.getInt("Explosion.Slot") - 10 + slots;
        }
        if (remove) {
            removeName = settings.getString("Remove.Name");
            removeLore = settings.getStringList("Remove.Lore");
            removeItem = settings.getString("Remove.Item").split(":");
            removePos = settings.getInt("Remove.Slot") - 10 + slots;
        }
        if (toggletrailtype) {
            toggleName = settings.getString("ToggleTrailType.Name");
            toggleLore = settings.getStringList("ToggleTrailType.Lore");
            toggleItem = settings.getString("ToggleTrailType.Item").split(":");
            togglePos = settings.getInt("ToggleTrailType.Slot") - 10 + slots;
        }
        if (particleamount) {
            amountName = settings.getString("ParticleAmount.Name");
            amountLore = settings.getStringList("ParticleAmount.Lore");
            amountItem = settings.getString("ParticleAmount.Item").split(":");
            amountPos = settings.getInt("ParticleAmount.Slot") - 10 + slots;
        }
        economy = pl.getConfig().getBoolean("Economy.Enabled");
        allowedLore = settings.getStringList("Menu.Permission.Allowed.Lore");
        notAllowedLore = settings.getStringList("Menu.Permission.NotAllowed.Lore");
        notAllowedItem = settings.getString("Menu.Permission.NotAllowed.Item");
    }

    public void createMenu(Player player) {
        if (invName == null)
            loadItems();
        List<String> menuArray = new ArrayList<>();
        Inventory inv = Bukkit.getServer().createInventory(null, slots, pl.getText().color(invName));
        int page = Click.page.get(player);
        boolean nextPage = false;
        for (int i = ((page - 1) * rows); i < menuList.length; i++) {
            String menus = menuList[i].toString();
            String name = nameArray.get(menus);
            String[] item = itemArray.get(menus);
            List<String> lore = new ArrayList<>(loreArray.get(menus));
            boolean boughten = pl.files.getBought(menus, player);
            boolean permToEquip = pl.getPerms().getMenuAll(player);
            if (!permToEquip)
                if (itemPerm.get(menus) != null && !boughten) {
                    if (player.hasPermission(itemPerm.get(menus)))
                        permToEquip = true;
                } else if (boughten)
                    permToEquip = true;
            if (permToEquip) {
                lore.addAll(allowedLore);
                // if (notAllowedItem != "%item%")
                // item = notAllowedItem.split(":");
            } else {
                lore.addAll(notAllowedLore);
                if (economy && pl.getEconomy() != null)
                    if (itemPrice.get(menus) != 0)
                        for (String allLore : pl.getConfig().getStringList("Economy.Lore")) {
                            String price = String.valueOf(itemPrice.get(menus));
                            lore.add(allLore.replaceAll("%price%", price));
                        }
                    else if (pl.getConfig().getBoolean("Economy.Default.Enabled"))
                        for (String allLore : pl.getConfig().getStringList("Economy.Lore")) {
                            String price = pl.getConfig().getInt("Economy.Default.Price") + "";
                            lore.add(allLore.replaceAll("%price%", price));
                        }
            }
            ItemStack mat = pl.getPhd().getItem(player, item, name, lore);
            inv.setItem(i - ((page - 1) * rows), mat);
            menuArray.add(menus);
            if (menuArray.size() >= rows) {
                i = menuList.length;
                if (menuList.length > menuArray.size())
                    nextPage = true;
            }
        }
        inv = pl.getPhd().checkPages(player, inv, page, nextPage);
        if (toggletrailtype && pl.getPerms().getMenuType(player)) {
            boolean currentSetting = pl.files.getType(player);
            String current = settings.getString("ToggleTrailType.Current.Flight");
            if (currentSetting)
                current = settings.getString("ToggleTrailType.Current.Despawn");
            List<String> newLore = new ArrayList<>(toggleLore);
            for (int i = 0; i < newLore.size(); i++)
                newLore.set(i, newLore.get(i).replaceAll("%current%", current));
            ItemStack item = pl.getPhd().getItem(player, toggleItem, toggleName, newLore);
            inv.setItem(togglePos, item);
        }
        if (particleamount && pl.getPerms().getMenuAmount(player)) {
            String current = String.valueOf(pl.files.getAmount(player));
            List<String> newLore = new ArrayList<>(amountLore);
            for (int i = 0; i < newLore.size(); i++)
                newLore.set(i, newLore.get(i).replaceAll("%current%", current));
            ItemStack item = pl.getPhd().getItem(player, amountItem, amountName, newLore);
            inv.setItem(amountPos, item);
        }
        if (remove && pl.getPerms().getMenuRemove(player)) {
            String current = pl.files.getParticleName(player);
            if (current == null || current.equals("null"))
                current = settings.getString("Remove.Current.None");
            List<String> newLore = new ArrayList<>(removeLore);
            for (int i = 0; i < newLore.size(); i++)
                newLore.set(i, newLore.get(i).replaceAll("%current%", current));
            ItemStack item = pl.getPhd().getItem(player, removeItem, removeName, newLore);
            inv.setItem(removePos, item);
        }
        if (explosion && pl.getPerms().getMenuExplosion(player)) {
            boolean explode = pl.files.getExplosion(player);
            String current = settings.getString("Explosion.Current.Disabled");
            if (explode)
                current = settings.getString("Explosion.Current.Enabled");
            List<String> newLore = new ArrayList<>(explosionLore);
            for (int i = 0; i < newLore.size(); i++)
                newLore.set(i, newLore.get(i).replaceAll("%current%", current));
            ItemStack mat = pl.getPhd().getItem(player, explosionItem, explosionName, newLore);
            inv.setItem(explosionPos, mat);
        }
        Click.invs.put(player, inv);
        Click.type.put(player, sec);
        pl.getPhd().show(player, inv);
    }
}