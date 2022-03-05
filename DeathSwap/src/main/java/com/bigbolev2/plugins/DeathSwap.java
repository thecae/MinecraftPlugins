package com.bigbolev2.plugins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;

public final class DeathSwap extends JavaPlugin implements Listener {

    private boolean isGameRunning = false;

    public void setRunning(boolean toggle) { isGameRunning = toggle; }

    // don't allow players to hit other players
    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e) {
        Entity target = e.getEntity();
        Entity damager = e.getDamager();
        if ((target instanceof Player) && (damager instanceof Player)) {
            e.setCancelled(true);
        }
    }

    @Override
    public void onEnable() {
        System.out.println("[Death Swap] Death Swap Enabled.........");

        // register listener and JavaPlugin
        Bukkit.getPluginManager().registerEvents(this, this);

        // set command
        getCommand("deathswap").setExecutor(new DeathSwapToggle(this));

        // execute runnable
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            int timeLeft = 300; // five minutes

            @Override
            public void run() {
                if (!isGameRunning) { return; }

                if (timeLeft == 0) {
                    ArrayList<Player> playerList = new ArrayList<>();
                    ArrayList<Location> locationList = new ArrayList<>();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        playerList.add(player);
                        locationList.add(player.getLocation());
                    }
                    Collections.shuffle(locationList);
                    for (int i = 0; i < playerList.size(); ++i) {
                        playerList.get(i).teleport(locationList.get(i));
                    }
                } else {
                    if (timeLeft >= 1 && timeLeft <= 10) {
                        Bukkit.broadcastMessage(ChatColor.RED + "Players will swap in " + Integer.toString(timeLeft) + "seconds!");
                    }
                    timeLeft--;
                }
            }
        }, 20*10, 20); // delay 10 seconds before starting

    }

    @Override
    public void onDisable() {
        System.out.println("[Death Swap] Death Swap Disabled.........");
    }
}
