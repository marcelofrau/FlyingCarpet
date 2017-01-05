/*    */ package me.sothatsit.flyingcarpet.model;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.sothatsit.flyingcarpet.util.BlockData;
/*    */ import me.sothatsit.flyingcarpet.util.Region;
/*    */ import me.sothatsit.flyingcarpet.util.Vector3I;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.block.Block;
/*    */ 
/*    */ 
/*    */ public abstract class ModelElement
/*    */ {
/*    */   private BlockData blockData;
/*    */   
/*    */   public ModelElement(BlockData blockData)
/*    */   {
/* 17 */     this.blockData = blockData;
/*    */   }
/*    */   
/*    */   public BlockData getBlockData() {
/* 21 */     return this.blockData;
/*    */   }
/*    */   
/*    */   public void setBlockData(BlockData blockData) {
/* 25 */     this.blockData = blockData;
/*    */   }
/*    */   
/*    */   public abstract List<Block> placeElement(Location paramLocation);
/*    */   
/*    */   public abstract boolean inBounds(Vector3I paramVector3I);
/*    */   
/*    */   public abstract Region getRegion();
/*    */ }


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/model/ModelElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */