package me.SuperRonanCraft.BetterBowTrails.event;

import java.util.ArrayList;
import java.util.List;

import me.SuperRonanCraft.BetterBowTrails.event.player.events.Click;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.SuperRonanCraft.BetterBowTrails.Main;

public class Commands {

    private Main pl;

    public Commands(Main pl) {
        this.pl = pl;
    }

    public void onCommand(CommandSender sendi, String label, String[] args) {
        if (!pl.getPerms().getUse(sendi))
            pl.getText().getNoPermission(sendi);
        else if (sendi instanceof Player) {
            if (pl.files.setup.get(sendi) == null)
                pl.files.setupFiles((Player) sendi);
            if (!(args.length == 0)) {
                if (args[0].equalsIgnoreCase("reload"))
                    reload(sendi);
                else if (args[0].equalsIgnoreCase("help") && args.length == 1)
                    help(sendi, label);
                else if (args[0].equalsIgnoreCase("set")) {
                    set(sendi, args, label);
                } else if (args[0].equalsIgnoreCase("amount")) {
                    if (args.length == 2 && (args[1].equals("+") || args[1].equals("-")))
                        amount(sendi, args);
                    else
                        usage(sendi, "amount", label, args);
                } else if (args[0].equalsIgnoreCase("type") && args.length == 1)
                    type(sendi);
                else if (args[0].equalsIgnoreCase("remove") && args.length == 1)
                    remove(sendi);
                else if (args[0].equalsIgnoreCase("explosion") && args.length == 1)
                    explosion(sendi);
                else
                    pl.getText().getInvalidCommand(sendi, label);
            } else {
                if (!isPlayer(sendi))
                    return;
                if (pl.getConfig().getBoolean("Settings.UseGUI")) {
                    if (!pl.getPerms().getOpen(sendi)) {
                        pl.getText().getNoPermission(sendi);
                        return;
                    }
                    Click.page.put((Player) sendi, 1);
                    pl.menuInv.createMenu((Player) sendi);
                } else
                    help(sendi, label);
            }
        }
    }

    private String[] cmds = {"help", "reload", "set", "amount", "type", "remove", "explosion"};

