/*    */ package me.sothatsit.flyingcarpet.message;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Argument
/*    */ {
/*    */   private String argument;
/*    */   private String value;
/*    */   
/*    */   public Argument(String argument, String value)
/*    */   {
/* 12 */     this.argument = argument;
/* 13 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getArgument() {
/* 17 */     return this.argument;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 21 */     return this.value;
/*    */   }
/*    */   
/*    */   public String replace(String str) {
/* 25 */     return str.replace(this.argument, this.value);
/*    */   }
/*    */   
/*    */   public static Argument sender(org.bukkit.command.CommandSender sender) {
/* 29 */     return new Argument("%sender%", sender.getName());
/*    */   }
/*    */   
/*    */   public static Argument player(Player p) {
/* 33 */     return new Argument("%player%", p.getName());
/*    */   }
/*    */   
/*    */   public static Argument player(String name) {
/* 37 */     return new Argument("%player%", name);
/*    */   }
/*    */   
/*    */   public static Argument valid(String valid) {
/* 41 */     return new Argument("%valid%", valid);
/*    */   }
/*    */   
/*    */   public static Argument action(String action) {
/* 45 */     return new Argument("%action%", action);
/*    */   }
/*    */ }


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/message/Argument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */