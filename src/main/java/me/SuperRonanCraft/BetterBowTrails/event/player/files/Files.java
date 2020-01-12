package me.SuperRonanCraft.BetterBowTrails.event.player.files;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import me.SuperRonanCraft.BetterBowTrails.event.player.events.Click;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.SuperRonanCraft.BetterBowTrails.Main;
import me.SuperRonanCraft.BetterBowTrails.inventories.Confirm;
import me.SuperRonanCraft.BetterBowTrails.inventories.Menu;

public class Files {
    private Main pl;
    private ConfigurationSection settings;
    public HashMap<Player, Boolean> setup = new HashMap<>();
    public HashMap<Player, File> file = new HashMap<>();
    public HashMap<Player, FileConfiguration> fileconfig = new HashMap<>();

    public Files(Main pl) {
        this.pl = pl;
    }

    private void create(Player player) {
        ConfigurationSection defaults = pl.getConfig().getConfigurationSection("Defaults");
        String trail = defaults.getString("ParticleTrail");
        String name = defaults.getString("ParticleName");
        int amount = defaults.getInt("ParticlesPerTick");
        boolean tilldespawn = defaults.getBoolean("tillDespawn");
        boolean explosion = defaults.getBoolean("Explosion");
        UUID playerUUID = player.getUniqueId();
        File f = new File(pl.dataFolder, File.separator + playerUUID + ".yml");
        if (!f.exists()) {
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
            try {
                playerData.set("particleID", trail);
                playerData.set("particleName", name);
                playerData.set("tillDespawn", tilldespawn);
                playerData.set("particlesPerTick", amount);
                playerData.set("Explosion", explosion);
                playerData.createSection("bought");
                playerData.save(f);
            } catch (IOException ex) {
                pl.getText().sms(player, "ERROR, please contact an admin, " + Main.getInstance().getName() + " was "
                        + "not able to setup your files!");
                ex.printStackTrace();
            }
        }
    }

    public void setupFiles(Player player) {
        if (file.get(player) == null) {
            UUID playerUUID = (player).getUniqueId();
            File f = new File(pl.dataFolder, File.separator + playerUUID + ".yml");
            if (!f.exists())
                create(player);
            fileconfig.put(player, YamlConfiguration.loadConfiguration(f));
            file.put(player, f);
        } else {
            if (!file.get(player).exists())
                create(player);
            fileconfig.put(player, YamlConfiguration.loadConfiguration(file.get(player)));
        }
        setup.put(player, true);
    }

    public void setConfirm(Player player) {
        FileConfiguration data = fileconfig.get(player);
        ConfigurationSection sec = pl.menu.getConfigurationSection("Menu." + Click.confirm.get(player)[0]);
        if (pl.getEconomy().charge(player, sec, Integer.valueOf(Click.confirm.get(player)[1]))) {
            String particle = sec.getString("ParticleID");
            data.set("particleID", particle);
            data.set("particleName", sec.getString("Name"));
            addBought(player, sec.getString("Name"));
            saveFile(data, player);
            if (pl.menu.getBoolean("Settings.Menu.Close"))
                player.closeInventory();
        }
    }

    public void setAmount(Player player, boolean plus, boolean inv) {
        int currentAmount;
        FileConfiguration playerData = fileconfig.get(player);
        currentAmount = playerData.getInt("particlesPerTick");
        int setAmount;
        if (!plus) {
            if (currentAmount != 1)
                setAmount = currentAmount - 1;
            else {
                String msg = pl.getText().getParticleAmountMin();
                msg = msg.replaceAll("%amount%", String.valueOf(currentAmount));
                pl.getText().sms(player, msg);
                return;
            }
        } else {
            if (currentAmount != pl.getConfig().getInt("Defaults.ParticlesPerTick"))
                setAmount = currentAmount + 1;
            else {
                String msg = pl.getText().getParticleAmountMax();
                msg = msg.replaceAll("%amount%", String.valueOf(currentAmount));
                pl.getText().sms(player, msg);
                return;
            }
        }
        String msg = pl.getText().getParticleAmount();
        msg = msg.replaceAll("%amount%", String.valueOf(setAmount));
        settings = pl.menu.getConfigurationSection("Settings");
        playerData.set("particlesPerTick", setAmount);
        try {
            playerData.save(getFileName(player));
        } catch (IOException e1) {
            e1.printStackTrace();
            pl.getText().sms(player, "&cError saving data! &6Contact an admin to check console!");
        }
        if (inv) {
            if (settings.getBoolean("ParticleAmount.SendMessage"))
                pl.getText().sms(player, msg);
            if (settings.getBoolean("ParticleAmount.Close"))
                player.closeInventory();
            else
                pl.menuInv.createMenu(player);
        } else
            pl.getText().sms(player, msg);
    }

