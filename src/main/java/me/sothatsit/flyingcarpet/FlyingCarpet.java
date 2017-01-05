package me.sothatsit.flyingcarpet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import me.sothatsit.flyingcarpet.message.ConfigWrapper;
import me.sothatsit.flyingcarpet.message.Messages;
import me.sothatsit.flyingcarpet.model.Model;
import me.sothatsit.flyingcarpet.util.BlockData;
import me.sothatsit.flyingcarpet.util.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

public class FlyingCarpet extends org.bukkit.plugin.java.JavaPlugin implements Listener {
    private static FlyingCarpet instance;
    private Model base;
    private Model tools;
    private Model light;
    private List<BlockData> passThrough;
    private int descendSpeed;
    private List<UPlayer> players = new ArrayList();

    private boolean worldguardHooked = false;
    private Set<RegionHook> regionHooks;

    public void onEnable() {
        instance = this;

        this.regionHooks = new java.util.HashSet();

        getCommand("flyingcarpet").setExecutor(new FlyingCarpetCommand());
        Bukkit.getPluginManager().registerEvents(this, this);

        Messages.setConfig(new ConfigWrapper(this, "lang.yml"));

        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            try {
                this.regionHooks.add(new WorldGuardHook());
                this.worldguardHooked = true;
            } catch (Exception e) {
                getLogger().severe("Exception hooking WorldGuard");
                e.printStackTrace();
            }
        }

