package com.bigbolev2.plugins;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public final class Manhunt extends JavaPlugin implements Listener {

    enum Hunter {SURVIVOR, HUNTER, DEAD}

    private boolean isRunning = false;

    void setRunning(boolean running) {
        isRunning = running;
    }

    private final HashMap<Player, Hunter> playerList = new HashMap<>();

    // set survivors
    void setSurvivors(ArrayList<Player> survivors) {
        for (Player survivor : survivors) {
            playerList.put(survivor, Hunter.SURVIVOR);
        }
    }

    // give compass
    void giveCompass() {
        // make the compass
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();
        assert compassMeta != null;
        compassMeta.setDisplayName(ChatColor.RED + "Tracker");
        compassMeta.setUnbreakable(true);
        compass.addEnchantment(Enchantment.VANISHING_CURSE, 1);

        // give to player
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (playerList.get(player) == Hunter.HUNTER) {
                player.getInventory().setItem(8, compass);
            }
        }
    }

    @Override
    public void onEnable() {
        System.out.println("[Manhunt] Plugin Enabled.........");

        // register listener
        Bukkit.getPluginManager().registerEvents(this, this);

        // register commands
        getCommand("startmanhunt").setExecutor(new StartManhunt(this));
        getCommand("getcompass").setExecutor(new ManhuntCompass());

        // set everyone to hunter to start
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerList.put(player, Hunter.HUNTER);
        }

    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (isRunning) {
            // check if it is compass right-clicked in main hand
            if (Objects.equals(event.getHand(), EquipmentSlot.HAND)) {
                if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (event.getItem() != null && event.getItem().getType().equals(Material.COMPASS)) {
                        Player sender = event.getPlayer();

                        // put everyone's distance into a hash map
                        HashMap<Double, Player> distances = new HashMap<Double, Player>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (playerList.get(player) == Hunter.SURVIVOR) {
                                distances.put(player.getLocation().distanceSquared(sender.getLocation()), player);
                            }
                        }

                        // find smallest
                        double closest = -1;
                        for (double distance : distances.keySet()) {
                            if (closest == -1) {
                                closest = distance;
                            } else if (distance < closest) {
                                closest = distance;
                            }
                        }

                        // access smallest
                        sender.setCompassTarget(distances.get(closest).getLocation());
                        sender.sendMessage(ChatColor.GOLD + "Compass is now pointing at " + distances.get(closest).getDisplayName());
                    }
                }
            }
        }

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (isRunning) {
            // if survivor dies, display chat
            if (playerList.get(event.getEntity()) == Hunter.SURVIVOR) {
                playerList.replace(event.getEntity(), Hunter.DEAD);
                event.getEntity().setGameMode(GameMode.SPECTATOR);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (playerList.get(player) == Hunter.SURVIVOR && player != event.getEntity()) {
                        player.sendMessage(ChatColor.RED + "Hunter " + ChatColor.DARK_RED + event.getEntity().getDisplayName() + ChatColor.RED + " has just died!");
                    } else if (player != event.getEntity()) {
                        player.sendMessage(ChatColor.GREEN + "Hunter " + ChatColor.DARK_GREEN + event.getEntity().getDisplayName() + ChatColor.GREEN + " has just died!");
                    }
                }
            }

            // count remaining hunted
            int count = 0;
            for (Player player : playerList.keySet()) {
                if (playerList.get(player) == Hunter.SURVIVOR) { ++count; }
            }

            // if all hunted are dead, seekers win
            if (count == 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (playerList.get(player) == Hunter.SURVIVOR || playerList.get(player) == Hunter.DEAD) {
                        player.sendTitle(ChatColor.RED + "The Seekers Win!", ChatColor.DARK_RED + "Better luck next time...", 20, 60, 20);
                    } else {
                        player.sendTitle(ChatColor.GREEN + "The Seekers Win!", ChatColor.DARK_GREEN + "Congratulations!", 20, 60, 20);
                    }
                }
                setRunning(false);
                return;
            }

            // if ender dragon dies, hunted win
            if (event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (playerList.get(player) == Hunter.HUNTER) {
                        player.sendTitle(ChatColor.RED + "The Survivors Win!", ChatColor.DARK_RED + "Better luck next time...", 20, 60, 20);
                    } else {
                        player.sendTitle(ChatColor.GREEN + "The Survivors Win!", ChatColor.DARK_GREEN + "Congratulations!", 20, 60, 20);
                    }
                }
                setRunning(false);
            }
        }

    }

    @Override
    public void onDisable() {
        System.out.println("[Manhunt] Plugin Disabled.........");
    }
}
