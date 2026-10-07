/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package rd.dru.thread.workload;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rd.dru.Helper;
import rd.dru.SuperHarvest;
import rd.dru.thread.Workload;

public class CustomChainBreak
implements Workload {
    Deque<Block> going = new ArrayDeque<Block>();
    Material type;
    Player player;
    private static int max = 12;

    public CustomChainBreak(Player player, Block loc) {
        this.player = player;
        this.type = loc.getType();
        this.chains(loc);
    }

    @Override
    public boolean compute() {
        if (!this.player.isOnline() || this.player == null || this.going.isEmpty()) {
            return this.cancel();
        }
        Block b = this.going.poll();
        return this.going.isEmpty();
    }

    private boolean cancel() {
        SuperHarvest.thread.cach.removeAll(this.going);
        return true;
    }

    private void chains(Block b) {
        SuperHarvest.thread.cach.remove(b);
        List sels = Helper.getNear(b).stream().filter(w -> w.getType().equals((Object)this.type) && !this.going.contains(w)).collect(Collectors.toList());
        this.going.addAll(sels);
        SuperHarvest.thread.cach.addAll(sels);
    }

    private static boolean isPickaxe(ItemStack item) {
        return item.getType().toString().contains("_PICKAXE");
    }

    @Override
    public Player player() {
        return this.player;
    }
}

