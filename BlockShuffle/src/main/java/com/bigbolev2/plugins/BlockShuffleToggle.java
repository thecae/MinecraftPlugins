package com.bigbolev2.plugins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BlockShuffleToggle implements CommandExecutor {
    // create instance of main
    private BlockShuffle blockShuffle;

    // constructor for class
    public BlockShuffleToggle(BlockShuffle blockShuffle) {
        this.blockShuffle = blockShuffle;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Invalid arguments!");
            return false;
        }
        if (args[0].equals("start")) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "BLOCK SHUFFLE IS ABOUT TO BEGIN!");
            blockShuffle.setRunning(true);
            return false;
        }
        if (args[0].equals("stop")) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "BLOCK SHUFFLE IS NOW OVER.  THANK YOU FOR PLAYING!");
            blockShuffle.setRunning(false);
            return false;
        }
        sender.sendMessage("Invalid arguments!");
        return false;
    }
}
