/*    */ package me.sothatsit.flyingcarpet.message;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.Reader;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.configuration.file.YamlConfiguration;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ 
/*    */ public class ConfigWrapper
/*    */ {
/*    */   private Plugin plugin;
/* 17 */   private FileConfiguration config = null;
/* 18 */   private File configFile = null;
/*    */   private String name;
/*    */   private boolean autoReload;
/*    */   private long lastEdit;
/*    */   
/*    */   public ConfigWrapper(Plugin plugin, String name) {
/* 24 */     this.plugin = plugin;
/* 25 */     this.name = name;
/* 26 */     this.configFile = new File(plugin.getDataFolder(), name);
/* 27 */     this.autoReload = true;
/* 28 */     reload();
/*    */   }
/*    */   
/*    */   public ConfigWrapper(File file) {
/* 32 */     this.configFile = file;
/* 33 */     reload();
/*    */   }
/*    */   
/*    */   public boolean isAutoReload() {
/* 37 */     return this.autoReload;
/*    */   }
/*    */   
/*    */   public void setAutoReload(boolean autoReload) {
/* 41 */     this.autoReload = autoReload;
/*    */   }
/*    */   
/*    */   public void reload() {
/* 45 */     this.config = YamlConfiguration.loadConfiguration(this.configFile);
/* 46 */     this.lastEdit = this.configFile.lastModified();
/*    */     
/* 48 */     Reader defConfigStream = null;
/*    */     try {
/* 50 */       defConfigStream = new InputStreamReader(this.plugin.getResource(this.name), "UTF8");
/*    */     } catch (UnsupportedEncodingException e) {
/* 52 */       e.printStackTrace();
/*    */     }
/*    */     
/* 55 */     if (defConfigStream != null) {
/* 56 */       YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
/* 57 */       this.config.setDefaults(defConfig);
/*    */     }
/*    */   }
/*    */   
/*    */   public FileConfiguration getConfig() {
/* 62 */     if ((this.config == null) || ((this.autoReload) && (this.configFile.lastModified() != this.lastEdit))) {
/* 63 */       reload();
/*    */     }
/*    */     
/* 66 */     return this.config;
/*    */   }
/*    */   
/*    */   public void save() {
/* 70 */     if ((this.config == null) || (this.configFile == null)) {
/* 71 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 75 */       getConfig().save(this.configFile);
/*    */     } catch (IOException ex) {
/* 77 */       this.plugin.getLogger().log(Level.SEVERE, "Could not save config " + this.configFile, ex);
/*    */     }
/*    */   }
/*    */   
/*    */   public void saveDefaults() {
/* 82 */     if (this.plugin != null) {
/* 83 */       if (!this.configFile.exists()) {
/* 84 */         this.plugin.saveResource(this.name, false);
/*    */       }
/*    */       
/* 87 */       reload();
/*    */     }
/*    */   }
/*    */   
/*    */   public FileConfiguration getDefaultConfig() {
/*    */     try {
/* 93 */       return YamlConfiguration.loadConfiguration(new InputStreamReader(this.plugin.getResource(this.name), "UTF8"));
/*    */     } catch (UnsupportedEncodingException e) {
/* 95 */       e.printStackTrace();
/*    */     }
/*    */     
/* 98 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/message/ConfigWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */