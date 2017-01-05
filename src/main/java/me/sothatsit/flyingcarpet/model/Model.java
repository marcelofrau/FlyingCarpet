/*     */ package me.sothatsit.flyingcarpet.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import me.sothatsit.flyingcarpet.FlyingCarpet;
/*     */ import me.sothatsit.flyingcarpet.util.BlockData;
/*     */ import me.sothatsit.flyingcarpet.util.Region;
/*     */ import me.sothatsit.flyingcarpet.util.Vector3I;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ 
/*     */ public class Model
/*     */ {
/*     */   private List<ModelElement> elements;
/*     */   
/*     */   public Model(List<ModelElement> elements)
/*     */   {
/*  19 */     this.elements = elements;
/*     */   }
/*     */   
/*     */   public List<ModelElement> getElements() {
/*  23 */     return this.elements;
/*     */   }
/*     */   
/*     */   public Region getRegion() {
/*  27 */     Region[] regions = new Region[this.elements.size()];
/*     */     
/*  29 */     for (int i = 0; i < regions.length; i++) {
/*  30 */       regions[i] = ((ModelElement)this.elements.get(i)).getRegion();
/*     */     }
/*     */     
/*  33 */     return Region.combine(regions);
/*     */   }
/*     */   
/*     */   public BlockData getBlockData(Vector3I offset) {
/*  37 */     BlockData data = BlockData.AIR;
/*     */     
/*  39 */     for (ModelElement element : this.elements) {
/*  40 */       if (element.inBounds(offset)) {
/*  41 */         data = element.getBlockData();
/*     */       }
/*     */     }
/*  44 */     return data;
/*     */   }
/*     */   
/*     */   public static Vector3I getOffset(Location centre, Location loc) {
/*  48 */     return new Vector3I(loc.getBlockX() - centre.getBlockX(), loc.getBlockY() - centre.getBlockY(), loc.getBlockZ() - centre.getBlockZ());
/*     */   }
/*     */   
/*     */   public static Model fromConfig(ConfigurationSection sec) {
/*  52 */     List<ModelElement> elements = new ArrayList();
/*     */     
/*  54 */     for (String key : sec.getKeys(false))
/*  55 */       if (sec.isConfigurationSection(key))
/*     */       {
/*     */ 
/*  58 */         ConfigurationSection elementSec = sec.getConfigurationSection(key);
/*     */         
/*  60 */         if (!elementSec.isString("model-type")) {
/*  61 */           FlyingCarpet.getInstance().getLogger().warning("Invalid model element for model \"" + sec.getName() + "\" > \"model-type\" not set or invalid");
/*     */         }
/*     */         else
/*     */         {
/*  65 */           BlockData data = BlockData.fromSection(elementSec);
/*     */           
/*  67 */           if (data == null) {
/*  68 */             FlyingCarpet.getInstance().getLogger().warning("Invalid model element for model \"" + sec.getName() + "\" > block data not set or invalid");
/*     */           }
/*     */           else
/*     */           {
/*  72 */             String modelType = elementSec.getString("model-type");
/*     */             
/*  74 */             if (modelType.equalsIgnoreCase("block")) {
/*  75 */               if (!elementSec.isConfigurationSection("offset")) {
/*  76 */                 FlyingCarpet.getInstance().getLogger().warning("Invalid model element for model \"" + sec.getName() + "\" > \"offset\" not set");
/*     */               }
/*     */               else
/*     */               {
/*  80 */                 Vector3I offset = Vector3I.fromConfig(elementSec.getConfigurationSection("offset"));
/*     */                 
/*  82 */                 if (offset == null) {
/*  83 */                   FlyingCarpet.getInstance().getLogger().warning("Invalid model element for model \"" + sec.getName() + "\" > \"offset\" invalid");
/*     */                 }
/*     */                 else
/*     */                 {
/*  87 */                   BlockElement element = new BlockElement(data, offset);
/*     */                   
/*  89 */                   elements.add(element);
/*     */                 }
/*     */               }
/*     */             }
/*  93 */             else if (modelType.equalsIgnoreCase("cuboid")) {
/*  94 */               if (!elementSec.isConfigurationSection("from")) {
/*  95 */                 FlyingCarpet.getInstance().getLogger().warning("Invalid model element for model \"" + sec.getName() + "\" > \"from\" not set");
/*     */ 
/*     */ 
/*     */               }
/*  99 */               else if (!elementSec.isConfigurationSection("to")) {
/* 100 */                 FlyingCarpet.getInstance().getLogger().warning("Invalid model element for model \"" + sec.getName() + "\" > \"to\" not set");
/*     */               }
/*     */               else
/*     */               {
/* 104 */                 Vector3I from = Vector3I.fromConfig(elementSec.getConfigurationSection("from"));
/*     */                 
/* 106 */                 if (from == null) {
/* 107 */                   FlyingCarpet.getInstance().getLogger().warning("Invalid model element for model \"" + sec.getName() + "\" > \"from\" invalid");
/*     */                 }
/*     */                 else
/*     */                 {
/* 111 */                   Vector3I to = Vector3I.fromConfig(elementSec.getConfigurationSection("to"));
/*     */                   
/* 113 */                   if (to == null) {
/* 114 */                     FlyingCarpet.getInstance().getLogger().warning("Invalid model element for model \"" + sec.getName() + "\" > \"to\" invalid");
/*     */                   }
/*     */                   else
/*     */                   {
/* 118 */                     CuboidElement element = new CuboidElement(data, from, to);
/*     */                     
/* 120 */                     elements.add(element);
/*     */                   }
/*     */                 }
/*     */               }
/*     */             } else
/* 125 */               FlyingCarpet.getInstance().getLogger().warning("Invalid model element for model \"" + sec.getName() + "\" > invalid model type, valid are \"block\" and \"cuboid\"");
/*     */           }
/*     */         } }
/* 128 */     return new Model(elements);
/*     */   }
/*     */ }


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/model/Model.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */