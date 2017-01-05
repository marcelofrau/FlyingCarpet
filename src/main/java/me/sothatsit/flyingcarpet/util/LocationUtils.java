/*     */ package me.sothatsit.flyingcarpet.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocationUtils
/*     */ {
/*     */   public static List<Location> getCircle(Location centerLoc, int radius)
/*     */   {
/*  37 */     List<Location> circle = new ArrayList();
/*  38 */     World world = centerLoc.getWorld();
/*  39 */     int x = 0;
/*  40 */     int z = radius;
/*  41 */     int error = 0;
/*  42 */     int d = 2 - 2 * radius;
/*  43 */     while (z >= 0)
/*     */     {
/*  45 */       circle.add(new Location(world, centerLoc.getBlockX() + x, centerLoc.getY(), centerLoc.getBlockZ() + z));
/*  46 */       circle.add(new Location(world, centerLoc.getBlockX() - x, centerLoc.getY(), centerLoc.getBlockZ() + z));
/*  47 */       circle.add(new Location(world, centerLoc.getBlockX() - x, centerLoc.getY(), centerLoc.getBlockZ() - z));
/*  48 */       circle.add(new Location(world, centerLoc.getBlockX() + x, centerLoc.getY(), centerLoc.getBlockZ() - z));
/*  49 */       error = 2 * (d + z) - 1;
/*  50 */       if ((d < 0) && (error <= 0))
/*     */       {
/*  52 */         x++;
/*  53 */         d += 2 * x + 1;
/*     */       }
/*     */       else
/*     */       {
/*  57 */         error = 2 * (d - x) - 1;
/*  58 */         if ((d > 0) && (error > 0))
/*     */         {
/*  60 */           z--;
/*  61 */           d += 1 - 2 * z;
/*     */         }
/*     */         else
/*     */         {
/*  65 */           x++;
/*  66 */           d += 2 * (x - z);
/*  67 */           z--;
/*     */         }
/*     */       }
/*     */     }
/*  71 */     return circle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<Location> getCuboid(Location position1, Location position2)
/*     */   {
/*  87 */     if (position1.getWorld().getName() != position2.getWorld().getName())
/*     */     {
/*  89 */       throw new UnsupportedOperationException("'Position1' and 'Position2' location need to be in the same world!");
/*     */     }
/*     */     
/*  92 */     List<Location> cube = new ArrayList();
/*     */     
/*  94 */     int minX = (int)Math.min(position1.getX(), position2.getX());
/*  95 */     int maxX = (int)Math.max(position1.getX(), position2.getX());
/*     */     
/*  97 */     int minY = (int)Math.min(position1.getY(), position2.getY());
/*  98 */     int maxY = (int)Math.max(position1.getY(), position2.getY());
/*     */     
/* 100 */     int minZ = (int)Math.min(position1.getZ(), position2.getZ());
/* 101 */     int maxZ = (int)Math.max(position1.getZ(), position2.getZ());
/*     */     
/* 103 */     for (int x = minX; x <= maxX; x++)
/*     */     {
/* 105 */       for (int y = minY; y <= maxY; y++)
/*     */       {
/* 107 */         for (int z = minZ; z <= maxZ; z++)
/*     */         {
/* 109 */           cube.add(new Location(position1.getWorld(), x, y, z));
/*     */         }
/*     */       }
/*     */     }
/* 113 */     return cube;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<Location> getPlain(Location position1, Location position2)
/*     */   {
/* 128 */     List<Location> plain = new ArrayList();
/* 129 */     if (position1 == null)
/* 130 */       return plain;
/* 131 */     if (position2 == null)
/* 132 */       return plain;
/* 133 */     for (int x = Math.min(position1.getBlockX(), position2.getBlockX()); x <= Math.max(position1.getBlockX(), position2.getBlockX()); x++)
/* 134 */       for (int z = Math.min(position1.getBlockZ(), position2.getBlockZ()); z <= Math.max(position1.getBlockZ(), position2.getBlockZ()); z++)
/* 135 */         plain.add(new Location(position1.getWorld(), x, position1.getBlockY(), z));
/* 136 */     return plain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<Location> getBlocks(Location position1, Location position2, boolean getOnlyAboveGround)
/*     */   {
/* 155 */     List<Location> blocks = new ArrayList();
/* 156 */     if (position1 == null)
/* 157 */       return blocks;
/* 158 */     if (position2 == null) {
/* 159 */       return blocks;
/*     */     }
/* 161 */     for (int x = Math.min(position1.getBlockX(), position2.getBlockX()); x <= Math.max(position1.getBlockX(), position2.getBlockX()); x++)
/* 162 */       for (int z = Math.min(position1.getBlockZ(), position2.getBlockZ()); z <= Math.max(position1.getBlockZ(), position2.getBlockZ()); z++)
/* 163 */         for (int y = Math.min(position1.getBlockY(), position2.getBlockY()); y <= Math.max(position1.getBlockY(), position2.getBlockY()); y++)
/*     */         {
/* 165 */           Block b = position1.getWorld().getBlockAt(x, y, z);
/* 166 */           if ((b.getType() == Material.AIR) && ((!getOnlyAboveGround) || 
/* 167 */             (b.getRelative(BlockFace.DOWN).getType() != Material.AIR)))
/* 168 */             blocks.add(b.getLocation());
/*     */         }
/* 170 */     return blocks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<Location> getLine(Location position1, Location position2)
/*     */   {
/* 185 */     List<Location> line = new ArrayList();
/* 186 */     int dx = Math.max(position1.getBlockX(), position2.getBlockX()) - Math.min(position1.getBlockX(), position2.getBlockX());
/* 187 */     int dy = Math.max(position1.getBlockY(), position2.getBlockY()) - Math.min(position1.getBlockY(), position2.getBlockY());
/* 188 */     int dz = Math.max(position1.getBlockZ(), position2.getBlockZ()) - Math.min(position1.getBlockZ(), position2.getBlockZ());
/* 189 */     int x1 = position1.getBlockX();
/* 190 */     int x2 = position2.getBlockX();
/* 191 */     int y1 = position1.getBlockY();
/* 192 */     int y2 = position2.getBlockY();
/* 193 */     int z1 = position1.getBlockZ();
/* 194 */     int z2 = position2.getBlockZ();
/* 195 */     int x = 0;
/* 196 */     int y = 0;
/* 197 */     int z = 0;
/* 198 */     int i = 0;
/* 199 */     int d = 1;
/* 200 */     switch (getHighest(dx, dy, dz))
/*     */     {
/*     */     case 1: 
/* 203 */       i = 0;
/* 204 */       d = 1;
/* 205 */       if (x1 > x2)
/* 206 */         d = -1;
/* 207 */       x = position1.getBlockX();
/*     */       do
/*     */       {
/* 210 */         i++;
/* 211 */         y = y1 + (x - x1) * (y2 - y1) / (x2 - x1);
/* 212 */         z = z1 + (x - x1) * (z2 - z1) / (x2 - x1);
/* 213 */         line.add(new Location(position1.getWorld(), x, y, z));
/* 214 */         x += d;
/* 215 */       } while (i <= Math.max(x1, x2) - Math.min(x1, x2));
/* 216 */       break;
/*     */     case 2: 
/* 218 */       i = 0;
/* 219 */       d = 1;
/* 220 */       if (y1 > y2)
/* 221 */         d = -1;
/* 222 */       y = position1.getBlockY();
/*     */       do
/*     */       {
/* 225 */         i++;
/* 226 */         x = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
/* 227 */         z = z1 + (y - y1) * (z2 - z1) / (y2 - y1);
/* 228 */         line.add(new Location(position1.getWorld(), x, y, z));
/* 229 */         y += d;
/* 230 */       } while (i <= Math.max(y1, y2) - Math.min(y1, y2));
/* 231 */       break;
/*     */     case 3: 
/* 233 */       i = 0;
/* 234 */       d = 1;
/* 235 */       if (z1 > z2)
/* 236 */         d = -1;
/* 237 */       z = position1.getBlockZ();
/*     */       do
/*     */       {
/* 240 */         i++;
/* 241 */         y = y1 + (z - z1) * (y2 - y1) / (z2 - z1);
/* 242 */         x = x1 + (z - z1) * (x2 - x1) / (z2 - z1);
/* 243 */         line.add(new Location(position1.getWorld(), x, y, z));
/* 244 */         z += d;
/* 245 */       } while (i <= Math.max(z1, z2) - Math.min(z1, z2));
/*     */     }
/*     */     
/* 248 */     return line;
/*     */   }
/*     */   
/*     */ 
/*     */   private static int getHighest(int x, int y, int z)
/*     */   {
/* 254 */     if ((x >= y) && (x >= z))
/* 255 */       return 1;
/* 256 */     if ((y >= x) && (y >= z))
/* 257 */       return 2;
/* 258 */     return 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashSet<LivingEntity> getNearbyEntities(Location location, int radius)
/*     */   {
/* 275 */     int chunkRadius = radius < 16 ? 1 : (radius - radius % 16) / 16;
/* 276 */     HashSet<LivingEntity> radiusEntities = new HashSet();
/*     */     
/* 278 */     for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++)
/*     */     {
/* 280 */       for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++)
/*     */       {
/* 282 */         int x = (int)location.getX();int y = (int)location.getY();int z = (int)location.getZ();
/* 283 */         for (Entity e : new Location(location.getWorld(), x + chX * 16, y, z + chZ * 16).getChunk().getEntities())
/*     */         {
/* 285 */           if ((e.getLocation().distance(location) <= radius) && (e.getLocation().getBlock() != location.getBlock()) && 
/* 286 */             ((e instanceof LivingEntity)))
/*     */           {
/* 288 */             radiusEntities.add((LivingEntity)e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 293 */     return radiusEntities;
/*     */   }
/*     */   
/*     */ 
/*     */   public static int[] getAsArray(Location loc)
/*     */   {
/* 299 */     return new int[] {loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() };
/*     */   }
/*     */   
/*     */   public static boolean locEqual(Location l1, Location l2)
/*     */   {
/* 304 */     int[] a = getAsArray(l1);
/* 305 */     int[] b = getAsArray(l2);
/* 306 */     return (a[0] == b[0]) && (a[1] == b[1]) && (a[2] == b[2]);
/*     */   }
/*     */   
/*     */   public static boolean locColumnEqual(Location l1, Location l2)
/*     */   {
/* 311 */     int[] a = getAsArray(l1);
/* 312 */     int[] b = getAsArray(l2);
/* 313 */     return (a[0] == b[0]) && (a[2] == b[2]);
/*     */   }
/*     */   
/*     */   public static boolean withinRadius(Location l1, Location l2, int radius)
/*     */   {
/* 318 */     return l1.distanceSquared(l2) <= radius * radius;
/*     */   }
/*     */   
/*     */   public static String locsToString(List<Location> locs)
/*     */   {
/* 323 */     String str = "";
/* 324 */     for (Location l : locs)
/*     */     {
/* 326 */       if (!str.isEmpty())
/* 327 */         str = str + "|";
/* 328 */       str = str + locToString(l);
/*     */     }
/* 330 */     return str;
/*     */   }
/*     */   
/*     */   public static List<Location> locsFromString(String str)
/*     */   {
/* 335 */     String[] split = str.split("|");
/* 336 */     List<Location> locs = new ArrayList();
/* 337 */     for (String loc : split)
/*     */     {
/* 339 */       locs.add(locFromString(loc));
/*     */     }
/* 341 */     return locs;
/*     */   }
/*     */   
/*     */   public static List<Location> locsFromStringList(List<String> locs)
/*     */   {
/* 346 */     List<Location> locations = new ArrayList();
/*     */     
/* 348 */     for (String loc : locs)
/*     */     {
/* 350 */       Location l = locFromString(loc);
/*     */       
/* 352 */       if (l != null)
/*     */       {
/*     */ 
/* 355 */         locations.add(l);
/*     */       }
/*     */     }
/* 358 */     return locations;
/*     */   }
/*     */   
/*     */   public static List<String> locsToStringList(List<Location> locs)
/*     */   {
/* 363 */     List<String> locations = new ArrayList();
/*     */     
/* 365 */     for (Location loc : locs)
/*     */     {
/* 367 */       String l = locToString(loc);
/*     */       
/* 369 */       locations.add(l);
/*     */     }
/*     */     
/* 372 */     return locations;
/*     */   }
/*     */   
/*     */   public static String locToString(Location loc)
/*     */   {
/* 377 */     if ((loc == null) || (loc.getWorld() == null)) {
/* 378 */       return "null";
/*     */     }
/*     */     
/* 381 */     Object[] list = { loc.getWorld().getName(), Double.valueOf(loc.getX()), Double.valueOf(loc.getY()), Double.valueOf(loc.getZ()), Float.valueOf(loc.getYaw()), Float.valueOf(loc.getPitch()) };
/* 382 */     String str = "";
/* 383 */     for (int i = 0; i < list.length; i++)
/*     */     {
/* 385 */       if (i != 0)
/* 386 */         str = str + ",";
/* 387 */       str = str + list[i].toString();
/*     */     }
/* 389 */     return str;
/*     */   }
/*     */   
/*     */   public static Location locFromString(String str)
/*     */   {
/* 394 */     if (str.equals("null")) {
/* 395 */       return null;
/*     */     }
/* 397 */     String[] split = str.split(",");
/* 398 */     if (split.length != 6)
/* 399 */       return null;
/* 400 */     World w = Bukkit.getWorld(split[0]);
/* 401 */     double x = Double.valueOf(split[1]).doubleValue();
/* 402 */     double y = Double.valueOf(split[2]).doubleValue();
/* 403 */     double z = Double.valueOf(split[3]).doubleValue();
/* 404 */     float yaw = Float.valueOf(split[4]).floatValue();
/* 405 */     float pitch = Float.valueOf(split[5]).floatValue();
/*     */     
/* 407 */     Location loc = new Location(w, x, y, z, yaw, pitch);
/* 408 */     return loc;
/*     */   }
/*     */   
/*     */   public static List<Location> getLocsInRadius(List<Location> list, Location loc, int radius)
/*     */   {
/* 413 */     List<Location> locs = new ArrayList();
/* 414 */     for (Location l : list)
/*     */     {
/* 416 */       if (l.distanceSquared(loc) <= radius * radius)
/* 417 */         locs.add(l);
/*     */     }
/* 419 */     return locs;
/*     */   }
/*     */   
/*     */   public static Map<Integer, Location> getLocsInRadiusWithIndex(List<Location> locs, Location center, int radius)
/*     */   {
/* 424 */     Map<Integer, Location> near = new HashMap();
/*     */     
/* 426 */     for (int i = 0; i < locs.size(); i++)
/*     */     {
/* 428 */       Location loc = (Location)locs.get(i);
/*     */       
/* 430 */       if (center.distanceSquared(loc) <= radius * radius)
/*     */       {
/*     */ 
/* 433 */         near.put(Integer.valueOf(i), loc);
/*     */       }
/*     */     }
/* 436 */     return near;
/*     */   }
/*     */   
/*     */ 
/*     */   public static Location[] getCorners(Location min, Location max)
/*     */   {
/* 442 */     int[] dif = { max.getBlockX() - min.getBlockX(), max.getBlockY() - min.getBlockY(), max.getBlockZ() - min.getBlockZ() };
/*     */     
/* 444 */     List<Location> locs = new ArrayList();
/*     */     
/* 446 */     for (int i = 0; i < 8; i++)
/*     */     {
/* 448 */       boolean x = i % 2 >= 1;
/* 449 */       boolean y = i % 4 >= 2;
/* 450 */       boolean z = i % 8 >= 4;
/*     */       
/* 452 */       locs.add(min.clone().add(x ? dif[0] : 0, y ? dif[1] : 0, z ? dif[2] : 0));
/*     */     }
/*     */     
/* 455 */     return (Location[])locs.toArray(new Location[0]);
/*     */   }
/*     */ }


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/util/LocationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */