package com.bigbolev2.plugins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class BlockShuffle extends JavaPlugin implements Listener {
    // list of blocks
    ArrayList<Material> blocks = new ArrayList<Material>();

    // list of players and blocks
    HashMap<Player, Material> playerMap = new HashMap<Player, Material>();

    // is game running
    private boolean isRunning = false;

    void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void onEnable() {
        System.out.println("[BlockShuffle] BlockShuffle Enabled.........");

        // register listener and command executor
        Bukkit.getPluginManager().registerEvents(this, this);

        getCommand("blockshuffle").setExecutor(new BlockShuffleToggle(this));

        // make block list
        for (Material block : Material.values()) {
            if (block.isSolid()) {
                blocks.add(block);
            }
        }

        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            int timeLeft = 300; // five minutes

            @Override
            public void run() {
                if (!isRunning) {
                    return;
                }

                if (timeLeft == 300) {
                    // give players their blocks
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Material block = blocks.get(new Random().nextInt(blocks.size()));
                        playerMap.put(player, block);
                        player.sendMessage(ChatColor.GOLD + "You must find and stand on: " + ChatColor.AQUA + block.toString());
                    }
                }

                if (playerMap.isEmpty()) {
                    // everyone has found their block, continue
                    Bukkit.broadcastMessage(ChatColor.GOLD + "Everyone has found their block!");
                    timeLeft = 300;
                    return;
                }

                if (timeLeft == 0) {
                    // figure out who hasn't finished, end game
                    for (HashMap.Entry<Player, Material> entry : playerMap.entrySet()) {
                        Bukkit.broadcastMessage(ChatColor.DARK_RED + entry.getKey().getDisplayName() + ChatColor.RED + " did not find their block ["+ ChatColor.DARK_RED + entry.getValue() + ChatColor.RED + "]!");
                        isRunning = false;
                        timeLeft = 300;
                    }
                } else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getLocation().add(0, -1, 0).getBlock().getType().equals(playerMap.get(player))) {
                            // broadcast player has found their block
                            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + player.getDisplayName() + ChatColor.GREEN + " found their block [" + ChatColor.DARK_GREEN + playerMap.get(player).name() + ChatColor.GREEN + "]!");
                            playerMap.remove(player);
                        }
                    }
                    if (timeLeft == 180 || timeLeft == 60) {
                        // send message to players that haven't found their block
                        for (HashMap.Entry<Player, Material> entry : playerMap.entrySet()) {
                            entry.getKey().sendMessage(ChatColor.GOLD + Integer.toString(timeLeft / 60) + " minutes remaining to find your block!");
                        }
                    }
                    if (timeLeft <= 10) {
                        // if time less than 10, but on main screen and chat
                        Bukkit.broadcastMessage(ChatColor.GOLD + Integer.toString(timeLeft) + " seconds remaining!");
                        for (HashMap.Entry<Player, Material> entry : playerMap.entrySet()) {
                            entry.getKey().sendTitle(null, ChatColor.RED + Integer.toString(timeLeft), 5, 10, 5);
                        }
                    }
                    timeLeft--; // subtract one second
                }
            }
        }, 20*10, 20);

    }

    @Override
    public void onDisable() {
        System.out.println("[BlockShuffle] BlockShuffle Disabled.........");
    }
}
