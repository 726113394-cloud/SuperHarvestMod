/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatMessageType
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.block.data.Ageable
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package rd.dru.nms;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import rd.dru.PlayerManager;
import rd.dru.SuperHarvest;
import rd.dru.nms.NMSHandler;

public class V1_16Handler
implements NMSHandler,
Listener {
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
        b.getWorld().spawnParticle(Particle.BLOCK_CRACK, b.getLocation().add(0.5, 0.5, 0.5), 25, 1.0, 0.1, 0.1, 0.1, (Object)type.createBlockData());
    }

    @Override
    public void crackCrop(Block b, Material type) {
        if (type.equals((Object)Material.NETHER_WART)) {
            this.crackBlock(b, Material.NETHER_WART);
        } else {
            this.crackBlock(b, Material.LEGACY_CROPS);
        }
    }

    @Override
    public void playSound(Block b, NMSHandler.NSound sound) {
        Sound e;
        switch (sound) {
            case Farm: {
                if (b.getType().equals((Object)Material.NETHER_WART)) {
                    e = Sound.BLOCK_NETHER_WART_BREAK;
                    break;
                }
                e = Sound.BLOCK_CROP_BREAK;
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
        b.getWorld().playSound(b.getLocation(), sound.createBlockData().getSoundGroup().getBreakSound(), 1.0f, 1.0f);
    }

    @Override
    public boolean isCrop(Block b) {
        return b.getBlockData() instanceof Ageable;
    }

    @Override
    public boolean isFarmLnad(Block b) {
        return b.getType().equals((Object)Material.FARMLAND) || b.getType().equals((Object)Material.SOUL_SAND);
    }

    @Override
    public boolean canCropHarvest(Block b) {
        if (!SuperHarvest.nms.isCrop(b)) {
            return false;
        }
        Ageable age = (Ageable)b.getBlockData();
        return age.getAge() == age.getMaximumAge();
    }

    @Override
    public void actionBarMes(Player p, String mes) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent)new TextComponent(mes));
    }

    @Override
    public void titleBarMes(Player p, String mes) {
        p.sendTitle(" ", mes, 0, 20, 10);
    }

    @Override
    public void init(SuperHarvest sh) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)sh);
        Bukkit.getOnlinePlayers().forEach(p -> this.onJoin(new PlayerJoinEvent(p, "player join")));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PersistentDataContainer data = p.getPersistentDataContainer();
        data.set(new NamespacedKey((Plugin)SuperHarvest.getInstance(), "Farming"), PersistentDataType.BYTE, (Object)this.convert(PlayerManager.isEnable(p, PlayerManager.OptionType.Farming)));
        data.set(new NamespacedKey((Plugin)SuperHarvest.getInstance(), "Logging"), PersistentDataType.BYTE, (Object)this.convert(PlayerManager.isEnable(p, PlayerManager.OptionType.Logging)));
        data.set(new NamespacedKey((Plugin)SuperHarvest.getInstance(), "Mining"), PersistentDataType.BYTE, (Object)this.convert(PlayerManager.isEnable(p, PlayerManager.OptionType.Mining)));
        data.set(new NamespacedKey((Plugin)SuperHarvest.getInstance(), "Mode"), PersistentDataType.BYTE, (Object)this.convert(PlayerManager.isSneakingMode(p)));
    }

    private byte convert(boolean bt) {
        return (byte)(bt ? 1 : 0);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PersistentDataContainer data = p.getPersistentDataContainer();
        if (data.has(new NamespacedKey((Plugin)SuperHarvest.getInstance(), "Farming"), PersistentDataType.BYTE)) {
            PlayerManager.set(p, PlayerManager.OptionType.Farming, (Byte)data.get(new NamespacedKey((Plugin)SuperHarvest.getInstance(), "Farming"), PersistentDataType.BYTE) == 1);
            PlayerManager.set(p, PlayerManager.OptionType.Logging, (Byte)data.get(new NamespacedKey((Plugin)SuperHarvest.getInstance(), "Logging"), PersistentDataType.BYTE) == 1);
            PlayerManager.set(p, PlayerManager.OptionType.Mining, (Byte)data.get(new NamespacedKey((Plugin)SuperHarvest.getInstance(), "Mining"), PersistentDataType.BYTE) == 1);
            if (PlayerManager.isSneakingMode(p) != ((Byte)data.get(new NamespacedKey((Plugin)SuperHarvest.getInstance(), "Mode"), PersistentDataType.BYTE) == 1)) {
                PlayerManager.toggleMode(p);
            }
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(p -> this.onLeave(new PlayerQuitEvent(p, "player leave")));
    }
}