    public int getAmount(Player player) {
        if (setup.get(player) == null)
            setupFiles(player);
        return fileconfig.get(player).getInt("particlesPerTick");
    }

    public void setType(Player player, boolean inv) {
        String msg = pl.getText().getToggleTrailType();
        FileConfiguration playerData = fileconfig.get(player);
        boolean current = playerData.getBoolean("tillDespawn");
        settings = pl.menu.getConfigurationSection("Settings");
        if (current) {
            playerData.set("tillDespawn", false);
            msg = msg.replaceAll("%type%", settings.getString("ToggleTrailType.Current.Flight"));
        } else {
            playerData.set("tillDespawn", true);
            msg = msg.replaceAll("%type%", settings.getString("ToggleTrailType.Current.Despawn"));
        }
        try {
            playerData.save(getFileName(player));
        } catch (IOException e1) {
            e1.printStackTrace();
            pl.getText().sms(player, "&cError saving data! &6Contact an admin to check console!");
            return;
        }
        if (inv) {
            if (settings.getBoolean("ToggleTrailType.SendMessage"))
                pl.getText().sms(player, msg);
            if (settings.getBoolean("ToggleTrailType.Close"))
                player.closeInventory();
            else
                pl.menuInv.createMenu(player);
        } else
            pl.getText().sms(player, msg);
    }

    public boolean getType(Player player) {
        if (setup.get(player) == null)
            setupFiles(player);
        return fileconfig.get(player).getBoolean("tillDespawn");
    }

    public void setRemove(Player player, boolean inv) {
        settings = pl.menu.getConfigurationSection("Settings");
        String msg = pl.getText().getRemove();
        FileConfiguration playerData = fileconfig.get(player);
        String parName = playerData.getString("particleName");
        if (playerData.getString("particleID").equals("null")) {
            pl.getText().getNoTrail(player);
            return;
        } else {
            msg = msg.replaceAll("%trail%", parName);
            playerData.set("particleID", "null");
            playerData.set("particleName", "null");
            try {
                playerData.save(file.get(player));
            } catch (IOException e1) {
                e1.printStackTrace();
                pl.getText().sms(player, "&cError saving data! &6Contact an admin to check console!");
            }
        }
        if (inv) {
            if (settings.getBoolean("Remove.SendMessage"))
                pl.getText().sms(player, msg);
            if (settings.getBoolean("Remove.Close"))
                player.closeInventory();
            else
                pl.menuInv.createMenu(player);
        } else
            pl.getText().sms(player, msg);
    }

    public void setParticle(Player player, InventoryClickEvent e) throws NullPointerException {
        settings = pl.menu.getConfigurationSection("Settings");
        String msg = pl.getText().getSetParticle();
        FileConfiguration playerData = fileconfig.get(player);
        //settings = pl.menu.getConfigurationSection("Settings");
        ConfigurationSection parts = null;
        String item = null;
        for (Object menus : Menu.menuList) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(pl.getText().color(Menu.nameArray.get(menus)).replaceAll("§f", ""))) {
                parts = pl.menu.getConfigurationSection("Menu." + menus);
                item = menus.toString();
                break;
            }
        }
        boolean boughten = getBought(item, player);
        boolean permToEquip = false;
        if (parts.getString("Permission") != null && !boughten) {
            if (player.hasPermission(parts.getString("Permission")))
                permToEquip = true;
        } else if (boughten || pl.getPerms().getMenuAll(player))
            permToEquip = true;
        if (e.isRightClick() && !boughten && pl.getEconomy() != null && Menu.economy && !permToEquip) {
            int price = pl.getConfig().getInt("Economy.Default.Price");
            if (parts.getInt("Price") != 0)
                price = parts.getInt("Price");
            else if (!pl.getConfig().getBoolean("Economy.Default.Enabled"))
                return;
            String[] confirmPut = (item + ":" + price).split(":");
            Click.confirm.put(player, confirmPut);
            if (pl.getConfig().getBoolean("Economy.Confirm.Enabled"))
                new Confirm(pl).createMenu(player);
            else
                setConfirm(player);
            return;
        }
        if (permToEquip) {
            String particle = parts.getString("ParticleID");
            String name = parts.getString("Name");
            playerData.set("particleID", particle);
            playerData.set("particleName", parts.getString("Name"));
            try {
                playerData.save(getFileName(player));
                msg = msg.replaceAll("%trail%", name);
            } catch (IOException e1) {
                e1.printStackTrace();
                pl.getText().sms(player, "&cError saving data! &6Contact an admin to check console!");
                return;
            }
        } else
            return;
        if (settings.getBoolean("Menu.Close"))
            player.closeInventory();
        else
            pl.menuInv.createMenu(player);
        pl.getText().sms(player, msg);
    }

    public String getParticleName(Player player) {
        if (setup.get(player) == null)
            setupFiles(player);
        return pl.getText().color(fileconfig.get(player).getString("particleName"));
    }

    public String[] getParticleId(Player player) {
        return fileconfig.get(player).getString("particleID").split(":");
    }

    public void setExplosion(Player player, boolean inv) {
        settings = pl.menu.getConfigurationSection("Settings");
        FileConfiguration playerData = fileconfig.get(player);
        boolean current = playerData.getBoolean("Explosion");
        String msg = pl.getText().getExplosion();
        if (current) {
            playerData.set("Explosion", false);
            msg = msg.replaceAll("%current%", settings.getString("Explosion.Current.Disabled"));
        } else {
            playerData.set("Explosion", true);
            msg = msg.replaceAll("%current%", settings.getString("Explosion.Current.Enabled"));
        }
        try {
            playerData.save(getFileName(player));
            if (settings.getBoolean("Explosion.SendMessage"))
                pl.getText().sms(player, msg);
        } catch (IOException e1) {
            e1.printStackTrace();
            pl.getText().sms(player, "&cError saving data! &6Contact an admin to check console!");
        }
        if (inv) {
            if (settings.getBoolean("Explosion.Close"))
                player.closeInventory();
            else
                pl.menuInv.createMenu(player);
        }
    }

    public boolean getExplosion(Player player) {
        if (setup.get(player) == null)
            setupFiles(player);
        return getFile(player).getBoolean("Explosion");
    }

    public void setParticleId(Player player, String particleID) {
        FileConfiguration data = fileconfig.get(player);
        data.set("particleID", particleID);
        saveFile(data, player);
    }

    public void setParticleName(Player player, String particleName) {
        FileConfiguration data = fileconfig.get(player);
        data.set("particleName", particleName);
        saveFile(data, player);
    }

    private FileConfiguration getFile(Player player) {
        if (file.get(player) == null) {
            UUID playerUUID = player.getUniqueId();
            File f = new File(pl.dataFolder, File.separator + playerUUID + ".yml");
            if (!f.exists())
                create(player);
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
            // Arrow.yamls.put(player, playerData);
            file.put(player, f);
            return playerData;
        } else {
            if (!file.get(player).exists())
                create(player);
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(file.get(player));
            // Arrow.yamls.put(player, playerData);
            return playerData;
        }
    }

    private File getFileName(Player player) {
        if (file.get(player) == null) {
            UUID playerUUID = player.getUniqueId();
            File f = new File(pl.dataFolder, File.separator + playerUUID + ".yml");
            if (!f.exists())
                create(player);
            return f;
        } else {
            if (!file.get(player).exists())
                create(player);
            return file.get(player);
        }
    }

    public boolean getBought(String menus, Player player) {
        if (pl.getConfig().getBoolean("Economy.BoughtTrailsEnabled")) {
            FileConfiguration data = fileconfig.get(player);
            String[] bought = data.getString("bought").split(",");
            for (Object menuSec : bought)
                if (menuSec.equals(menus))
                    return true;
        }
        return false;
    }

    private void saveFile(FileConfiguration data, Player player) {
        try {
            data.save(file.get(player));
        } catch (IOException e) {
            e.printStackTrace();
            pl.getText().sms(player, "&cError saving data! &6Contact an admin to check console!");
        }
    }

    public void addBought(Player player, String bought) {
        FileConfiguration data = fileconfig.get(player);
        String bought1 = data.getString("bought");
        if (!bought1.equals(""))
            bought1 = bought1 + "," + bought;
        else
            bought1 = bought;
        data.set("bought", bought1);
        saveFile(data, player);
    }
}
