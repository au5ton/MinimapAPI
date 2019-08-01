package net.austinj.xaerominimap;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

class PacketHandler implements PluginMessageListener {
    public void onPluginMessageReceived(String s, Player player, byte[] message) {
        if (message[2] == 72) {
            MinimapAPI.valid.add(player);
            MinimapAPI.timer.remove(player);
            try {
                MinimapAPI.getInstance().sendMessage(player, player.getWorld());
            } catch (IOException e1) {
                e1.printStackTrace();
            } 
        } 
    }
}