        reloadConfiguration();
    }

    public void onDisable() {
        instance = null;

        for (UPlayer up : this.players) {
            up.setEnabled(false);
        }
    }

    public boolean isCarpetAllowed(Location loc) {
        for (RegionHook hook : this.regionHooks) {
            if (!hook.isCarpetAllowed(loc)) {
                return false;
            }
        }

        return true;
    }

    public boolean isWorldGuardHooked() {
        return getWorldGuardHook() != null;
    }

    public RegionHook getWorldGuardHook() {
        for (RegionHook hook : this.regionHooks) {
            if (hook.getClass().equals(WorldGuardHook.class)) {
                return hook;
            }
        }

        return null;
    }

    public ConfigWrapper loadConfig() {
        ConfigWrapper configWrapper = new ConfigWrapper(this, "config.yml");

        configWrapper.saveDefaults();
        configWrapper.reload();

        return configWrapper;
    }

    public void reloadConfiguration() {
        ConfigWrapper configWrapper = loadConfig();

        FileConfiguration config = configWrapper.getConfig();

        if ((!config.isSet("pass-through")) || (!config.isList("pass-through"))) {
            getLogger().warning("\"pass-through\" not set or invalid in config, resetting to default");
            config.set("pass-through", configWrapper.getDefaultConfig().get("pass-through"));
            configWrapper.save();
        }

        if ((!config.isSet("descend-speed")) || (!config.isInt("descend-speed"))) {
            getLogger().warning("\"descend-speed\" not set or invalid in config, resetting to default");
            config.set("descend-speed", configWrapper.getDefaultConfig().get("descend-speed"));
            configWrapper.save();
        }

        this.descendSpeed = config.getInt("descend-speed");

        List<String> passThrough = config.getStringList("pass-through");
        this.passThrough = new ArrayList();

        this.passThrough.add(BlockData.AIR);

        for (String str : passThrough) {
            if (str.equalsIgnoreCase("water")) {
                BlockData stillWater = new BlockData(Material.STATIONARY_WATER);
                BlockData runningWater = new BlockData(Material.WATER);

                this.passThrough.add(stillWater);
                this.passThrough.add(runningWater);


            } else if (str.equalsIgnoreCase("lava")) {
                BlockData stillLava = new BlockData(Material.STATIONARY_LAVA);
                BlockData runningLava = new BlockData(Material.LAVA);

                this.passThrough.add(stillLava);
                this.passThrough.add(runningLava);

            } else {
                String[] split = str.split(":");

                if (split.length == 0) {
                    getLogger().warning("Invalid pass through block \"" + str + "\"");

                } else {

                    Material type = null;

                    int id = -1;
                    try {
                        id = Integer.valueOf(split[0]).intValue();
                        type = Material.getMaterial(id);
                    } catch (NumberFormatException e) {
                        getLogger().warning("Invalid pass through block \"" + str + "\", type must be an integer");
                    }
                    //continue;

                    if (type == null) {
                        getLogger().warning("Invalid pass through block \"" + str + "\", invalid material \"" + id + "\"");
                    } else if (split.length == 1) {
                        this.passThrough.add(new BlockData(type));
                    }
                }
            }
        }
        Material type;
        if (!config.isConfigurationSection("model")) {
            getLogger().warning("\"model\" not set or invalid in config, resetting to default");
            config.set("model", null);

            ConfigurationSection sec = config.createSection("model");
            copySection(configWrapper, config, sec);
        }

        ConfigurationSection model = config.getConfigurationSection("model");

        if (!model.isSet("base")) {
            getLogger().warning("\"model.base\" not set or invalid in config, resetting to default");
            model.set("base", null);

            ConfigurationSection sec = model.createSection("base");
            copySection(configWrapper, config, sec);
        }

        if (!model.isSet("tools")) {
            getLogger().warning("\"model.tools\" not set or invalid in config, resetting to default");
            model.set("tools", null);

            ConfigurationSection sec = model.createSection("tools");
            copySection(configWrapper, config, sec);
        }

        if (!model.isSet("light")) {
            getLogger().warning("\"model.light\" not set or invalid in config, resetting to default");
            model.set("light", null);

            ConfigurationSection sec = model.createSection("light");
            copySection(configWrapper, config, sec);
        }

        ConfigurationSection baseSec = model.getConfigurationSection("base");
        ConfigurationSection toolsSec = model.getConfigurationSection("tools");
        ConfigurationSection lightSec = model.getConfigurationSection("light");

        this.base = Model.fromConfig(baseSec);
        this.tools = Model.fromConfig(toolsSec);
        this.light = Model.fromConfig(lightSec);

        for (RegionHook hook : this.regionHooks) {
            hook.reloadConfiguration(config);
        }

        configWrapper.save();
    }

    private static void copySection(ConfigWrapper config, FileConfiguration conf, ConfigurationSection section) {
        boolean changed = false;
        FileConfiguration defaults = config.getDefaultConfig();

        for (String key : defaults.getKeys(true)) {
            if ((key.startsWith(section.getCurrentPath())) && (!conf.isSet(key))) {
                if (conf.isConfigurationSection(key)) {
                    conf.createSection(key);
                    changed = true;
                } else {
                    conf.set(key, defaults.get(key));
                    changed = true;
                }
            }
        }
        if (changed)
            config.save();
    }

    public UPlayer getUPlayer(Player p) {
        for (UPlayer up : this.players) {
            if (up.getPlayer().getUniqueId().equals(p.getUniqueId())) {
                return up;
            }
        }
        UPlayer up = new UPlayer(p);

        this.players.add(up);

        return up;
    }

    public UPlayer getUPlayer(UUID uuid) {
        for (UPlayer up : this.players) {
            if (up.getPlayer().getUniqueId().equals(uuid))
                return up;
        }
        return null;
    }

    public void removeUPlayer(Player p) {
        UPlayer up = getUPlayer(p);

        if (up == null) {
            return;
        }
        this.players.remove(up);

        up.setEnabled(false);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        removeUPlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        removeUPlayer(e.getPlayer());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        for (UPlayer up : this.players) {
            Player p = up.getPlayer();

            if (!up.isCarpetBlock(e.getBlock())) {
                if (up.isEnabled()) {
                    final UUID uuid = p.getUniqueId();
                    new BukkitRunnable() {
                        public void run() {
                            UPlayer up = FlyingCarpet.this.getUPlayer(uuid);

                            if (up == null) {
                                return;
                            }
                            up.createCarpet();
                        }
                    }

                            .runTask(this);
                }

            } else {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent e) {
        for (Iterator localIterator1 = e.getBlocks().iterator(); localIterator1.hasNext(); ) {
            Block b;
            b = (Block) localIterator1.next();
            for (UPlayer up : this.players)
                if (up.isCarpetBlock(b)) {

                    e.setCancelled(true);
                    return;
                }
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent e) {
        for (UPlayer up : this.players)
            if (up.isCarpetBlock(e.getEntity().getLocation())) {

                e.setCancelled(true);
                return;
            }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        List<Block> remove = new ArrayList();


        for (Iterator localIterator1 = e.blockList().iterator(); localIterator1.hasNext(); ) {

            Block b;
            b = (Block) localIterator1.next();
            for (UPlayer up : this.players) {
                Player p = up.getPlayer();

                if (!up.isCarpetBlock(b)) {
                    if (up.isEnabled()) {
                        final UUID uuid = p.getUniqueId();
                        new BukkitRunnable() {
                            public void run() {
                                UPlayer up = FlyingCarpet.this.getUPlayer(uuid);

                                if (up == null) {
                                    return;
                                }
                                up.createCarpet();
                            }
                        }

                                .runTask(this);
                    }


                } else
                    remove.add(b);
            }
        }

        for (Block block : remove) {
            e.blockList().remove(block);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) {
            return;
        }
        LivingEntity le;
        Location[] locs;
        if ((e.getEntity() instanceof LivingEntity)) {
            le = (LivingEntity) e.getEntity();

            locs = new Location[]{le.getLocation(), le.getEyeLocation()};
        } else {
            locs = new Location[]{e.getEntity().getLocation()};
        }
        for (Location loc : locs) {
            for (UPlayer up : this.players) {
                if (up.isCarpetBlock(loc)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        final Player p = (Player) e.getEntity();
        final UPlayer up = getUPlayer(p);
        final DamageCause cause = e.getCause();
        if (cause == DamageCause.ENTITY_ATTACK) {
            up.setEnabled(false);
            up.removeCarpet();
            return;
        }

        if (cause != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        if (up.isEnabled()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if ((e.isCancelled()) || (LocationUtils.locEqual(e.getFrom(), e.getTo()))) {
            return;
        }

        UPlayer up = getUPlayer(e.getPlayer());

        if ((LocationUtils.locColumnEqual(e.getFrom(), e.getTo())) && (Math.abs(e.getFrom().getY() - e.getTo().getY()) <= 2.0D)) {
            up.setLocation(e.getTo().clone().subtract(0.0D, 1.0D, 0.0D));
            return;
        }

        if ((up.isEnabled()) && (!e.getPlayer().hasPermission("flyingcarpet.teleport"))) {
            up.setEnabled(false);
            Messages.get("message.teleport-remove").send(e.getPlayer());
        } else {
            up.setLocation(e.getTo().clone().subtract(0.0D, 1.0D, 0.0D));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent e) {
        if ((e.isCancelled()) || (LocationUtils.locEqual(e.getFrom(), e.getTo()))) {
            return;
        }
        UPlayer up = getUPlayer(e.getPlayer());

        if ((up.isEnabled()) && (up.getLocation().getBlockY() == e.getTo().getBlockY()) && (up.isCarpetBlock(e.getTo().getBlock()))) {
            Location loc = e.getPlayer().getLocation();

            loc.setY(up.getLocation().getBlockY() + 1.2D);

            e.getPlayer().teleport(loc);
            return;
        }

        up.setLocation(e.getTo().clone().subtract(0.0D, 1.0D, 0.0D));
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) {
            getUPlayer(e.getPlayer()).cancelDescendTimer();
            return;
        }

        UPlayer up = getUPlayer(e.getPlayer());

        up.setLocation(e.getPlayer().getLocation().subtract(0.0D, 2.0D, 0.0D));
        up.createDescendTimer();
    }

    public static Model getBaseModel() {
        return instance.base;
    }

    public static Model getToolsModel() {
        return instance.tools;
    }

    public static Model getLightModel() {
        return instance.light;
    }

    public static boolean canPassThrough(Material type, byte data) {
        for (BlockData pass : instance.passThrough) {
            if ((pass.getType() == type) && ((pass.getData() < 0) || (pass.getData() == data)))
                return true;
        }
        return false;
    }

    public static int getDescendSpeed() {
        return instance.descendSpeed;
    }

    public static FlyingCarpet getInstance() {
        return instance;
    }
}
