/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.CropState
 *  org.bukkit.Effect
 *  org.bukkit.Material
 *  org.bukkit.NetherWartsState
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.material.Crops
 *  org.bukkit.material.NetherWarts
 */
package rd.dru.nms;

import org.bukkit.CropState;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Crops;
import org.bukkit.material.NetherWarts;
import rd.dru.nms.LegacyMethod;
import rd.dru.nms.NMSHandler;

public class V1_8Handler
implements NMSHandler {
    @Override
    public boolean breakBlock(Player p, Block b) {
        return NMSHandler.super.breakBlock(p, b);
    }

    @Override
    public void crackBlock(Block b, Material type) {
        b.getWorld().playEffect(b.getLocation(), Effect.valueOf((String)"TILE_BREAK"), type.getId(), 25);
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
                    e = Sound.valueOf((String)"DIG_STONE");
                    break;
                }
                e = Sound.valueOf((String)"DIG_GRASS");
                break;
            }
            case Tree: {
                e = Sound.valueOf((String)"DIG_WOOD");
                break;
            }
            default: {
                e = Sound.valueOf((String)"DIG_STONE");
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
        LegacyMethod.sendActionBar(p, mes);
    }

    @Override
    public void titleBarMes(Player p, String mes) {
        LegacyMethod.sendTitle(p, " ", mes, 0, 20, 10);
    }
}

