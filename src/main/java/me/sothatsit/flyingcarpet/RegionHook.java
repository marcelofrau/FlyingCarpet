package me.sothatsit.flyingcarpet;

import java.util.Set;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public abstract interface RegionHook
{
  public abstract boolean isCarpetAllowed(Location paramLocation);
  
  public abstract void reloadConfiguration(FileConfiguration paramFileConfiguration);
  
  public abstract void addBlacklistedRegion(String paramString);
  
  public abstract void removeBlacklistedRegion(String paramString);
  
  public abstract Set<String> getBlacklistedRegions();
}


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/RegionHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */