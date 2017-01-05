/*    */ package me.sothatsit.flyingcarpet;
/*    */ 
/*    */ import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
/*    */ import com.sk89q.worldguard.protection.managers.RegionManager;
/*    */ import com.sk89q.worldguard.protection.regions.ProtectedRegion;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import java.util.logging.Logger;
/*    */ import me.sothatsit.flyingcarpet.message.ConfigWrapper;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.plugin.PluginManager;
/*    */ 
/*    */ public class WorldGuardHook implements RegionHook
/*    */ {
/* 18 */   private WorldGuardPlugin hook = null;
/*    */   private Set<String> regionBlacklist;
/*    */   
/*    */   public WorldGuardHook() {
/* 22 */     this.hook = ((WorldGuardPlugin)Bukkit.getPluginManager().getPlugin("WorldGuard"));
/* 23 */     this.regionBlacklist = new java.util.HashSet();
/*    */     
/* 25 */     FlyingCarpet.getInstance().getLogger().info("Hooked WorldGuard");
/*    */   }
/*    */   
/*    */   public boolean isCarpetAllowed(Location loc)
/*    */   {
/* 30 */     RegionManager manager = this.hook.getRegionManager(loc.getWorld());
/*    */     
/* 32 */     Iterator<String> iter = this.regionBlacklist.iterator();
/*    */     
/* 34 */     while (iter.hasNext()) {
/* 35 */       ProtectedRegion region = manager.getRegion((String)iter.next());
/*    */       
/* 37 */       if ((region != null) && (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))) {
/* 38 */         return false;
/*    */       }
/*    */     }
/*    */     
/* 42 */     return true;
/*    */   }
/*    */   
/*    */   public void reloadConfiguration(FileConfiguration config)
/*    */   {
/* 47 */     if ((!config.isSet("wg-region-blacklist")) || (!config.isList("wg-region-blacklist"))) {
/* 48 */       config.set("wg-region-blacklist", new ArrayList(this.regionBlacklist));
/*    */     }
/*    */     
/* 51 */     this.regionBlacklist.addAll(config.getStringList("wg-region-blacklist"));
/*    */     
/* 53 */     FlyingCarpet.getInstance().getLogger().info("Loaded " + this.regionBlacklist.size() + " blacklisted WorldGuard regions");
/*    */   }
/*    */   
/*    */   public void addBlacklistedRegion(String region)
/*    */   {
/* 58 */     this.regionBlacklist.add(region.toLowerCase());
/*    */     
/* 60 */     ConfigWrapper wrapper = FlyingCarpet.getInstance().loadConfig();
/* 61 */     wrapper.getConfig().set("wg-region-blacklist", new ArrayList(this.regionBlacklist));
/* 62 */     wrapper.save();
/*    */   }
/*    */   
/*    */   public void removeBlacklistedRegion(String region)
/*    */   {
/* 67 */     this.regionBlacklist.remove(region.toLowerCase());
/*    */     
/* 69 */     ConfigWrapper wrapper = FlyingCarpet.getInstance().loadConfig();
/* 70 */     wrapper.getConfig().set("wg-region-blacklist", new ArrayList(this.regionBlacklist));
/* 71 */     wrapper.save();
/*    */   }
/*    */   
/*    */   public Set<String> getBlacklistedRegions()
/*    */   {
/* 76 */     return this.regionBlacklist;
/*    */   }
/*    */ }


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/WorldGuardHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */