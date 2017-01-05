/*    */ package me.sothatsit.flyingcarpet.model;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.sothatsit.flyingcarpet.util.BlockData;
/*    */ import me.sothatsit.flyingcarpet.util.Region;
/*    */ import me.sothatsit.flyingcarpet.util.Vector3I;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.block.Block;
/*    */ 
/*    */ public class CuboidElement
/*    */   extends ModelElement
/*    */ {
/*    */   private Vector3I from;
/*    */   private Vector3I to;
/*    */   
/*    */   public CuboidElement(BlockData blockData, Vector3I from, Vector3I to)
/*    */   {
/* 19 */     super(blockData);
/* 20 */     this.from = from;
/* 21 */     this.to = to;
/*    */   }
/*    */   
/*    */   public Vector3I getFrom() {
/* 25 */     return this.from;
/*    */   }
/*    */   
/*    */   public Vector3I getTo() {
/* 29 */     return this.to;
/*    */   }
/*    */   
/*    */   public List<Block> placeElement(Location loc)
/*    */   {
/* 34 */     List<Block> blocks = new ArrayList();
/*    */     
/* 36 */     for (int x = this.from.getX(); x <= this.to.getX(); x++) {
/* 37 */       for (int y = this.from.getY(); y <= this.to.getY(); y++) {
/* 38 */         for (int z = this.from.getZ(); z <= this.to.getZ(); z++) {
/* 39 */           Block b = loc.clone().add(x, y, z).getBlock();
/*    */           
/* 41 */           blocks.add(b);
/*    */           
/* 43 */           getBlockData().apply(b);
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 48 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean inBounds(Vector3I offset)
/*    */   {
/* 55 */     return (offset.getX() >= this.from.getX()) && (offset.getX() <= this.to.getX()) && (offset.getY() >= this.from.getY()) && (offset.getY() <= this.to.getY()) && (offset.getZ() >= this.from.getZ()) && (offset.getZ() <= this.to.getZ());
/*    */   }
/*    */   
/*    */   public Region getRegion()
/*    */   {
/* 60 */     return new Region(this.from, this.to);
/*    */   }
/*    */ }


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/model/CuboidElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */