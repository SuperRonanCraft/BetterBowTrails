package me.SuperRonanCraft.BetterBowTrails;

import java.io.File;
import java.util.List;

import me.SuperRonanCraft.BetterBowTrails.event.Commands;
import me.SuperRonanCraft.BetterBowTrails.event.player.events.Listeners;
import me.SuperRonanCraft.BetterBowTrails.event.player.files.Files;
import me.SuperRonanCraft.BetterBowTrails.references.Econ;
import me.SuperRonanCraft.BetterBowTrails.references.Permissions;
import me.SuperRonanCraft.BetterBowTrails.references.items.Placeholders;
import me.SuperRonanCraft.BetterBowTrails.references.text.Messages;
import me.SuperRonanCraft.BetterBowTrails.references.web.Updater;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.SuperRonanCraft.BetterBowTrails.inventories.Menu;
import me.SuperRonanCraft.BetterBowTrails.references.text.CustomPH;
import me.SuperRonanCraft.BetterBowTrails.references.web.Metrics;

public class Main extends JavaPlugin implements Listener {

    public YamlConfiguration menu = new YamlConfiguration(), msgs = new YamlConfiguration();
    private Messages text = new Messages(this);
    private Permissions perms = new Permissions();
    public File dataFolder = new File(getDataFolder(), File.separator + "data");
    private Placeholders phd = new Placeholders(this);
    private Econ economy;
    private Listeners listen = new Listeners();
    public Files files = new Files(this);
    private Commands cmd = new Commands(this);
    public Menu menuInv = new Menu(this);
    public int ticks;
    private static Main instance;
    public boolean papihooked = false;

    public void onEnable() {
        new Metrics(this);
        instance = this;
        registerConfig();
        registerDepends();
        registerEvents();
        registerFiles();
        phd.load();
        menuInv.loadItems();
        new Updater(this);
    }

    public static Main getInstance() {
        return instance;
    }

    public Econ getEconomy() {
        return economy;
    }

    public Placeholders getPhd() {
        return phd;
    }

    public Permissions getPerms() {
        return perms;
    }

    public Messages getText() {
        return text;
    }

    private void registerDepends() {
        if (!papihooked && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            papihooked = true;
            new CustomPH(this).hook();
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Vault"))
            economy = new Econ(this).registerEconomy();
        else
            economy = null;
    }

    @SuppressWarnings("all")
    @Override
    public boolean onCommand(CommandSender sendi, Command cmd, String label, String[] args) {
        this.cmd.onCommand(sendi, label, args);
        return true;
    }

    @SuppressWarnings("all")
    @Override
    public List<String> onTabComplete(CommandSender sendi, Command cmd, String label, String[] args) {
        return this.cmd.onTab(sendi, args);
    }

    private void registerEvents() {
        listen.register();
        if (getConfig().getBoolean("Settings.UseGUI"))
            listen.registerClick();
        if (!getConfig().getBoolean("Settings.DisableUpdater"))
            listen.registerJoin();
    }

    private void registerConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        ticks = this.getConfig().getInt("Settings.TickDelay");
        if (ticks <= 0 || ticks > 20)
            ticks = 1;
    }

    private void registerFiles() {
        File f = new File(getDataFolder(), "menu.yml");
        if (!f.exists())
            saveResource("menu.yml", false);
        load(f, menu);
        f = new File(getDataFolder(), "messages.yml");
        if (!f.exists())
            saveResource("messages.yml", false);
        load(f, msgs);
    }

    private void load(File f, YamlConfiguration yml) {
        try {
            yml.load(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload(CommandSender sendi) {
        reloadConfig();
        registerConfig();
        registerFiles();
        registerDepends();
        phd.load();
        menuInv.loadItems();
        text.getReload(sendi);
    }
}
