package com.bigbolev2.manhunt;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ManhuntCompass implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            // make the compass
            ItemStack compass = new ItemStack(Material.COMPASS);
            ItemMeta compassMeta = compass.getItemMeta();
            compassMeta.setDisplayName(ChatColor.RED + "Tracker");
            compassMeta.setUnbreakable(true);
            compass.addEnchantment(Enchantment.VANISHING_CURSE, 1);

            ((Player) sender).getInventory().setItem(8, compass);
        }

        return false;
    }
}
