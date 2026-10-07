/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 */
package rd.dru.thread.workload;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import rd.dru.Helper;
import rd.dru.SuperHarvest;
import rd.dru.nms.NMSHandler;
import rd.dru.thread.Workload;
import rd.dru.utils.MaterialGroup;

public class TreeBreaks
implements Workload {
    Deque<Block> going = new ArrayDeque<Block>();
    Deque<Block> leaves = new ArrayDeque<Block>();
    MaterialGroup type;
    Player player;
    int cl = 0;

    public TreeBreaks(Player player, Block loc) {
        this.player = player;
        this.type = new MaterialGroup(loc.getType());
        this.chains(loc);
    }

    public TreeBreaks(Player player, Block loc, MaterialGroup mats) {
        this.player = player;
        this.type = mats;
        this.chains(loc);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean compute() {
        if (!this.player.isOnline() || this.player == null) {
            return true;
        }
        if (!this.going.isEmpty()) {
            Block b = this.going.poll();
            Material t = b.getType();
            if (!this.type.contains(t) || !b.getWorld().equals(this.player.getWorld()) || !SuperHarvest.nms.breakBlock(this.player, b) || !TreeBreaks.isAxe(this.player.getItemInHand())) return this.cancel();
            this.chains(b);
            SuperHarvest.nms.crackBlock(b, t);
            SuperHarvest.nms.playSound(b, NMSHandler.NSound.Tree);
            return this.going.isEmpty() && this.leaves.isEmpty();
        } else {
            int i = 0;
            while (!this.leaves.isEmpty() && i < 6) {
                Block b = this.leaves.poll();
                BlockBreakEvent event = new BlockBreakEvent(b, this.player);
                Bukkit.getPluginManager().callEvent((Event)event);
                if (!event.isCancelled()) {
                    b.breakNaturally();
                }
                this.chainsLeave(b);
                ++i;
            }
        }
        return this.going.isEmpty() && this.leaves.isEmpty();
    }

    private void chainsLeave(Block b) {
        ++this.cl;
        if (this.cl > 64) {
            return;
        }
        this.leaves.addAll(Helper.getNear(b).stream().filter(nb -> this.isLeaves((Block)nb) && !this.leaves.contains(nb)).collect(Collectors.toList()));
    }

    private boolean cancel() {
        SuperHarvest.thread.cach.removeAll(this.going);
        return true;
    }

    private void chains(Block b) {
        SuperHarvest.thread.cach.remove(b);
        Helper.getNear(b).stream().forEach(nb -> {
            if (this.isLeaves((Block)nb) && !this.leaves.contains(nb)) {
                this.leaves.add((Block)nb);
            } else if (this.type.contains(nb.getType()) && nb.getY() >= b.getY() && !this.going.contains(nb)) {
                SuperHarvest.thread.cach.add((Block)nb);
                this.going.add((Block)nb);
            }
        });
    }

    private boolean isLeaves(Block b) {
        return b.getType().toString().contains("LEAVES");
    }

    private static boolean isAxe(ItemStack item) {
        return item.getType().toString().contains("_AXE");
    }

    @Override
    public Player player() {
        return this.player;
    }
}

