/*    */ package me.sothatsit.flyingcarpet.util;
/*    */ 
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.configuration.ConfigurationSection;
/*    */ 
/*    */ public class BlockData
/*    */ {
/*  9 */   public static final BlockData AIR = new BlockData(Material.AIR, (byte)0);
/*    */   private Material type;
/*    */   private byte data;
/*    */   
/*    */   public BlockData(Material type)
/*    */   {
/* 15 */     this(type, (byte)-1);
/*    */   }
/*    */   
/*    */   public BlockData(Material type, byte data) {
/* 19 */     this.type = type;
/* 20 */     this.data = data;
/*    */   }
/*    */   
/*    */   public Material getType() {
/* 24 */     return this.type;
/*    */   }
/*    */   
/*    */   public byte getData() {
/* 28 */     return this.data;
/*    */   }
/*    */   
/*    */   public void setType(Material type) {
/* 32 */     this.type = type;
/*    */   }
/*    */   
/*    */   public void setData(byte data) {
/* 36 */     this.data = data;
/*    */   }
/*    */   
/*    */   public void apply(Block b) {
/* 40 */     if ((b.getType() == this.type) && (b.getData() == this.data)) {
/* 41 */       return;
/*    */     }
/*    */     
/* 44 */     b.setTypeIdAndData(this.type.getId(), this.data < 0 ? 0 : this.data, false);
/*    */   }
/*    */   
/*    */   public static BlockData fromSection(ConfigurationSection section) {
/* 48 */     if ((!section.isInt("block-type")) || (!section.isInt("block-data"))) {
/* 49 */       return null;
/*    */     }
/*    */     
/* 52 */     Material type = Material.getMaterial(section.getInt("block-type"));
/* 53 */     byte data = (byte)section.getInt("block-data");
/*    */     
/* 55 */     return type == null ? null : new BlockData(type, data);
/*    */   }
/*    */ }


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/util/BlockData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */