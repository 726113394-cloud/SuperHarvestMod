/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.ExperienceOrb
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package rd.dru.nms;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import rd.dru.SuperHarvest;
import rd.dru.nms.BlockExpDropTable;
import rd.dru.nms.VersionChecker;

public interface NMSHandler {
    public static NMSHandler getNMSHandler() {
        return VersionChecker.getCurrentVersion().getNMS();
    }

    default public void init(SuperHarvest sh) {
    }

    default public void onDisable() {
    }

    default public boolean breakBlock(Player p, Block b) {
        BlockBreakEvent event = new BlockBreakEvent(b, p);
        event.setExpToDrop(BlockExpDropTable.getExpDrop(b, this.getItemInHand(p)));
        Bukkit.getPluginManager().callEvent((Event)event);
        if (!event.isCancelled()) {
            ItemStack hand = p.getItemInHand();
            hand.setDurability((short)(hand.getDurability() + 1));
            if (hand.getDurability() >= hand.getType().getMaxDurability() && !hand.getItemMeta().isUnbreakable()) {
                return false;
            }
            b.breakNaturally(hand);
            if (event.getExpToDrop() > 0) {
                ExperienceOrb exp = (ExperienceOrb)b.getWorld().spawnEntity(b.getLocation().clone().add(0.5, 0.5, 0.5), EntityType.EXPERIENCE_ORB);
                exp.setVelocity(new Vector(0.0f, 0.2f, 0.0f));
                exp.setExperience(exp.getExperience() + event.getExpToDrop());
            }
        }
        return !event.isCancelled();
    }

    default public ItemStack getItemInHand(Player p) {
        return p.getItemInHand();
    }

    public void crackBlock(Block var1, Material var2);

    public void crackCrop(Block var1, Material var2);

    public void playSound(Block var1, NSound var2);

    public void playSound(Block var1, Material var2);

    public boolean canCropHarvest(Block var1);

    public boolean isFarmLnad(Block var1);

    public boolean isCrop(Block var1);

    public void actionBarMes(Player var1, String var2);

    public void titleBarMes(Player var1, String var2);

    public static enum NSound {
        Ore,
        Farm,
        Tree;

    }
}

