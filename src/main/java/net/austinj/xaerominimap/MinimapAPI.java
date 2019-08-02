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
import org.bukkit.scheduler.BukkitRunnable;

public class MinimapAPI implements Listener {
    protected static final String CHANNEL_NAME = "xaerominimap:spigot_api";

    private static MinimapAPI instance = null;
    /**
     * Does a thing
     */
    public static MinimapAPI getInstance() {
        if (instance == null) {
            instance = new MinimapAPI();
        }
        return instance;
    }

    static int id = 0;
    static Collection<Player> valid = new ArrayList();
    static Collection<Waypoint> waypoints = new ArrayList();
    static HashMap<Player, Integer> timer = new HashMap();
    private HashMap<Player, Collection<Waypoint>> players = new HashMap();
    
    private final int TIMEOUT = 5;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) { timer.put(e.getPlayer(), Integer.valueOf(0)); }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        valid.remove(e.getPlayer());
        timer.remove(e.getPlayer());
        this.players.remove(e.getPlayer());
    }

    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent e) {
        if (valid.contains(e.getPlayer())) {
            try {
                sendMessage(e.getPlayer(), e.getPlayer().getWorld());
            } catch (IOException e1) {
                e1.printStackTrace();
            } 
        }
    }

    protected void check() { 
        (new BukkitRunnable() {
            public void run() {
                if (MinimapAPI.timer.isEmpty())
                    return; 
                for (Object o : MinimapAPI.timer.entrySet()) {
                    Map.Entry thisEntry = (Map.Entry)o;
                    Player key = (Player)thisEntry.getKey();
                    Integer value = (Integer)thisEntry.getValue();

                    if (value.intValue() == 5) MinimapAPI.timer.remove(key);

                    MinimapAPI.timer.put(key, Integer.valueOf(value.intValue() + 1));
                } 
            }
        }).runTaskTimer(Main.getInstance(), 100L, 20L); 
    }

    public void sendWaypoint(Waypoint waypoint, Collection<Player> players) {
        for (Player player : players) {
            sendWaypoint(waypoint, player);
        }
    }


    public void sendWaypoint(final Waypoint waypoint, final Player player) {
        if (!valid.contains(player)) {
            if (timer.containsKey(player)) {
                (new BukkitRunnable() {
                    public void run() {
                        MinimapAPI.this.sendWaypoint(waypoint, player);
                    }
                }).runTaskLater(Main.getInstance(), 100L);
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

    public void removeWaypoint(Waypoint waypoint, Collection<Player> players) {
        for (Player player : players) {
            removeWaypoint(waypoint, player);
        }
    }

    public void removeWaypoint(Waypoint waypoint, Player player) {
        try {
            sendMessage(player, waypoint, Operation.REMOVE);
        } catch (IOException e) {
            e.printStackTrace();
        } 
        ((Collection)this.players.get(player)).remove(waypoint);
    }

    private void sendMessage(Player player, Waypoint waypoint, Operation op) throws IOException {
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
        player.sendPluginMessage(Main.getInstance(), CHANNEL_NAME, b.toByteArray());
    }

    void sendMessage(Player player, World world) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        out.write(new byte[] { 0 });
        out.writeChar(87);
        out.writeUTF(world.getUID().toString());
        out.writeUTF(world.getName());

        player.sendPluginMessage(Main.getInstance(), CHANNEL_NAME, b.toByteArray());
    }

    Waypoint searchWaypoint(String identifier) {
        for (Waypoint wp : waypoints) {
            if (wp.getIdentifier().equals(identifier)) return wp; 
        } 
        return null;
    }

    public Waypoint getWaypoint(String identifier) {
        Waypoint w = searchWaypoint(identifier);
        if (w != null) {
            return w;
        }
        Bukkit.getConsoleSender().sendMessage("[MinimapAPI] " + ChatColor.RED + "Cannot get Waypoint '" + identifier + "', it doesn't exist!");
        return null;
    }

    public void deleteWaypoint(Waypoint waypoint) {
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

    public Collection<String> getIdentifiers(Player player) {
        Collection<String> identifiers = new ArrayList<String>();
        for (Waypoint wp : (Collection<Waypoint>)this.players.get(player)) {
            identifiers.add(wp.getIdentifier());
        }
        return identifiers;
    }

    private enum Operation {
        ADD, REMOVE;
    }
}
