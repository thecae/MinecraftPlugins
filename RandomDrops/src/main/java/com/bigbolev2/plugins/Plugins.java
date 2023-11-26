package com.bigbolev2.plugins;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public final class Plugins extends JavaPlugin implements Listener {

    // get list of items in the game
    ArrayList<Material> items = new ArrayList<>();

    private boolean running = false;
    void setRunning(boolean run) { running = run; }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (!running) return;

        // get random amount and size
        Material item = items.get(new Random().nextInt(items.size()));
        int amount = new Random().nextInt((int)1E6);

        // replace droppings with new ones
        ArrayList<Material> dropArray = new ArrayList<>();
        for (int x = 0; x < amount; ++x) dropArray.add(item);
        event.setDropItems(false);
        /* todo - how to spawn an item in-place */
    }

    @Override
    public void onEnable() {
        System.out.println("[RandomDrops] RandomDrops Enabled.........");

        // register listener and command executor
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("randomdrops").setExecutor(new RandomDropToggle(this));

        // make the drop list
        items.addAll(Arrays.asList(Material.values()));
    }

    @Override
    public void onDisable() {
        System.out.println("[RandomDrops] RandomDrops Disabled.........");
    }
}
