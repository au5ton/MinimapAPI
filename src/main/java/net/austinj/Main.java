/*    */ package me.liec0dez.MinimapAPI;
/*    */ 
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ 
/*    */ public class Main
/*    */   extends JavaPlugin {
/*  8 */   private static Main instance = null;
/*    */   
/* 10 */   protected static Main getInstance() { return instance; }
/*    */ 
/*    */   
/*    */   public static final String CHANNEL_NAME = "XaeroMinimap";
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 17 */     instance = this;
/* 18 */     Bukkit.getMessenger().registerOutgoingPluginChannel(this, "XaeroMinimap");
/* 19 */     Bukkit.getMessenger().registerIncomingPluginChannel(this, "XaeroMinimap", new PacketHandler());
/* 20 */     Bukkit.getPluginManager().registerEvents(MinimapAPI.getInstance(), this);
/*    */     
/* 22 */     MinimapAPI.getInstance().check();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void onDisable() { instance = null; }
/*    */ }


/* Location:              C:\Users\austinjckson\Downloads\MinimapAPI.jar!\me\liec0dez\MinimapAPI\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */