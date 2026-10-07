/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.CropState
 *  org.bukkit.Material
 *  org.bukkit.NetherWartsState
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.Crops
 *  org.bukkit.material.MaterialData
 *  org.bukkit.material.NetherWarts
 */
package rd.dru.nms;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.material.NetherWarts;
import rd.dru.nms.LegacyMethod;
import rd.dru.nms.NMSHandler;
import rd.dru.nms.V1_13Handler;
import rd.dru.nms.VersionChecker;

public class V1_9Handler
implements NMSHandler {
    @Override
    public boolean breakBlock(Player p, Block b) {
        return NMSHandler.super.breakBlock(p, b);
    }

    @Override
    public ItemStack getItemInHand(Player p) {
        return p.getInventory().getItemInMainHand();
    }

    @Override
    public void crackBlock(Block b, Material type) {
        MaterialData d = new MaterialData(type);
        b.getWorld().spawnParticle(Particle.BLOCK_CRACK, b.getLocation().add(0.5, 0.5, 0.5), 25, 1.0, 0.1, 0.1, 0.1, (Object)d);
    }

    @Override
    public void crackCrop(Block b, Material type) {
        if (b.getState().getData() instanceof NetherWarts) {
            this.crackBlock(b, Material.matchMaterial((String)"NETHER_WARTS"));
        } else {
            this.crackBlock(b, Material.matchMaterial((String)"CROPS"));
        }
    }

    @Override
    public void playSound(Block b, NMSHandler.NSound sound) {
        Sound e;
        switch (sound) {
            case Farm: {
                if (b.getState().getData() instanceof NetherWarts) {
                    e = Sound.BLOCK_STONE_BREAK;
                    break;
                }
                e = Sound.BLOCK_GRASS_BREAK;
                break;
            }
            case Ore: {
                e = Sound.BLOCK_STONE_BREAK;
                break;
            }
            case Tree: {
                e = Sound.BLOCK_WOOD_BREAK;
                break;
            }
            default: {
                e = Sound.BLOCK_GRASS_BREAK;
            }
        }
        b.getWorld().playSound(b.getLocation(), e, 1.0f, 1.0f);
    }

    @Override
    public void playSound(Block b, Material sound) {
        this.playSound(b, NMSHandler.NSound.Ore);
    }

    @Override
    public boolean canCropHarvest(Block b) {
        if (!this.isCrop(b)) {
            return false;
        }
        return b.getState().getData() instanceof Crops ? ((Crops)b.getState().getData()).getState().equals((Object)CropState.RIPE) : ((NetherWarts)b.getState().getData()).getState().equals((Object)NetherWartsState.RIPE);
    }

    @Override
    public boolean isCrop(Block b) {
        return b.getState().getData() instanceof Crops || b.getState().getData() instanceof NetherWarts;
    }

    @Override
    public boolean isFarmLnad(Block b) {
        return b.getType().equals((Object)Material.matchMaterial((String)"SOIL")) || b.getType().equals((Object)Material.SOUL_SAND);
    }

    @Override
    public void actionBarMes(Player p, String mes) {
        if (VersionChecker.getServerVersion() < 11) {
            LegacyMethod.sendActionBar(p, mes);
        } else {
            new V1_13Handler().actionBarMes(p, mes);
        }
    }

    @Override
    public void titleBarMes(Player p, String mes) {
        if (VersionChecker.getServerVersion() < 10) {
            LegacyMethod.sendTitle(p, " ", mes, 0, 20, 10);
        } else {
            new V1_13Handler().titleBarMes(p, mes);
        }
    }
}

