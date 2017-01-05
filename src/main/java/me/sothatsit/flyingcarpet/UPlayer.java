package me.sothatsit.flyingcarpet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.sothatsit.flyingcarpet.message.Message;
import me.sothatsit.flyingcarpet.message.Messages;
import me.sothatsit.flyingcarpet.model.Model;
import me.sothatsit.flyingcarpet.util.BlockData;
import me.sothatsit.flyingcarpet.util.LocationUtils;
import me.sothatsit.flyingcarpet.util.Region;
import me.sothatsit.flyingcarpet.util.Vector3I;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class UPlayer {
    private Player player;
    private Location loc;
    private boolean enabled;
    private boolean tools;
    private boolean light;
    private List<BlockState> blocks;
    private BukkitRunnable descendTimer;

    public UPlayer(Player player) {
        this.player = player;
        this.loc = player.getLocation().subtract(0.0D, 1.0D, 0.0D);
        this.enabled = false;
        this.tools = false;
        this.light = false;
        this.blocks = new ArrayList();
    }

    public Player getPlayer() {
        return this.player;
    }

    public Location getLocation() {
        return this.loc;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isTools() {
        return this.tools;
    }

    public boolean isLight() {
        return this.light;
    }

    public BukkitRunnable getDescendTimer() {
        return this.descendTimer;
    }

    public void cancelDescendTimer() {
        if (this.descendTimer != null) {
            this.descendTimer.cancel();
            this.descendTimer = null;
        }
    }

    public void createDescendTimer() {
        cancelDescendTimer();

        this.descendTimer = new BukkitRunnable() {
            public void run() {
                if ((UPlayer.this.player == null) || (!UPlayer.this.player.isOnline())) {
                    cancel();
                    return;
                }

                UPlayer.this.setLocation(UPlayer.this.player.getLocation().subtract(0.0D, 2.0D, 0.0D));
            }

        };
        this.descendTimer.runTaskTimer(FlyingCarpet.getInstance(), FlyingCarpet.getDescendSpeed(), FlyingCarpet.getDescendSpeed());
    }

    public boolean isCarpetBlock(Block b) {
        return isCarpetBlock(b.getLocation());
    }

    public boolean isCarpetBlock(Location loc) {
        for (BlockState state : this.blocks) {
            if (LocationUtils.locEqual(state.getLocation(), loc))
                return true;
        }
        return false;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }

        this.enabled = enabled;

        if (enabled) {
            createCarpet();
        } else {
            removeCarpet();
        }
    }

    public void setLocation(Location loc) {
        if ((!this.enabled) || (LocationUtils.locEqual(this.loc, loc))) {
            this.loc = loc;
            return;
        }

        this.loc = loc;

        createCarpet();
    }

    public void setTools(boolean tools) {
        if ((!this.enabled) || (this.tools == tools)) {
            this.tools = tools;
            return;
        }

        this.tools = tools;

        createCarpet();
    }

    public void setLight(boolean light) {
        if ((!this.enabled) || (this.light == light)) {
            this.light = light;
            return;
        }

        this.light = light;

        createCarpet();
    }

    public void removeCarpet() {
        for (BlockState state : this.blocks) {
            final Block b = state.getBlock();
            b.setType(state.getType());
            b.setData(state.getRawData());
        }

        this.blocks.clear();
    }

    public void createCarpet() {
        if (!FlyingCarpet.getInstance().isCarpetAllowed(this.player.getLocation())) {
            setEnabled(false);
            Messages.get("message.region-remove").send(this.player);
            return;
        }

        List<Model> models = new ArrayList();

        models.add(FlyingCarpet.getBaseModel());

        if (this.tools) {
            models.add(FlyingCarpet.getToolsModel());
        }

        if (this.light) {
            models.add(FlyingCarpet.getLightModel());
        }

        Region[] regions = new Region[models.size()];

        for (int i = 0; i < regions.length; i++) {
            regions[i] = ((Model) models.get(i)).getRegion();
        }

        Region region = Region.combine(regions);

        List<CarpetBlock> newBlocks = new ArrayList();
        for (int x = region.getMin().getX(); x <= region.getMax().getX(); x++) {
            for (int y = region.getMin().getY(); y <= region.getMax().getY(); y++) {
                for (int z = region.getMin().getZ(); z <= region.getMax().getZ(); z++) {
                    Location l = this.loc.clone().add(x, y, z);
                    Vector3I offset = Model.getOffset(this.loc, l);

                    BlockData data = BlockData.AIR;

                    for (int i = 0; i < models.size(); i++) {
                        Model m = (Model) models.get(i);

                        BlockData d = m.getBlockData(offset);

                        if (d.getType() != Material.AIR) {
                            data = d;
                        }
                    }
                    if (data.getType() != Material.AIR) {

                        newBlocks.add(new CarpetBlock(data, l));
                    }
                }
            }
        }
        List<BlockState> states = new ArrayList();

        Iterator<BlockState> stateIterator = this.blocks.iterator();

        while (stateIterator.hasNext()) {
            BlockState state = (BlockState) stateIterator.next();

            boolean found = false;
            Iterator<CarpetBlock> carpetIterator = newBlocks.iterator();
            while (carpetIterator.hasNext()) {
                CarpetBlock block = (CarpetBlock) carpetIterator.next();

                if (LocationUtils.locEqual(state.getLocation(), block.loc)) {

                    found = true;

                    carpetIterator.remove();
                    states.add(state);

                    block.blockData.apply(state.getBlock());
                }
            }


            if (!found) {
                Block b = state.getBlock();

                b.setTypeIdAndData(state.getTypeId(), state.getRawData(), false);
            }
        }

        Iterator<CarpetBlock> carpetIterator = newBlocks.iterator();
        CarpetBlock block;
        while (carpetIterator.hasNext()) {
            block = (CarpetBlock) carpetIterator.next();

            Block b = block.loc.getBlock();

            if (!FlyingCarpet.canPassThrough(b.getType(), b.getData())) {
                carpetIterator.remove();
            }
        }

        //FIXME
        for (CarpetBlock block2 : newBlocks) {
            final Block b = block2.loc.getBlock();
            states.add(b.getState());
            block2.blockData.apply(b);
        }

        blocks.clear();
        blocks.addAll(states);
        //this.blocks = states;
    }

    private class CarpetBlock {
        public BlockData blockData;
        public Location loc;

        public CarpetBlock(BlockData blockData, Location loc) {
            this.blockData = blockData;
            this.loc = loc;
        }
    }
}


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/UPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */