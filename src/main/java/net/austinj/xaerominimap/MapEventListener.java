package net.austinj.xaerominimap;

import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MapEventListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) { MinimapAPI.timer.put(e.getPlayer(), Integer.valueOf(0)); }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        MinimapAPI.valid.remove(e.getPlayer());
        MinimapAPI.timer.remove(e.getPlayer());
        MinimapAPI.players.remove(e.getPlayer());
    }

    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent e) {
        if (MinimapAPI.valid.contains(e.getPlayer())) {
            try {
                MinimapAPI.sendMessage(e.getPlayer(), e.getPlayer().getWorld());
            } catch (IOException e1) {
                e1.printStackTrace();
            } 
        }
    }
}