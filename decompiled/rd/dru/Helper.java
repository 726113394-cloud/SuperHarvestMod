/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 */
package rd.dru;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import rd.dru.PlayerManager;

public class Helper {
    private final List<String> stones = Arrays.asList("STONE", "DEEPSLATE", "NETHERRACK", "COBBLED_DEEPSLATE", "GRAVEL", "BLACKSTONE", "GRANITE", "DIORITE", "ANDESITE", "CALCITE", "TUFF", "DRIPSTONE_BLOCK", "DIRT", "MAGMA_BLOCK", "BASALT", "BEDROCK");

    public static Collection<Block> getNear(Block b) {
        HashSet<Block> bs = new HashSet<Block>();
        int x = -1;
        while (x <= 1) {
            int y = -1;
            while (y <= 1) {
                int z = -1;
                while (z <= 1) {
                    if (x != 0 || y != 0 || z != 0) {
                        bs.add(new Location(b.getWorld(), (double)(x + b.getX()), (double)(y + b.getY()), (double)(z + b.getZ())).getBlock());
                    }
                    ++z;
                }
                ++y;
            }
            ++x;
        }
        return bs;
    }

    public static Collection<Block> getNear(Block b, int extra) {
        HashSet<Block> bs = new HashSet<Block>();
        int x = -1 - extra;
        while (x <= 1 + extra) {
            int y = -1 - extra;
            while (y <= 1 + extra) {
                int z = -1 - extra;
                while (z <= 1 + extra) {
                    if (x != 0 || y != 0 || z != 0) {
                        bs.add(new Location(b.getWorld(), (double)(x + b.getX()), (double)(y + b.getY()), (double)(z + b.getZ())).getBlock());
                    }
                    ++z;
                }
                ++y;
            }
            ++x;
        }
        return bs;
    }

    public static String trans(Player p, PlayerManager.OptionType type) {
        switch (type) {
            case Farming: {
                return PlayerManager.getLang((Player)p).farm;
            }
            case Logging: {
                return PlayerManager.getLang((Player)p).log;
            }
            case Mining: {
                return PlayerManager.getLang((Player)p).mine;
            }
        }
        return "";
    }

    public static String tranEnable(Player p, boolean enabled) {
        return enabled ? PlayerManager.getLang((Player)p).enable : PlayerManager.getLang((Player)p).disable;
    }

    public boolean isstone(Block block) {
        return this.stones.contains(block.getType().name());
    }
}

