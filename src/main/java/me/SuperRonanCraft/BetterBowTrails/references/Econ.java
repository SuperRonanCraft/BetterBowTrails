package me.SuperRonanCraft.BetterBowTrails.references;

import me.SuperRonanCraft.BetterBowTrails.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Econ {
    private Main pl;
    private Economy e;

    public Econ(Main pl) {
        this.pl = pl;
    }

    public boolean charge(Player player, ConfigurationSection sec, int price) {
        String priceName = e.currencyNameSingular() + price;
        String par = pl.getText().color(sec.getString("Name"));
        EconomyResponse r = e.withdrawPlayer(player, price);
        if (r.transactionSuccess()) {
            pl.getText().color(pl.getText().getBought().replaceAll("%price%", priceName).replaceAll("%trail%", par));
            pl.files.addBought(player, sec.getName());
            return true;
        } else {
            pl.getText().sms(player, pl.getText().getNotEnoughMoney().replaceAll("%price%", priceName).replaceAll
                    ("%trail%", par));
            return false;
        }
    }

    /*public boolean getEco() {
        return e != null;
    }*/

    public Econ registerEconomy() {
        if (pl.getServer().getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Economy> rsp = pl.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null)
                return null;
            e = rsp.getProvider();
            return this;
        }
        return null;
    }
}
