package com.bigbolev2.plugins;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DeathSwapToggle implements CommandExecutor {
    // contains DeathSwap object
    private DeathSwap game;

    // constructor for class
    public DeathSwapToggle(DeathSwap gameInstance) { game = gameInstance; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Invalid arguments!");
            return false;
        }
        if (args[0].equals("start")) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "DEATH SWAP IS ABOUT TO BEGIN!");
            game.setRunning(true);
            return false;
        }
        if (args[0].equals("stop")) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "DEATH SWAP HAS FINISHED. THANKS FOR PLAYING!");
            game.setRunning(false);
            return false;
        }
        sender.sendMessage("Invalid arguments!");
        return false;
    }
}
