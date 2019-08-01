package net.austinj.xaerominimap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Waypoint {
    private Location loc;
    private String identifier;
    private String tag;
    private char symbol;

    private ChatColor color; 
    private final int id;
    private final int MAX_LENGTH = 40;
    private boolean useYaw;
    private final MinimapAPI api;

    public Waypoint(Location loc, String identifier, String tag, char symbol, ChatColor color, boolean useYaw) {
        this.MAX_LENGTH = 40;

        this.api = MinimapAPI.getInstance();

        this.loc = loc;
        this.identifier = identifier;
        this.tag = tag;
        this.symbol = symbol;
        this.color = color;
        this.useYaw = useYaw;
        this.id = MinimapAPI.id++;

        if (color.ordinal() > 15) this.color = ChatColor.WHITE; 
        if (tag.length() > 40) this.tag = tag.substring(0, 40);


        if (this.api.searchWaypoint(identifier) != null) {
            Bukkit.getConsoleSender().sendMessage("[MinimapAPI] " + ChatColor.RED + "Cannot create waypoint '" + identifier + "', it already exists! Please use getWaypoint method instead");
            throw new IllegalArgumentException("Waypoint identifier '" + identifier + "' already in use!");
        } 
        MinimapAPI.waypoints.add(this);
    }

    public Location getLocation() { return this.loc; }
    public String getIdentifier() { return this.identifier; }
    public String getTag() { return this.tag; }
    public char getSymbol() { return this.symbol; }
    public ChatColor getColor() { return this.color; }
    protected int getID() { return this.id; }
    public boolean useYaw() { return this.useYaw; }
}