    @SuppressWarnings("deprecation")
    public List<String> onTab(CommandSender sendi, String[] args) {
        if (!pl.getPerms().getUse(sendi))
            return new ArrayList<>();
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            for (String str : cmds)
                if (str.startsWith(args[0].toLowerCase()) && pl.getPerms().valueOf(sendi, str))
                    list.add(str);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase(cmds[3]) && pl.getPerms().valueOf(sendi, cmds[3])) {
                list.add("+");
                list.add("-");
            } else if (args[0].equalsIgnoreCase(cmds[2]) && pl.getPerms().valueOf(sendi, cmds[2]))
                if (!isInt(args[1])) {
                    for (Material mat : Material.values())
                        if (mat.isBlock() && mat.name().contains(args[1].toUpperCase()))
                            list.add(mat.name());
                } else if (isInt(args[1]))
                    for (Material mat : Material.values())
                        if (mat.isBlock() && String.valueOf(mat.getId()).contains(args[1]))
                            list.add(String.valueOf(mat.getId()));

        }
        return list;
    }

    private boolean isInt(String str) {
        try {
            Integer.valueOf(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void reload(CommandSender sendi) {
        if (!pl.getPerms().getReload(sendi)) {
            pl.getText().getNoPermission(sendi);
            return;
        }
        pl.reload(sendi);
    }

    private void help(CommandSender sendi, String cmd) {
        if (!pl.getPerms().getUse(sendi)) {
            pl.getText().getNoPermission(sendi);
            return;
        }
        if (!(sendi instanceof Player))
            pl.getText().smsnp(sendi, "&cWARNING: &7Console may not be able to execute most of these commands!");
        for (String str : pl.getText().getHelpTitle())
            pl.getText().smsnp(sendi, str.replaceAll("%command%", cmd));
        if (pl.getConfig().getBoolean("Settings.UseGUI"))
            if (pl.getPerms().getOpen(sendi))
                for (String str : pl.getText().getHelpGUI())
                    pl.getText().smsnp(sendi, str.replaceAll("%command%", cmd));
        if (pl.getPerms().getAmount(sendi))
            for (String str : pl.getText().getHelpAmount())
                pl.getText().smsnp(sendi, str.replaceAll("%command%", cmd));
        if (pl.getPerms().getSet(sendi))
            for (String str : pl.getText().getHelpSet())
                pl.getText().smsnp(sendi, str.replaceAll("%command%", cmd));
        if (pl.getPerms().getRemove(sendi))
            for (String str : pl.getText().getHelpRemove())
                pl.getText().smsnp(sendi, str.replaceAll("%command%", cmd));
        if (pl.getPerms().getType(sendi))
            for (String str : pl.getText().getHelpType())
                pl.getText().smsnp(sendi, str.replaceAll("%command%", cmd));
        if (pl.getPerms().getExplosion(sendi))
            for (String str : pl.getText().getHelpExplosion())
                pl.getText().smsnp(sendi, str.replaceAll("%command%", cmd));
        if (pl.getPerms().getReload(sendi))
            for (String str : pl.getText().getHelpReload())
                pl.getText().smsnp(sendi, str.replaceAll("%command%", cmd));
    }

    @SuppressWarnings("deprecation")
    private void set(CommandSender sendi, String[] args, String label) {
        if (!isPlayer(sendi))
            return;
        if (!pl.getPerms().getSet(sendi)) {
            pl.getText().getNoPermission(sendi);
            return;
        } else if (args.length < 2) {
            usage(sendi, "set", label, args);
            return;
        }
        Player player = Bukkit.getPlayer(sendi.getName());
        // Filepl.getConfig()uration playerData = phd.getFile(player);
        String[] block = args[1].split(":");
        boolean fromMenu = false;
        Object[] menuList = pl.menu.getConfigurationSection("Menu").getKeys(false).toArray();
        for (Object menus : menuList)
            if (block[0].equals(menus.toString()))
                fromMenu = true;
        String particleID;
        try {
            if (block.length == 1 && !fromMenu) {
                try {
                    particleID = Material.getMaterial(block[0].toUpperCase()).toString();
                } catch (NullPointerException ne) {
                    //if (Particle.valueOf(block[0].toUpperCase()) != null)
                    particleID = block[0].toUpperCase();
                }
            } else if (block.length == 2 && !fromMenu) {
                // IF the item is from the menu, set the preset
                particleID = Material.getMaterial(block[0].toUpperCase()).toString() + ":" + block[1];
            } else {
                if (fromMenu) {
                    ConfigurationSection sec = pl.menu.getConfigurationSection("Menu." + block[0]);
                    particleID = sec.getString("ParticleID");
                    String name = sec.getString("Name");
                    pl.files.setParticleId(player, particleID);
                    pl.files.setParticleName(player, name);
                    pl.getText().smsnp(player, pl.getText().getSetParticle().replaceAll("%trail%", name));
                    return;
                } else {
                    pl.getText().getInvalidID(player);
                    return;
                }
            }
        } catch (IllegalArgumentException e) {
            pl.getText().getInvalidID(player);
            return;
        }
        if (args.length > 2) {
            String parName = null;
            for (int i = 0; i < args.length; i++) {
                if (!(args[i].equals(args[0]) || args[i].equals(args[1])))
                    if (parName == null)
                        parName = args[i];
                    else
                        parName = parName + " " + args[i];
            }
            pl.files.setParticleName(player, parName);
            pl.getText().smsnp(player, pl.getText().getSetParticle().replaceAll("%trail%", parName));
        } else {
            pl.files.setParticleName(player, particleID);
            pl.getText().smsnp(player, pl.getText().getSetParticle().replaceAll("%trail%", particleID));
        }
        pl.files.setParticleId(player, particleID);
    }

    private void amount(CommandSender sendi, String[] args) {
        if (!isPlayer(sendi))
            return;
        if (!pl.getPerms().getAmount(sendi)) {
            pl.getText().getNoPermission(sendi);
            return;
        }
        if (args[1].equals("-"))
            pl.files.setAmount((Player) sendi, false, false);
        else if (args[1].equals("+"))
            pl.files.setAmount((Player) sendi, true, false);
    }

    private void type(CommandSender sendi) {
        if (!isPlayer(sendi))
            return;
        if (!pl.getPerms().getType(sendi)) {
            pl.getText().getNoPermission(sendi);
            return;
        }
        pl.files.setType((Player) sendi, false);
    }

    private void remove(CommandSender sendi) {
        if (!isPlayer(sendi))
            return;
        if (!pl.getPerms().getRemove(sendi)) {
            pl.getText().getNoPermission(sendi);
            return;
        }
        pl.files.setRemove((Player) sendi, false);
    }

    private void explosion(CommandSender sendi) {
        if (!isPlayer(sendi))
            return;
        if (!pl.getPerms().getExplosion(sendi)) {
            pl.getText().getNoPermission(sendi);
            return;
        }
        pl.files.setExplosion((Player) sendi, false);
    }

    private boolean isPlayer(CommandSender sendi) {
        if (sendi instanceof Player)
            return true;
        pl.getText().smsnp(sendi, "&7You must be a player to execute this command!");
        return false;
    }

    private void usage(CommandSender sendi, String str, String cmd, String[] args) {
        if (str.equals("amount"))
            pl.getText().smsnp(sendi, "&7Usage: &6/" + cmd + " " + args[0] + " <+/->");
        else if (str.equals("set"))
            pl.getText().smsnp(sendi, "&7Usage: &6/" + cmd + " " + args[0] + " <blockID> [name]");
        else
            pl.getText().smsnp(sendi, "&c&lERROR, what happened?");
    }
}
