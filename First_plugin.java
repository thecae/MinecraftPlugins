package me.cole.first_plugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class First_plugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        System.out.println("Plugin activated.");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onLeaveBed(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("Good morning!");
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent event) {
        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.DARK_RED + "Nice try!");
    }

    @Override
    public void onDisable() {
        System.out.println("Plugin deactivated.");
    }
}
