package me.SuperRonanCraft.BetterBowTrails.event.arrow;

import me.SuperRonanCraft.BetterBowTrails.event.player.files.Files;
import me.SuperRonanCraft.BetterBowTrails.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.material.MaterialData;

import java.util.List;

class Particles {
    private int run;
    private boolean explosion = false;
    private boolean tillDespawn;
    private Player player;
    private String[] par;

    Particles(Main pl, Player plr, Projectile p) {
        Files files = pl.files;
        player = plr;
        tillDespawn = files.getType(player);
        int parCount = files.getAmount(player);
        par = files.getParticleId(player);
        if (files.getExplosion(player))
            explosion = true;
        run = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
            @Override
            public void run() {
                if (par[0].equals("null"))
                    Bukkit.getScheduler().cancelTask(run);
                Location loc = p.getLocation();
                if (tillDespawn && (p.isDead() || !Arrow.online.get(player))) {
                    List<Projectile> ps = Arrow.arrows.get(player);
                    ps.remove(p);
                    Arrow.arrows.put(player, ps);
                    Arrow.global.remove(p);
                    Bukkit.getScheduler().cancelTask(run);
                } else if (!tillDespawn && (p.isDead() || p.isOnGround() || !Arrow.online.get(player))) {
                    createParticle(loc, true, parCount);
                    List<Projectile> ps = Arrow.arrows.get(player);
                    ps.remove(p);
                    Arrow.arrows.put(player, ps);
                    Arrow.global.remove(p);
                    Bukkit.getScheduler().cancelTask(run);
                }
                createParticle(loc, false, parCount);
            }
        }, 0, pl.ticks);
    }

    @SuppressWarnings("deprecation")
    private void addParticleEffect(Location loc, Material mat, int data, Particle par, boolean hitGround,
                                   int parCount) {
        if (par != null) {
            if (!hitGround) {
                if (!par.equals(Particle.NOTE) && !par.equals(Particle.REDSTONE) && !par.equals(Particle.SPELL_MOB))
                    loc.getWorld().spawnParticle(par, loc, 0);
                else
                    try {
                        loc.getWorld().spawnParticle(par, loc, 1, new Particle.DustOptions(Color.RED, 1));
                    } catch (Exception e) {
                        loc.getWorld().spawnParticle(par, loc, 1);
                    }
            } else if (explosion)
                loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
        } else if (mat != null) {
            if (!hitGround) {
                //
                // loc.getWorld().playEffect(loc, Effect.TILE_BREAK,
                // mat.getId(), data);
                //ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(mat, (byte) data), (float) loc
                // .getX()
                //        , (float) loc.getY(), (float) loc.getZ(), 0F, 64, loc, player);
                //loc.getWorld().playEffect(loc, Effect.CLICK1, ); loc.getWorld().playEffect();
                //loc.getWorld().spawnParticle(Particle.valueOf(mat.name()), loc, 0);
                loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, parCount, 0, 0, 0, new MaterialData(mat,
                        (byte) data));
            } else if (explosion)
                loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
        }
    }

    private void createParticle(Location loc, boolean hitGround, int parCount) {
        try {
            addParticleEffect(loc, null, 0, Particle.valueOf(par[0].toUpperCase()), hitGround, parCount);
        } catch (IllegalArgumentException e) {
            if (par.length == 2)
                addParticleEffect(loc, Material.getMaterial(par[0].toUpperCase()), Integer.valueOf(par[1]), null,
                        hitGround, parCount);
            else
                addParticleEffect(loc, Material.getMaterial(par[0].toUpperCase()), 0, null, hitGround, parCount);
        }
    }
}
