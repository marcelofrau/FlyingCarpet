/*    */ package me.sothatsit.flyingcarpet.message;
/*    */ 
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ 
/*    */ public class Messages
/*    */ {
/*  7 */   private static ConfigWrapper config = null;
/*    */   
/*    */   public static Message get(String key) {
/* 10 */     if (config == null) {
/* 11 */       return new Message(key);
/*    */     }
/*    */     
/* 14 */     Message message = new Message(key);
/*    */     
/* 16 */     if (config.getConfig().isConfigurationSection(key)) {
/* 17 */       key = key + ".default";
/*    */     }
/*    */     
/* 20 */     if (config.getConfig().isString(key)) {
/* 21 */       message = new Message(key, config.getConfig().getString(key));
/* 22 */     } else if (config.getConfig().isList(key)) {
/* 23 */       message = new Message(key, config.getConfig().getStringList(key));
/*    */     }
/*    */     
/* 26 */     return message;
/*    */   }
/*    */   
/*    */   public static void setConfig(ConfigWrapper config) {
/* 30 */     config = config;
/*    */     
/* 32 */     reload();
/*    */   }
/*    */   
/*    */   public static void reload() {
/* 36 */     if (config == null) {
/* 37 */       return;
/*    */     }
/*    */     
/* 40 */     boolean changed = false;
/* 41 */     FileConfiguration defaults = config.getDefaultConfig();
/* 42 */     FileConfiguration conf = config.getConfig();
/*    */     
/* 44 */     for (String key : defaults.getKeys(true)) {
/* 45 */       if (!conf.isSet(key)) {
/* 46 */         if (conf.isConfigurationSection(key)) {
/* 47 */           conf.createSection(key);
/* 48 */           changed = true;
/*    */         }
/*    */         else
/*    */         {
/* 52 */           conf.set(key, defaults.get(key));
/* 53 */           changed = true;
/*    */         }
/*    */       }
/*    */     }
/* 57 */     if (changed) {
/* 58 */       config.save();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/message/Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */