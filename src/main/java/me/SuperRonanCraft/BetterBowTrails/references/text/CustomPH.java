package me.SuperRonanCraft.BetterBowTrails.references.text;

import me.SuperRonanCraft.BetterBowTrails.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CustomPH extends PlaceholderExpansion {

    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier(){
        return getpl().getDescription().getName();
    }

    @Override
    public String getVersion(){
        return getpl().getDescription().getVersion();
    }

    @Override
    public String getAuthor(){
        return getpl().getDescription().getAuthors().get(0);
    }

    @Override
    public String onRequest(OfflinePlayer op, String id) {
        Player p = op.getPlayer();
        if (id.equals("amount"))
            return String.valueOf(getParAmount(p));
        if (id.equals("type"))
            return getType(p);
        if (id.equals("particle"))
            return getParticle(p);
        if (id.equals("explosion"))
            return getExplosion(p);
        return null;
    }

    private int getParAmount(Player player) {
        return getpl().files.getAmount(player);
    }

    private String getType(Player player) {
        boolean typeBoolean = getpl().files.getType(player);
        String type = getpl().getText().color(getpl().menu.getString("Settings.ToggleTrailType.Current.Flight"));
        if (typeBoolean)
            type = getpl().getText().color(getpl().menu.getString("Settings.ToggleTrailType.Current.Despawn"));
        return type;
    }

    private String getParticle(Player player) {
        String par = getpl().files.getParticleName(player);
        if (par.equals("null"))
            par = "None";
        return getpl().getText().strip(par);
    }

    private String getExplosion(Player player) {
        boolean typeBoolean = getpl().files.getExplosion(player);
        String type = getpl().getText().color(getpl().menu.getString("Settings.Explosion.Current.Disabled"));
        if (typeBoolean)
            type = getpl().getText().color(getpl().menu.getString("Settings.Explosion.Current.Enabled"));
        return type;
    }

    private Main getpl() {
        return Main.getInstance();
    }
}
