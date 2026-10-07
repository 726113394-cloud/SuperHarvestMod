/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package rd.dru;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import rd.dru.Helper;
import rd.dru.SuperHarvest;
import rd.dru.config.Config;
import rd.dru.config.Lang;

public class PlayerManager {
    public static boolean isEnable(Player p, OptionType type) {
        return !p.hasMetadata("disable-" + type.toString()) && p.hasPermission("superharvest." + type.toString().toLowerCase());
    }

    public static boolean isSneakingMode(Player p) {
        return p.hasMetadata("enable-sneaking") ? ((MetadataValue)p.getMetadata("enable-sneaking").get(0)).asBoolean() : SuperHarvest.getSuperConfig().defaultSneaking;
    }

    public static boolean shouldNotify(Player p) {
        return !p.hasMetadata("disable-notify");
    }

    public static boolean toggle(Player p) {
        boolean on = p.hasMetadata("disable-" + (Object)((Object)OptionType.Farming));
        p.sendMessage(ChatColor.GREEN + PlayerManager.getLang((Player)p).toggleAll.replace("{0}", Helper.tranEnable(p, on)));
        PlayerManager.set(p, OptionType.Farming, on);
        PlayerManager.set(p, OptionType.Mining, on);
        PlayerManager.set(p, OptionType.Logging, on);
        return on;
    }

    public static void set(Player p, OptionType type, boolean on) {
        if (!on) {
            p.setMetadata("disable-" + type.toString(), (MetadataValue)new FixedMetadataValue((Plugin)SuperHarvest.getInstance(), (Object)"disable"));
        } else {
            p.removeMetadata("disable-" + type.toString(), (Plugin)SuperHarvest.getInstance());
        }
    }

    public static boolean toggle(Player p, OptionType type) {
        boolean on = p.hasMetadata("disable-" + type.toString());
        p.sendMessage(ChatColor.GREEN + PlayerManager.getLang((Player)p).toggle.replace("{1}", Helper.tranEnable(p, on)).replace("{0}", Helper.trans(p, type)));
        PlayerManager.set(p, type, on);
        return on;
    }

    public static void toggleNotify(Player p) {
        boolean on = p.hasMetadata("disable-notify");
        p.sendMessage(PlayerManager.getLang((Player)p).notifyStatus.replace("{0}", Helper.tranEnable(p, on)));
        if (!on) {
            p.setMetadata("disable-notify", (MetadataValue)new FixedMetadataValue((Plugin)SuperHarvest.getInstance(), (Object)"disable"));
        } else {
            p.removeMetadata("disable-notify", (Plugin)SuperHarvest.getInstance());
        }
    }

    public static void toggleMode(Player p) {
        Config c = SuperHarvest.getSuperConfig();
        boolean on = p.hasMetadata("enable-sneaking") ? ((MetadataValue)p.getMetadata("enable-sneaking").get(0)).asBoolean() : c.defaultSneaking;
        p.sendMessage(!on ? PlayerManager.getLang((Player)p).sneakingMode : PlayerManager.getLang((Player)p).classicMode);
        p.setMetadata("enable-sneaking", (MetadataValue)new FixedMetadataValue((Plugin)SuperHarvest.getInstance(), (Object)(!on ? 1 : 0)));
    }

    public static void setLang(Player p, String lang) {
        if (lang.equals("default")) {
            p.removeMetadata("language", (Plugin)SuperHarvest.getInstance());
        } else {
            p.setMetadata("language", (MetadataValue)new FixedMetadataValue((Plugin)SuperHarvest.getInstance(), (Object)lang));
        }
        p.sendMessage(PlayerManager.getLang((Player)p).toggleLang.replace("{0}", lang));
    }

    public static Lang getLang(Player p) {
        return SuperHarvest.getSuperConfig().getLang(PlayerManager.getLanguage(p));
    }

    private static String getLanguage(Player p) {
        if (p.hasMetadata("language")) {
            return p.getMetadata("language").stream().filter(m -> m.getOwningPlugin().equals((Object)SuperHarvest.getInstance())).findFirst().get().asString();
        }
        if (SuperHarvest.getSuperConfig().autoLang) {
            return p.getLocale();
        }
        return Config.language;
    }

    public static enum OptionType {
        Farming,
        Mining,
        Logging;

    }
}

