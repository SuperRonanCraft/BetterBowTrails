package me.SuperRonanCraft.BetterBowTrails.references;

import org.bukkit.command.CommandSender;

public class Permissions {

    public boolean getDonor(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.donator");
    }

    public boolean getUpdate(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.updater");
    }

    public boolean getUse(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.use");
    }

    public boolean getOpen(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.use.menu");
    }

    public boolean getReload(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.reload");
    }

    public boolean getSet(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.set");
    }

    public boolean getAmount(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.amount");
    }

    public boolean getType(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.type");
    }

    public boolean getRemove(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.remove");
    }

    public boolean getMenuAmount(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.amount.menu");
    }

    public boolean getMenuType(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.type.menu");
    }

    public boolean getMenuRemove(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.remove.menu");
    }

    public boolean getMenuExplosion(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.explosion.menu");
    }

    public boolean getMenuAll(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.menu.all");
    }

    public boolean getExplosion(CommandSender sendi) {
        return perm(sendi, "betterbowtrails.explosion");
    }

    private boolean perm(CommandSender sendi, String str) {
        return sendi.hasPermission(str);
    }

    private String[] cmds = {"help", "reload", "set", "amount", "type", "remove", "explosion"};

    public boolean valueOf(CommandSender sendi, String str) {
        if (str.equals(cmds[0]))
            return this.getUse(sendi);
        else if (str.equals(cmds[1]))
            return this.getReload(sendi);
        else if (str.equals(cmds[2]))
            return this.getSet(sendi);
        else if (str.equals(cmds[3]))
            return this.getAmount(sendi);
        else if (str.equals(cmds[4]))
            return this.getType(sendi);
        else if (str.equals(cmds[5]))
            return this.getRemove(sendi);
        else if (str.equals(cmds[6]))
            return this.getExplosion(sendi);
        else
            return false;
    }
}
