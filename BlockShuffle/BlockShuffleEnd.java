package com.bigbolev2.blockshuffle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BlockShuffleEnd implements CommandExecutor {
    // create instance of main
    private BlockShuffle blockShuffle;

    // constructor for class
    public BlockShuffleEnd(BlockShuffle blockShuffle) {
        this.blockShuffle = blockShuffle;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.broadcastMessage(ChatColor.GOLD + "BLOCK SHUFFLE IS NOW OVER.  THANK YOU FOR PLAYING!");

        blockShuffle.setRunning(false);
        return false;
    }
}
