/*    */ package me.sothatsit.flyingcarpet.util;
/*    */ 
/*    */ import org.bukkit.Location;
/*    */ 
/*    */ public class Region
/*    */ {
/*    */   private Vector3I min;
/*    */   private Vector3I max;
/*    */   
/*    */   public Region(Vector3I from, Vector3I to) {
/* 11 */     this.min = new Vector3I(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
/* 12 */     this.max = new Vector3I(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
/*    */   }
/*    */   
/*    */   public Vector3I getMin() {
/* 16 */     return this.min;
/*    */   }
/*    */   
/*    */   public Vector3I getMax() {
/* 20 */     return this.max;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean inBounds(Location loc)
/*    */   {
/* 26 */     return (loc.getX() >= this.min.getX()) && (loc.getX() <= this.max.getX() + 1) && (loc.getY() >= this.min.getY()) && (loc.getY() <= this.max.getY() + 1) && (loc.getZ() >= this.min.getZ()) && (loc.getZ() <= this.max.getZ() + 1);
/*    */   }
/*    */   
/*    */   public static Region combine(Region... regions) {
/* 30 */     if (regions.length == 0) {
/* 31 */       return null;
/*    */     }
/* 33 */     int minx = Integer.MAX_VALUE;
/* 34 */     int miny = Integer.MAX_VALUE;
/* 35 */     int minz = Integer.MAX_VALUE;
/*    */     
/* 37 */     int maxx = Integer.MIN_VALUE;
/* 38 */     int maxy = Integer.MIN_VALUE;
/* 39 */     int maxz = Integer.MIN_VALUE;
/*    */     
/* 41 */     for (Region r : regions) {
/* 42 */       minx = Math.min(r.getMin().getX(), minx);
/* 43 */       miny = Math.min(r.getMin().getY(), miny);
/* 44 */       minz = Math.min(r.getMin().getZ(), minz);
/*    */       
/* 46 */       maxx = Math.max(r.getMax().getX(), maxx);
/* 47 */       maxy = Math.max(r.getMax().getY(), maxy);
/* 48 */       maxz = Math.max(r.getMax().getZ(), maxz);
/*    */     }
/*    */     
/* 51 */     return new Region(new Vector3I(minx, miny, minz), new Vector3I(maxx, maxy, maxz));
/*    */   }
/*    */ }


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/util/Region.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */