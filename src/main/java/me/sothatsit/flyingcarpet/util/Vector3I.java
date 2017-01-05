/*    */ package me.sothatsit.flyingcarpet.util;
/*    */ 
/*    */ import org.bukkit.configuration.ConfigurationSection;
/*    */ 
/*    */ public class Vector3I
/*    */ {
/*    */   private int x;
/*    */   private int y;
/*    */   private int z;
/*    */   
/*    */   public Vector3I(int x, int y, int z) {
/* 12 */     this.x = x;
/* 13 */     this.y = y;
/* 14 */     this.z = z;
/*    */   }
/*    */   
/*    */   public int getX() {
/* 18 */     return this.x;
/*    */   }
/*    */   
/*    */   public int getY() {
/* 22 */     return this.y;
/*    */   }
/*    */   
/*    */   public int getZ() {
/* 26 */     return this.z;
/*    */   }
/*    */   
/*    */   public void setX(int x) {
/* 30 */     this.x = x;
/*    */   }
/*    */   
/*    */   public void setY(int y) {
/* 34 */     this.y = y;
/*    */   }
/*    */   
/*    */   public void setZ(int z) {
/* 38 */     this.z = z;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 42 */     if (!(obj instanceof Vector3I)) {
/* 43 */       return super.equals(obj);
/*    */     }
/* 45 */     Vector3I other = (Vector3I)obj;
/*    */     
/* 47 */     return (other.x == this.x) && (other.y == this.y) && (other.z == this.z);
/*    */   }
/*    */   
/*    */   public static Vector3I fromConfig(ConfigurationSection section) {
/* 51 */     if ((!section.isInt("x")) || (!section.isInt("y")) || (!section.isInt("z"))) {
/* 52 */       return null;
/*    */     }
/* 54 */     int x = section.getInt("x");
/* 55 */     int y = section.getInt("y");
/* 56 */     int z = section.getInt("z");
/*    */     
/* 58 */     return new Vector3I(x, y, z);
/*    */   }
/*    */ }


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/util/Vector3I.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */