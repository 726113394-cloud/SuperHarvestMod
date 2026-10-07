/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package rd.dru.thread.workload;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rd.dru.Helper;
import rd.dru.SuperHarvest;
import rd.dru.nms.NMSHandler;
import rd.dru.thread.Workload;

public class CropBreaks
implements Workload {
    HashSet<Block> lands = new HashSet();
    Deque<Block> going = new ArrayDeque<Block>();
    Player player;

    public CropBreaks(Player player, Block loc) {
        this.player = player;
        this.lands.add(loc);
        this.going.add(loc);
        this.chains(loc);
    }

    @Override
    public boolean compute() {
        if (!this.player.isOnline() || this.player == null || this.going.isEmpty()) {
            return true;
        }
        Block b = this.going.poll();
        if (this.canHarvest(b)) {
            SuperHarvest.nms.crackCrop(b, b.getType());
            SuperHarvest.nms.playSound(b, NMSHandler.NSound.Farm);
            if (b.getWorld().equals(this.player.getWorld()) && SuperHarvest.nms.breakBlock(this.player, b) && this.isHoe(SuperHarvest.nms.getItemInHand(this.player))) {
                this.chains(b);
            } else {
                return this.cancel();
            }
        }
        return this.going.isEmpty();
    }

    private boolean cancel() {
        SuperHarvest.thread.cach.removeAll(this.going.stream().map(bb -> bb).collect(Collectors.toSet()));
        return true;
    }

    private void chains(Block b) {
        SuperHarvest.thread.cach.remove(b);
        List sels = Helper.getNear(b).stream().map(w -> {
            if (SuperHarvest.nms.isFarmLnad((Block)w)) {
                return w.getRelative(BlockFace.UP);
            }
            return w;
        }).filter(w -> SuperHarvest.nms.isCrop((Block)w) && !this.going.contains(w) && !w.getType().equals((Object)Material.SUGAR_CANE) || w.getType().equals((Object)Material.SUGAR_CANE) && w.getY() == b.getY() && !this.going.contains(w)).collect(Collectors.toList());
        this.going.addAll(sels);
        SuperHarvest.thread.cach.addAll(sels);
    }

    private boolean canHarvest(Block b) {
        return SuperHarvest.nms.canCropHarvest(b) || b.getType().equals((Object)Material.SUGAR_CANE);
    }

    private boolean isHoe(ItemStack item) {
        return item.getType().toString().contains("_HOE");
    }

    @Override
    public Player player() {
        return this.player;
    }
}

