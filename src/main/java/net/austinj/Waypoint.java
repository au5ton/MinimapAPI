/*    */ package me.liec0dez.MinimapAPI;
/*    */ 
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Location;
/*    */ 
/*    */ public class Waypoint {
/*    */   private Location loc;
/*    */   private String identifier;
/*    */   private String tag;
/*    */   private char symbol;
/*    */   
/*    */   public Waypoint(Location loc, String identifier, String tag, char symbol, ChatColor color, boolean useYaw) {
/* 14 */     this.MAX_LENGTH = 40;
/*    */     
/* 16 */     this.api = MinimapAPI.getInstance();
/*    */ 
/*    */     
/* 19 */     this.loc = loc;
/* 20 */     this.identifier = identifier;
/* 21 */     this.tag = tag;
/* 22 */     this.symbol = symbol;
/* 23 */     this.color = color;
/* 24 */     this.useYaw = useYaw;
/* 25 */     this.id = MinimapAPI.id++;
/*    */     
/* 27 */     if (color.ordinal() > 15) this.color = ChatColor.WHITE; 
/* 28 */     if (tag.length() > 40) this.tag = tag.substring(0, 40);
/*    */ 
/*    */     
/* 31 */     if (this.api.searchWaypoint(identifier) != null) {
/* 32 */       Bukkit.getConsoleSender().sendMessage("[MinimapAPI] " + ChatColor.RED + "Cannot create waypoint '" + identifier + "', it already exists! Please use getWaypoint method instead");
/* 33 */       throw new IllegalArgumentException("Waypoint identifier '" + identifier + "' already in use!");
/*    */     } 
/* 35 */     MinimapAPI.waypoints.add(this);
/*    */   }
/*    */   private ChatColor color; private final int id; private final int MAX_LENGTH = 40; private boolean useYaw; private final MinimapAPI api;
/*    */   
/* 39 */   public Location getLocation() { return this.loc; }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public String getIdentifier() { return this.identifier; }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public String getTag() { return this.tag; }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public char getSymbol() { return this.symbol; }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public ChatColor getColor() { return this.color; }
/*    */ 
/*    */ 
/*    */   
/* 59 */   protected int getID() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public boolean useYaw() { return this.useYaw; }
/*    */ }


/* Location:              C:\Users\austinjckson\Downloads\MinimapAPI.jar!\me\liec0dez\MinimapAPI\Waypoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */