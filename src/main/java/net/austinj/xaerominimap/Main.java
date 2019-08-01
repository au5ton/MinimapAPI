package net.austinj.xaerominimap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance = null;
    public static final String CHANNEL_NAME = "XaeroMinimap";

    public void onEnable() {
        instance = this;
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "XaeroMinimap");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "XaeroMinimap", new PacketHandler());
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
