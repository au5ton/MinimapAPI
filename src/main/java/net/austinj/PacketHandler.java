/*    */ package me.liec0dez.MinimapAPI;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.plugin.messaging.PluginMessageListener;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PacketHandler
/*    */   implements PluginMessageListener
/*    */ {
/*    */   public void onPluginMessageReceived(String s, Player player, byte[] message) {
/* 14 */     if (message[2] == 72) {
/* 15 */       MinimapAPI.valid.add(player);
/* 16 */       MinimapAPI.timer.remove(player);
/*    */       try {
/* 18 */         MinimapAPI.getInstance().sendMessage(player, player.getWorld());
/* 19 */       } catch (IOException e1) {
/* 20 */         e1.printStackTrace();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\austinjckson\Downloads\MinimapAPI.jar!\me\liec0dez\MinimapAPI\PacketHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */