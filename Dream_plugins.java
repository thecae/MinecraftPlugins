package me.cole.dream_plugins;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class Dream_plugins extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        System.out.println("dream_plugin activated.");

    }

    @EventHandler
    public void creatureSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getType() == EntityType.CREEPER) {
            entity.setInvisible(true);
        }
        if (entity.getType() == EntityType.ZOMBIE) {
            entity.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
            entity.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            entity.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
            entity.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        }
        if (entity.getType() == EntityType.SKELETON) {
            ItemStack punchbow = new ItemStack(Material.BOW);
            punchbow.addEnchantment(Enchantment.ARROW_KNOCKBACK,2);
            entity.getEquipment().setItemInMainHand(punchbow);
        }
    }

    @Override
    public void onDisable() {
        System.out.println("dream_plugin deactivated.");
    }
}
