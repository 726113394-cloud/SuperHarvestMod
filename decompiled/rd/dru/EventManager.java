/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.player.PlayerItemHeldEvent
 *  org.bukkit.event.player.PlayerToggleSneakEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package rd.dru;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import rd.dru.Helper;
import rd.dru.PlayerManager;
import rd.dru.SuperHarvest;
import rd.dru.config.Config;
import rd.dru.thread.workload.CropBreaks;
import rd.dru.thread.workload.OreBreaks;
import rd.dru.thread.workload.TreeBreaks;
import rd.dru.utils.MaterialGroup;

public class EventManager
implements Listener {
    private static MaterialGroup speicalTrees = new MaterialGroup("MANGROVE_WOOD", "MANGROVE_LOG", "MANGROVE_ROOTS", "WARPED_STEM", "CRIMSON_STEM");

    public EventManager() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)SuperHarvest.getInstance());
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onSneaking(PlayerToggleSneakEvent e) {
        if (e.isSneaking() && PlayerManager.isSneakingMode(e.getPlayer()) && SuperHarvest.getSuperConfig().isVaildWorld(e.getPlayer().getWorld())) {
            this.notify(e.getPlayer());
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onItemHeld(PlayerItemHeldEvent e) {
        if (!PlayerManager.isSneakingMode(e.getPlayer()) && SuperHarvest.getSuperConfig().isVaildWorld(e.getPlayer().getWorld())) {
            this.notify(e.getPlayer(), e.getNewSlot());
        }
    }

    private boolean notify(Player p) {
        return this.notify(p, p.getInventory().getHeldItemSlot());
    }

    private boolean notify(Player p, int slot) {
        ItemStack i = p.getInventory().getItem(slot);
        if (i == null || !PlayerManager.shouldNotify(p)) {
            return false;
        }
        String tool = i.getType().toString();
        if (!tool.contains("_")) {
            return false;
        }
        PlayerManager.OptionType type = null;
        switch (tool.substring(tool.indexOf("_")).toLowerCase()) {
            case "_hoe": {
                type = PlayerManager.OptionType.Farming;
                break;
            }
            case "_pickaxe": {
                type = PlayerManager.OptionType.Mining;
                break;
            }
            case "_axe": {
                type = PlayerManager.OptionType.Logging;
            }
        }
        if (type != null && PlayerManager.isEnable(p, type)) {
            Config c = SuperHarvest.getSuperConfig();
            String message = ChatColor.GREEN + PlayerManager.getLang((Player)p).notify.replace("{0}", Helper.trans(p, type));
            if (c.actionBarNotify) {
                SuperHarvest.nms.actionBarMes(p, message);
            }
            if (c.titleBarNotify) {
                SuperHarvest.nms.titleBarMes(p, message);
            }
            return true;
        }
        return false;
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled() || !SuperHarvest.getSuperConfig().isVaildWorld(e.getBlock().getWorld())) {
            return;
        }
        if (SuperHarvest.thread.cach.contains(e.getBlock()) || PlayerManager.isSneakingMode(e.getPlayer()) && !e.getPlayer().isSneaking()) {
            SuperHarvest.thread.cach.remove(e.getBlock());
            return;
        }
        Player p = e.getPlayer();
        Material hand = SuperHarvest.nms.getItemInHand(p).getType();
        String type = e.getBlock().getType().toString();
        String tool = hand.toString();
        if (!p.hasPermission("superharvest.block." + type.toLowerCase()) && p.isPermissionSet("superharvest.block." + type.toLowerCase()) || !p.hasPermission("superharvest.tool." + tool.toLowerCase()) && p.isPermissionSet("superharvest.tool." + tool.toLowerCase())) {
            return;
        }
        if (SuperHarvest.getSuperConfig().enableFarming && PlayerManager.isEnable(p, PlayerManager.OptionType.Farming) && tool.contains("_HOE") && SuperHarvest.nms.isCrop(e.getBlock())) {
            SuperHarvest.thread.poll(new CropBreaks(e.getPlayer(), e.getBlock()));
        } else if (SuperHarvest.getSuperConfig().enableMining && PlayerManager.isEnable(p, PlayerManager.OptionType.Mining) && tool.contains("_PICKAXE") && type.contains("_ORE")) {
            SuperHarvest.thread.poll(new OreBreaks(e.getPlayer(), e.getBlock()));
        } else if (SuperHarvest.getSuperConfig().enableLogging && PlayerManager.isEnable(p, PlayerManager.OptionType.Logging) && tool.contains("_AXE")) {
            if (type.contains("LOG")) {
                SuperHarvest.thread.poll(new TreeBreaks(e.getPlayer(), e.getBlock()));
            } else if (speicalTrees.contains(e.getBlock().getType())) {
                SuperHarvest.thread.poll(new TreeBreaks(e.getPlayer(), e.getBlock(), speicalTrees));
            }
        }
    }
}

