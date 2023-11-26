package com.bigbolev2.plugins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AssassinsStart implements CommandExecutor {
    private Assassins game;

    public AssassinsStart(Assassins game) { this.game = game; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Invalid arguments!");
            return false;
        }
        if (Bukkit.getServer().getPlayer(args[0]) != null) {
            Player player = Bukkit.getServer().getPlayer(args[0]);

            // allocate each player to the hash map with player status
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p == player) { game.setSurvivor(p); }
            }

            // announce the game has started
            Bukkit.broadcastMessage(ChatColor.GOLD + "ASSASSINS HAS BEGUN!");
            return true;
        }
        sender.sendMessage("Invalid arguments!");
        return false;
    }
}
