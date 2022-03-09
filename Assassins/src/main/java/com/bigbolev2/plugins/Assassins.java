package com.bigbolev2.plugins;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Assassins extends JavaPlugin implements Listener {
    private Player survivor;

    // for use in AssassinsStart to establish hashmap
    public void setSurvivor(Player player) { survivor = player; }

    @Override
    public void onEnable() {
        System.out.println("[Assassins] Plugin Enabled.........");
    }

    // if assassin hits them, instakill
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (e.getEntity() == survivor && e.getDamager() != survivor) {
                // auto kill player on one hit
                e.setDamage(1000000);
            }
        }
    }

    // disallow movement if player is looking at them
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer() != survivor && survivor.getEyeLocation() == e.getPlayer().getLocation()) {
            e.setCancelled(true);
            survivor.spawnParticle(Particle.DRIP_LAVA, survivor.getLocation(), 5);
        }
    }

    @Override
    public void onDisable() {
        System.out.println("[Assassins] Plugin Disabled.........");
    }
}
