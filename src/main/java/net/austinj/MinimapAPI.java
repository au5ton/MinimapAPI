/*     */ package me.liec0dez.MinimapAPI;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.player.PlayerChangedWorldEvent;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ public class MinimapAPI implements Listener {
/*  20 */   private static MinimapAPI instance = null;
/*     */   public static MinimapAPI getInstance() {
/*  22 */     if (instance == null) {
/*  23 */       instance = new MinimapAPI();
/*     */     }
/*  25 */     return instance;
/*     */   }
/*     */   
/*  28 */   static int id = 0;
/*  29 */   static Collection<Player> valid = new ArrayList();
/*  30 */   static Collection<Waypoint> waypoints = new ArrayList();
/*  31 */   static HashMap<Player, Integer> timer = new HashMap();
/*  32 */   private HashMap<Player, Collection<Waypoint>> players = new HashMap();
/*     */   
/*  34 */   private final int TIMEOUT = 5;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*  40 */   public void onJoin(PlayerJoinEvent e) { timer.put(e.getPlayer(), Integer.valueOf(0)); }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onQuit(PlayerQuitEvent e) {
/*  45 */     valid.remove(e.getPlayer());
/*  46 */     timer.remove(e.getPlayer());
/*  47 */     this.players.remove(e.getPlayer());
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onWorldSwitch(PlayerChangedWorldEvent e) {
/*  52 */     if (valid.contains(e.getPlayer())) {
/*     */       
/*     */       try {
/*  55 */         sendMessage(e.getPlayer(), e.getPlayer().getWorld());
/*  56 */       } catch (IOException e1) {
/*  57 */         e1.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  64 */   protected void check() { (new BukkitRunnable()
/*     */       {
/*     */         public void run()
/*     */         {
/*  68 */           if (MinimapAPI.timer.isEmpty())
/*     */             return; 
/*  70 */           for (Object o : MinimapAPI.timer.entrySet()) {
/*  71 */             Map.Entry thisEntry = (Map.Entry)o;
/*  72 */             Player key = (Player)thisEntry.getKey();
/*  73 */             Integer value = (Integer)thisEntry.getValue();
/*     */             
/*  75 */             if (value.intValue() == 5) MinimapAPI.timer.remove(key);
/*     */             
/*  77 */             MinimapAPI.timer.put(key, Integer.valueOf(value.intValue() + 1));
/*     */           } 
/*     */         }
/*  80 */       }).runTaskTimer(Main.getInstance(), 100L, 20L); }
/*     */ 
/*     */   
/*     */   public void sendWaypoint(Waypoint waypoint, Collection<Player> players) {
/*  84 */     for (Player player : players) {
/*  85 */       sendWaypoint(waypoint, player);
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendWaypoint(final Waypoint waypoint, final Player player) {
/*  90 */     if (!valid.contains(player)) {
/*  91 */       if (timer.containsKey(player)) {
/*  92 */         (new BukkitRunnable()
/*     */           {
/*     */             public void run() {
/*  95 */               MinimapAPI.this.sendWaypoint(waypoint, player);
/*     */             }
/*  97 */           }).runTaskLater(Main.getInstance(), 100L);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 103 */       sendMessage(player, waypoint, Operation.ADD);
/* 104 */     } catch (IOException e) {
/* 105 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 108 */     if (this.players.containsKey(player)) {
/* 109 */       ((Collection)this.players.get(player)).add(waypoint);
/*     */     } else {
/* 111 */       this.players.put(player, new ArrayList<Waypoint>() {  }
/*     */         );
/*     */     } 
/*     */   }
/*     */   public void removeWaypoint(Waypoint waypoint, Collection<Player> players) {
/* 116 */     for (Player player : players) {
/* 117 */       removeWaypoint(waypoint, player);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeWaypoint(Waypoint waypoint, Player player) {
/*     */     try {
/* 123 */       sendMessage(player, waypoint, Operation.REMOVE);
/* 124 */     } catch (IOException e) {
/* 125 */       e.printStackTrace();
/*     */     } 
/* 127 */     ((Collection)this.players.get(player)).remove(waypoint);
/*     */   }
/*     */   
/*     */   private void sendMessage(Player player, Waypoint waypoint, Operation op) throws IOException {
/* 131 */     ByteArrayOutputStream b = new ByteArrayOutputStream();
/* 132 */     DataOutputStream out = new DataOutputStream(b);
/*     */     
/* 134 */     switch (op) {
/*     */       
/*     */       case ADD:
/* 137 */         out.write(new byte[] { 0 });
/* 138 */         out.writeChar(65);
/* 139 */         out.writeUTF(waypoint.getLocation().getWorld().getUID().toString());
/* 140 */         out.writeInt((int)waypoint.getLocation().getX());
/* 141 */         out.writeInt((int)waypoint.getLocation().getY());
/* 142 */         out.writeInt((int)waypoint.getLocation().getZ());
/* 143 */         out.writeUTF(waypoint.getTag());
/* 144 */         out.writeChar(waypoint.getSymbol());
/* 145 */         out.write(waypoint.getColor().ordinal());
/* 146 */         out.writeInt(waypoint.getID());
/* 147 */         out.writeBoolean(waypoint.useYaw());
/* 148 */         if (waypoint.useYaw()) out.writeShort((short)(int)waypoint.getLocation().getYaw());
/*     */         
/*     */         break;
/*     */       case REMOVE:
/* 152 */         out.write(new byte[] { 0 });
/* 153 */         out.writeChar(82);
/* 154 */         out.writeInt(waypoint.getID());
/*     */         break;
/*     */     } 
/* 157 */     player.sendPluginMessage(Main.getInstance(), "XaeroMinimap", b.toByteArray());
/*     */   }
/*     */ 
/*     */   
/*     */   void sendMessage(Player player, World world) throws IOException {
/* 162 */     ByteArrayOutputStream b = new ByteArrayOutputStream();
/* 163 */     DataOutputStream out = new DataOutputStream(b);
/*     */     
/* 165 */     out.write(new byte[] { 0 });
/* 166 */     out.writeChar(87);
/* 167 */     out.writeUTF(world.getUID().toString());
/* 168 */     out.writeUTF(world.getName());
/*     */     
/* 170 */     player.sendPluginMessage(Main.getInstance(), "XaeroMinimap", b.toByteArray());
/*     */   }
/*     */   
/*     */   Waypoint searchWaypoint(String identifier) {
/* 174 */     for (Waypoint wp : waypoints) {
/* 175 */       if (wp.getIdentifier().equals(identifier)) return wp; 
/*     */     } 
/* 177 */     return null;
/*     */   }
/*     */   
/*     */   public Waypoint getWaypoint(String identifier) {
/* 181 */     Waypoint w = searchWaypoint(identifier);
/* 182 */     if (w != null) {
/* 183 */       return w;
/*     */     }
/* 185 */     Bukkit.getConsoleSender().sendMessage("[MinimapAPI] " + ChatColor.RED + "Cannot get Waypoint '" + identifier + "', it doesn't exist!");
/* 186 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteWaypoint(Waypoint waypoint) {
/* 191 */     for (Object o : this.players.entrySet()) {
/* 192 */       Map.Entry entry = (Map.Entry)o;
/* 193 */       Collection<Waypoint> value = (Collection)entry.getValue();
/*     */       
/* 195 */       if (value.contains(waypoint)) {
/* 196 */         value.remove(waypoint);
/* 197 */         removeWaypoint(waypoint, (Player)entry.getKey());
/*     */       } 
/*     */     } 
/*     */     
/* 201 */     waypoints.remove(waypoint);
/*     */   }
/*     */   
/*     */   public Collection<String> getIdentifiers(Player player) {
/* 205 */     Collection<String> identifiers = new ArrayList<String>();
/* 206 */     for (Waypoint wp : (Collection)this.players.get(player)) {
/* 207 */       identifiers.add(wp.getIdentifier());
/*     */     }
/* 209 */     return identifiers;
/*     */   }
/*     */   
/*     */   private enum Operation {
/* 213 */     ADD, REMOVE;
/*     */   }
/*     */ }


/* Location:              C:\Users\austinjckson\Downloads\MinimapAPI.jar!\me\liec0dez\MinimapAPI\MinimapAPI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */