/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package rd.dru.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import rd.dru.config.Lang;

public class Config {
    File f;
    FileConfiguration config;
    List<String> disableWorlds;
    List<Material> customBreaks;
    public long bufferMs;
    public boolean enableFarming;
    public boolean enableMining;
    public boolean enableLogging;
    public boolean actionBarNotify;
    public boolean titleBarNotify;
    public boolean defaultSneaking;
    public boolean autoLang;
    public static String language;
    private static HashMap<String, Lang> langs;

    static {
        langs = new HashMap();
    }

    public Config(File f) {
        this.f = f;
        this.load();
        this.save();
    }

    public Lang getLang(String lang) {
        if (langs.containsKey(lang)) {
            return langs.get(lang);
        }
        return langs.get(language);
    }

    public Set<String> getLangs() {
        return langs.keySet();
    }

    public boolean isVaildWorld(World world) {
        return !this.disableWorlds.contains(world.getName());
    }

    public boolean isCustomBreak(Material id) {
        return this.customBreaks.contains(id);
    }

    public static String color(String mes) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)mes);
    }

    public void load() {
        if (this.f.isDirectory()) {
            this.f.delete();
        }
        if (!this.f.exists() || !new File("plugins/SuperHarvest/Langs").exists()) {
            try {
                new File("plugins/SuperHarvest").mkdirs();
                new File("plugins/SuperHarvest/Langs").mkdirs();
                this.f.createNewFile();
                this.loadLang("zh_tw");
                this.loadLang("zh_cn");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration((File)this.f);
        this.disableWorlds = this.config.getStringList("disable-worlds");
        this.enableFarming = this.config.getBoolean("enable-farming", true);
        this.enableMining = this.config.getBoolean("enable-mining", true);
        this.enableLogging = this.config.getBoolean("enable-logging", true);
        this.actionBarNotify = this.config.getBoolean("notify-actionbar", true);
        this.titleBarNotify = this.config.getBoolean("notify-titlebar", false);
        this.defaultSneaking = this.config.getBoolean("default-sneaking-mode", false);
        this.bufferMs = this.config.getLong("cpu-mileseconds-limitation", 3L);
        language = this.config.getString("default-language", "en_us");
        this.loadLang(language);
        this.autoLang = this.config.getBoolean("auto-locale", true);
        Arrays.stream(new File("plugins/SuperHarvest/Langs").list()).forEach(s -> this.loadLang(s.replace(".yml", "")));
    }

    public void save() {
        try {
            this.config.set("disable-worlds", this.disableWorlds);
            this.config.set("enable-farming", (Object)this.enableFarming);
            this.config.set("enable-mining", (Object)this.enableMining);
            this.config.set("enable-logging", (Object)this.enableLogging);
            this.config.set("messages", null);
            if (this.config.contains("eanble-farming")) {
                this.config.set("eanble-farming", null);
                this.config.set("eanble-mining", null);
                this.config.set("eanble-logging", null);
            }
            this.config.set("default-sneaking-mode", (Object)this.defaultSneaking);
            this.config.set("cpu-mileseconds-limitation", (Object)this.bufferMs);
            this.config.set("notify-actionbar", (Object)this.actionBarNotify);
            this.config.set("notify-titlebar", (Object)this.titleBarNotify);
            this.config.set("default-language", (Object)language);
            this.config.set("auto-locale", (Object)this.autoLang);
            this.config.save(this.f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLang(String dlang) {
        langs.put(dlang, new Lang(dlang));
    }
}

