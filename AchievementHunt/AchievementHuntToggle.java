package com.bigbolev2.plugins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AchievementHuntToggle implements CommandExecutor {
    // create instance of main
    private AchievementHunt hunt;

    // constructor for class
    public AchievementHuntToggle(AchievementHunt hunt) { this.hunt = hunt; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Invalid arguments!");
            return false;
        }
        if (args[0].equals("start")) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "ACHIEVEMENT HUNT IS ABOUT TO BEGIN!");
            hunt.setRunning(true);
            return false;
        }
        if (args[0].equals("stop")) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "ACHIEVEMENT HUNT IS NOW OVER.  THANK YOU FOR PLAYING!");
            hunt.setRunning(false);
            return false;
        }
        sender.sendMessage("Invalid arguments!");
        return false;
    }
}
