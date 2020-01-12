package me.SuperRonanCraft.BetterBowTrails.references.text;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.SuperRonanCraft.BetterBowTrails.Main;

public class Messages {

    private Main pl;

    public Messages(Main pl) {
        this.pl = pl;
    }

    public void getReload(CommandSender sendi) {
        sms(sendi, pl.msgs.getString("Messages.Reload"));
    }

    public String getSetParticle() {
        return pl.msgs.getString("Messages.SetParticle");
    }

    public String getToggleTrailType() {
        return pl.msgs.getString("Messages.ToggleTrailType");
    }

    public String getParticleAmount() {
        return pl.msgs.getString("Messages.ParticleAmount");
    }

    public String getParticleAmountMax() {
        return pl.msgs.getString("Messages.ParticleAmountMax");
    }

    public String getParticleAmountMin() {
        return pl.msgs.getString("Messages.ParticleAmountMin");
    }

    public String getExplosion() {
        return pl.msgs.getString("Messages.Explosion");
    }

    public void getNoPermission(CommandSender sendi) {
        sms(sendi, pl.msgs.getString("Messages.NoPermission"));
    }

    public String getRemove() {
        return pl.msgs.getString("Messages.Remove");
    }

    public void getNoTrail(CommandSender sendi) {
        sms(sendi, pl.msgs.getString("Messages.NoTrail"));
    }

    public void getInvalidID(CommandSender sendi) {
        sms(sendi, pl.msgs.getString("Messages.InvalidID"));
    }

    public void getInvalidCommand(CommandSender sendi, String label) {
        sms(sendi, pl.msgs.getString("Messages.InvalidCommand").replaceAll("%command%", label));
    }

    public String getBought() {
        return pl.msgs.getString("Messages.Bought");
    }

    public String getNotEnoughMoney() {
        return pl.msgs.getString("Messages.NotEnoughMoney");
    }

    public String getPrefix() {
        return pl.msgs.getString("Messages.Prefix");
    }

    public List<String> getHelpTitle() {
        return pl.msgs.getStringList("Help.Title");
    }

    public List<String> getHelpGUI() {
        return pl.msgs.getStringList("Help.Gui");
    }

    public List<String> getHelpAmount() {
        return pl.msgs.getStringList("Help.Amount");
    }

    public List<String> getHelpSet() {
        return pl.msgs.getStringList("Help.Set");
    }

    public List<String> getHelpRemove() {
        return pl.msgs.getStringList("Help.Remove");
    }

    public List<String> getHelpType() {
        return pl.msgs.getStringList("Help.Type");
    }

    public List<String> getHelpExplosion() {
        return pl.msgs.getStringList("Help.Explosion");
    }

    public List<String> getHelpReload() {
        return pl.msgs.getStringList("Help.Reload");
    }

    public void sms(CommandSender sendi, String str) {
        sendi.sendMessage(colorPre(str));
    }

    public void smsnp(CommandSender sendi, String str) {
        sendi.sendMessage(color(str));
    }

    public String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    String strip(String str) {
        return ChatColor.stripColor(color(str));
    }

    private String colorPre(String str) {
        return color(getPrefix() + str);
    }

}
