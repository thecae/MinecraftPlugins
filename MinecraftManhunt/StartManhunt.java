package com.bigbolev2.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class StartManhunt implements CommandExecutor {

    private Manhunt manhunt;

    public StartManhunt(Manhunt manhunt) {
        this.manhunt = manhunt;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0) {
            // ensure that all given players are real players, if so add to list
            ArrayList<Player> survivors = new ArrayList<Player>();

            for (String name : args) {
                Player player = Bukkit.getPlayer(name);
                if (player == null) {
                    sender.sendMessage("Player " + name + " not found.");
                    return false;
                } else {
                    survivors.add(player);
                }
            }
            manhunt.setSurvivors(survivors);
            manhunt.giveCompass();

            Bukkit.broadcastMessage(ChatColor.GOLD + "The Manhunt has begun....");
            manhunt.setRunning(true);
        } else {
            sender.sendMessage(ChatColor.RED + "Enter the names of the survivors!");
        }

        return false;
    }
}
