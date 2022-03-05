package com.bigbolev2.plugins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class AchievementHunt extends JavaPlugin implements Listener {
    // status of if game is running
    private boolean isGameRunning = false;

    ArrayList<Advancement> advancementList = new ArrayList<>();

    // connect players to their advancement
    HashMap<Player, Advancement> playerMap = new HashMap<>();

    HashMap<Player, Advancement> singleConnect = new HashMap<>();

    public void setRunning(boolean toggle) { isGameRunning = toggle; }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent e) {
        singleConnect.put(e.getPlayer(), e.getAdvancement());
    }

    @Override
    public void onEnable() {
        System.out.println("[Achievement Hunt] Achievement Hunt enabled.........!");

        Bukkit.getPluginManager().registerEvents(this, this);

        getCommand("achievementhunt").setExecutor(new AchievementHuntToggle(this));

        Iterator<Advancement> advancementIterator = getServer().advancementIterator();
        // add elements to list
        while (advancementIterator.hasNext()) {
            Advancement advancement = advancementIterator.next();
            advancementList.add(advancement);
            Bukkit.broadcastMessage(advancement.toString());
        }

        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            int timeLeft = 300; // five minutes

            @Override
            public void run() {
                if (!isGameRunning) { return; }

                if (timeLeft == 300) { // game just started
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Advancement advancement = advancementList.get(new Random().nextInt(advancementList.size()));
                        playerMap.put(player, advancement);
                        player.sendMessage(ChatColor.GOLD + "You must complete the achievement: " + ChatColor.AQUA + advancement.toString());
                        player.sendMessage(ChatColor.GRAY + new AdvancementInfo(advancement).getDescription());
                    }
                }

                if (playerMap.isEmpty()) { // everyone found their block
                    // everyone has completed their achievement, continue
                    Bukkit.broadcastMessage(ChatColor.GOLD + "Everyone has completed their achievement!");
                    timeLeft = 300;
                    singleConnect.clear();
                    return;
                }

                if (timeLeft == 0) { // who hasn't finished, end game
                    for (HashMap.Entry<Player, Advancement> entry : playerMap.entrySet()) {
                        Bukkit.broadcastMessage(ChatColor.DARK_RED + entry.getKey().getDisplayName() + ChatColor.RED + " did not complete their achievement ["+ ChatColor.DARK_RED + entry.getValue().toString() + ChatColor.RED + "]!");
                        isGameRunning = false;
                        timeLeft = 300;
                        return;
                    }
                }
                else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (singleConnect.containsKey(player) && (singleConnect.get(player) == playerMap.get(player))) {
                            // broadcast player has found their block
                            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + player.getDisplayName() + ChatColor.GREEN + " completed their achievement [" + ChatColor.DARK_GREEN + playerMap.get(player).toString() + ChatColor.GREEN + "]!");
                            playerMap.remove(player);
                            singleConnect.remove(player);
                        }
                    }
                    if (timeLeft == 180 || timeLeft == 60) {
                        // send message to players that haven't found their block
                        for (HashMap.Entry<Player, Advancement> entry : playerMap.entrySet()) {
                            entry.getKey().sendMessage(ChatColor.GOLD + Integer.toString(timeLeft / 60) + " minutes remaining to complete your achievement!");
                        }
                    }
                    if (timeLeft <= 10) {
                        // if time less than 10, but on main screen and chat
                        Bukkit.broadcastMessage(ChatColor.GOLD + Integer.toString(timeLeft) + " seconds remaining!");
                        for (HashMap.Entry<Player, Advancement> entry : playerMap.entrySet()) {
                            entry.getKey().sendTitle(null, ChatColor.RED + Integer.toString(timeLeft), 5, 10, 5);
                        }
                    }
                    timeLeft--;
                }
            }
        }, 20*10, 20);
    }

    @Override
    public void onDisable() {
        System.out.println("[Achievement Hunt] Achievement Hunt disabled.........!");
    }
}
