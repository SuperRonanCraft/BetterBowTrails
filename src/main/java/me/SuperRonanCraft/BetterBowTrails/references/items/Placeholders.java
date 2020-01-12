package me.SuperRonanCraft.BetterBowTrails.references.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.SuperRonanCraft.BetterBowTrails.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Placeholders {
    private Main pl;
    private String[] nextType, lastType;
    private String nextName, lastName;
    private List<String> nextLore, lastLore;
    private int nextSlot, lastSlot;
    private HashMap<UUID, ItemStack> next = new HashMap<>(), last = new HashMap<>();

    public Placeholders(Main pl) {
        this.pl = pl;
    }

    public void load() {
        nextType = pl.menu.getString("Settings.NextPage.Item").split(":");
        lastType = pl.menu.getString("Settings.LastPage.Item").split(":");
        nextName = pl.menu.getString("Settings.NextPage.Name");
        lastName = pl.menu.getString("Settings.LastPage.Name");
        nextLore = pl.menu.getStringList("Settings.NextPage.Lore");
        lastLore = pl.menu.getStringList("Settings.LastPage.Lore");
        nextSlot = pl.menu.getInt("Settings.NextPage.Slot");
        lastSlot = pl.menu.getInt("Settings.LastPage.Slot");
    }

    public void clearItems(Player p) {
        if (next.get(p.getUniqueId()) != null)
            next.remove(p.getUniqueId());
        if (last.get(p.getUniqueId()) != null)
            last.remove(p.getUniqueId());
    }

    public ItemStack getNext(Player p) {
        if (next.get(p.getUniqueId()) != null)
            return next.get(p.getUniqueId());
        ItemStack item = getItem(p, nextType, nextName, nextLore);
        next.put(p.getUniqueId(), item);
        return item;
    }

    public ItemStack getLast(Player p) {
        if (last.get(p.getUniqueId()) != null)
            return last.get(p.getUniqueId());
        ItemStack item = getItem(p, lastType, lastName, lastLore);
        last.put(p.getUniqueId(), item);
        return item;
    }

    public Inventory checkPages(Player p, Inventory inv, int page, boolean next) {
        if (next) {
            ItemStack item = getNext(p);
            if (item.getItemMeta().getLore() != null) {
                List<String> lore = item.getItemMeta().getLore();
                for (int i = 0; i < lore.size(); i++)
                    lore.set(i, lore.get(i).replaceAll("%page%", String.valueOf(page + 1)));
                ItemMeta meta = item.getItemMeta();
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            inv.setItem(inv.getSize() - 10 + nextSlot, item);
        }
        if (page != 1) {
            ItemStack item = getLast(p);
            if (item.getItemMeta().getLore() != null) {
                List<String> lore = item.getItemMeta().getLore();
                for (int i = 0; i < lore.size(); i++)
                    lore.set(i, lore.get(i).replaceAll("%page%", String.valueOf(page - 1)));
                ItemMeta meta = item.getItemMeta();
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            inv.setItem(inv.getSize() - 10 + lastSlot, item);
        }
        return inv;
    }

    public ItemStack getItem(Player p, String[] type, String name, List<String> lore) {
        return createItem(p, type, name, lore);
    }

    private ItemStack createItem(Player p, String[] type, String name, List<String> lore) {
        try {
            if (type.length == 1)
                if (p != null && pl.papihooked)
                    return addMeta(p, new ItemStack(mat(color(type[0])), 1, (short) 0), name, lore);
                else
                    return addMeta(p, new ItemStack(mat(type[0]), 1, (short) 0), name, lore);
            else if (type.length == 2)
                if (p != null && pl.papihooked)
                    return addMeta(p, new ItemStack(mat(color(type[0])), 1, (short) val(color(type[1].trim()))),
                            name, lore);
                else
                    return addMeta(p, new ItemStack(mat(type[0]), 1, (short) val(type[1])), name, lore);
            else if (type.length == 3)
                if (p != null && pl.papihooked)
                    return addMeta(p, new ItemStack(mat(color(type[0])), val(color(type[2])), (short) val(color
                            (type[1]))), name, lore);
                else
                    return addMeta(p, new ItemStack(mat(type[0]), val(type[2].trim()), (short) val(type[1])), name,
                            lore);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return invalidItem(type);
    }

    private ItemStack addMeta(Player p, ItemStack item, String name, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        if (lore != null)
            meta.setLore(lore);
        if (name != null)
            meta.setDisplayName(name);
        item.setItemMeta(meta);
        checkPH(item, p);
        return item;
    }

    private void checkPH(ItemStack item, Player player) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(meta.getDisplayName()));
        // Lore
        List<String> lore = item.getItemMeta().getLore();
        if (lore != null) {
            List<String> newLore = new ArrayList<>();
            for (int i = 0; i < meta.getLore().size(); i++) {
                String str = color(lore.get(i));
                if (str != null)
                    newLore.add(str);
            }
            meta.setLore(newLore);
        }
        item.setItemMeta(meta);
    }

    private String color(String name) {
        return Main.getInstance().getText().color(name);
    }

    public void show(Player p, Inventory inv) {
        p.openInventory(inv);
    }

    @SuppressWarnings("deprecated")
    private Material mat(String str) {
        return Material.getMaterial(str.toUpperCase().trim());
    }

    private int val(String str) {
        return Integer.valueOf(str.trim());
    }

    private ItemStack invalidItem(String[] item) {
        List<String> lore = new ArrayList<>();
        if (item != null)
            if (item.length == 1)
                lore.add(pl.getText().color("&f&n" + item[0]));
            else if (item.length == 2)
                lore.add(pl.getText().color("&f&n" + item[0] + ":" + item[1]));
            else {
                String itemid = null;
                for (String str : item) {
                    if (itemid == null)
                        itemid = str;
                    else
                        itemid = itemid + ":" + str;
                }
                lore.add(pl.getText().color("&f&n" + itemid));
            }
        return createItem(null, ("Bedrock").split(":"), "&c&lInvalid Item Id", lore);
    }
}
