package net.austinj.xaerominimap;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MinimapAPI {
    private static JavaPlugin plugin;
    public static final String CHANNEL_NAME = "xaerominimap:spigot_api";

    // For other classes in our library
    public static JavaPlugin getPlugin() {
        return plugin;
    }

    // This method must not be used any where in the library!
    public static void setPlugin(JavaPlugin plugin) {
        MinimapAPI.plugin = plugin;
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL_NAME);
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, CHANNEL_NAME, new PacketHandler());
        Bukkit.getPluginManager().registerEvents(new MapEventListener(), plugin);

        MinimapAPI.heartbeat();
    }

    static int id = 0;
    static Collection<Player> valid = new ArrayList();
    static Collection<Waypoint> waypoints = new ArrayList();
    static HashMap<Player, Integer> timer = new HashMap();
    static HashMap<Player, Collection<Waypoint>> players = new HashMap(); // formerly private

    // Run this heartbeat every 20 ticks with a delay of 100 ticks before starting
    static void heartbeat() {
        (new BukkitRunnable() {
            public void run() {
                if (timer.isEmpty())
                    return; 
                for (Object o : timer.entrySet()) {
                    Map.Entry thisEntry = (Map.Entry)o;
                    Player key = (Player)thisEntry.getKey();
                    Integer value = (Integer)thisEntry.getValue();

                    if (value.intValue() == 5) timer.remove(key);

                    timer.put(key, Integer.valueOf(value.intValue() + 1));
                } 
            }
        }).runTaskTimer(getPlugin(), 100L, 20L); 
    }

    static void sendWaypoint(Waypoint waypoint, Collection<Player> players) {
        for (Player player : players) {
            sendWaypoint(waypoint, player);
        }
    }

    static void sendWaypoint(final Waypoint waypoint, final Player player) {
        if (!valid.contains(player)) {
            if (timer.containsKey(player)) {
                (new BukkitRunnable() {
                    public void run() {
                        MinimapAPI.sendWaypoint(waypoint, player);
                    }
                }).runTaskLater(getPlugin(), 100L);
            }

            return;
        } 
        try {
            sendMessage(player, waypoint, Operation.ADD);
        } catch (IOException e) {
            e.printStackTrace();
        } 

        if (this.players.containsKey(player)) {
            ((Collection)this.players.get(player)).add(waypoint);
        } else {
            this.players.put(player, new ArrayList<Waypoint>() {  } );
        } 
    }

    // asdasd

    static void removeWaypoint(Waypoint waypoint, Collection<Player> players) {
        for (Player player : players) {
            removeWaypoint(waypoint, player);
        }
    }

    static void removeWaypoint(Waypoint waypoint, Player player) {
        try {
            sendMessage(player, waypoint, Operation.REMOVE);
        } catch (IOException e) {
            e.printStackTrace();
        } 
        ((Collection)this.players.get(player)).remove(waypoint);
    }

    static void sendMessage(Player player, Waypoint waypoint, Operation op) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        
        switch (op) {
            case ADD: {
                out.write(new byte[] { 0 });
                out.writeChar(65);
                out.writeUTF(waypoint.getLocation().getWorld().getUID().toString());
                out.writeInt((int)waypoint.getLocation().getX());
                out.writeInt((int)waypoint.getLocation().getY());
                out.writeInt((int)waypoint.getLocation().getZ());
                out.writeUTF(waypoint.getTag());
                out.writeChar(waypoint.getSymbol());
                out.write(waypoint.getColor().ordinal());
                out.writeInt(waypoint.getID());
                out.writeBoolean(waypoint.useYaw());
                if (waypoint.useYaw()) out.writeShort((short)(int)waypoint.getLocation().getYaw());
                
                break;
            }
            case REMOVE: {
                out.write(new byte[] { 0 });
                out.writeChar(82);
                out.writeInt(waypoint.getID());
                break;
            }
        } 
        player.sendPluginMessage(Main.getInstance(), "xaerominimap:spigot_api", b.toByteArray());
    }

    static void sendMessage(Player player, World world) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        out.write(new byte[] { 0 });
        out.writeChar(87);
        out.writeUTF(world.getUID().toString());
        out.writeUTF(world.getName());

        player.sendPluginMessage(Main.getInstance(), "xaerominimap:spigot_api", b.toByteArray());
    }

    static Waypoint searchWaypoint(String identifier) {
        for (Waypoint wp : waypoints) {
            if (wp.getIdentifier().equals(identifier)) return wp; 
        } 
        return null;
    }

    static Waypoint getWaypoint(String identifier) {
        Waypoint w = searchWaypoint(identifier);
        if (w != null) {
            return w;
        }
        Bukkit.getConsoleSender().sendMessage("[MinimapAPI] " + ChatColor.RED + "Cannot get Waypoint '" + identifier + "', it doesn't exist!");
        return null;
    }

    static void deleteWaypoint(Waypoint waypoint) {
        for (Object o : this.players.entrySet()) {
            Map.Entry entry = (Map.Entry)o;
            Collection<Waypoint> value = (Collection)entry.getValue();
            
            if (value.contains(waypoint)) {
            value.remove(waypoint);
            removeWaypoint(waypoint, (Player)entry.getKey());
            } 
        } 

        waypoints.remove(waypoint);
    }

    static Collection<String> getIdentifiers(Player player) {
        Collection<String> identifiers = new ArrayList<String>();
        for (Waypoint wp : (Collection<Waypoint>)this.players.get(player)) {
            identifiers.add(wp.getIdentifier());
        }
        return identifiers;
    }

    static enum Operation {
        ADD, REMOVE;
    }
}