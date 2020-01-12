package me.SuperRonanCraft.BetterBowTrails.references.text;

import me.SuperRonanCraft.BetterBowTrails.Main;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

public class CustomPH extends EZPlaceholderHook {

    private Main pl;

    public CustomPH(Main pl) {
        super(pl, "betterbowtrails");
        this.pl = pl;
    }

    @Override
    public String onPlaceholderRequest(Player p, String id) {
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
        return pl.files.getAmount(player);
    }

    private String getType(Player player) {
        boolean typeBoolean = pl.files.getType(player);
        String type = pl.getText().color(pl.menu.getString("Settings.ToggleTrailType.Current.Flight"));
        if (typeBoolean)
            type = pl.getText().color(pl.menu.getString("Settings.ToggleTrailType.Current.Despawn"));
        return type;
    }

    private String getParticle(Player player) {
        String par = pl.files.getParticleName(player);
        if (par.equals("null"))
            par = "None";
        return pl.getText().strip(par);
    }

    private String getExplosion(Player player) {
        boolean typeBoolean = pl.files.getExplosion(player);
        String type = pl.getText().color(pl.menu.getString("Settings.Explosion.Current.Disabled"));
        if (typeBoolean)
            type = pl.getText().color(pl.menu.getString("Settings.Explosion.Current.Enabled"));
        return type;
    }
}
