package net.austinj.xaerominimap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance = null;
    public static final String CHANNEL_NAME = "xaerominimap:spigot_api";

    public void onEnable() {
        instance = this;
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "xaerominimap:spigot_api");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "xaerominimap:spigot_api", new PacketHandler());
        Bukkit.getPluginManager().registerEvents(MinimapAPI.getInstance(), this);
        
        MinimapAPI.getInstance().check();
    }

    public void onDisable() {
        instance = null;
    }

    protected static Main getInstance() { 
        return instance;
    }
}
