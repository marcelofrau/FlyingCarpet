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
/*    */ public class BlockElement
/*    */   extends ModelElement
/*    */ {
/*    */   private Vector3I offset;
/*    */   
/*    */   public BlockElement(BlockData blockData, Vector3I offset)
/*    */   {
/* 18 */     super(blockData);
/* 19 */     this.offset = offset;
/*    */   }
/*    */   
/*    */   public Vector3I getOffset() {
/* 23 */     return this.offset;
/*    */   }
/*    */   
/*    */   public List<Block> placeElement(Location loc)
/*    */   {
/* 28 */     List<Block> blocks = new ArrayList();
/*    */     
/* 30 */     Block b = loc.clone().add(this.offset.getX(), this.offset.getY(), this.offset.getZ()).getBlock();
/*    */     
/* 32 */     blocks.add(b);
/*    */     
/* 34 */     getBlockData().apply(b);
/*    */     
/* 36 */     return null;
/*    */   }
/*    */   
/*    */   public boolean inBounds(Vector3I offset)
/*    */   {
/* 41 */     return this.offset.equals(offset);
/*    */   }
/*    */   
/*    */   public Region getRegion()
/*    */   {
/* 46 */     return new Region(this.offset, this.offset);
/*    */   }
/*    */ }


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/model/BlockElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */