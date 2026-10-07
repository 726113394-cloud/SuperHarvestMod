/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package rd.dru.config;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import rd.dru.config.Config;

public class Lang {
    public String language;
    public boolean enableFarming;
    public boolean enableMining;
    public boolean enableLogging;
    public boolean actionBarNotify;
    public boolean titleBarNotify;
    public boolean defaultSneaking;
    public String toggle;
    public String toggleAll;
    public String toggleLang;
    public String farm;
    public String mine;
    public String log;
    public String enable;
    public String disable;
    public String about;
    public String perms;
    public String notify;
    public String notifyStatus;
    public String classicMode;
    public String sneakingMode;

    public Lang(String lang) {
        this.language = lang;
        File langF = new File("plugins/SuperHarvest/Langs/" + lang + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration((File)langF);
        switch (lang) {
            case "zh_tw": {
                config.options().header("- \u76f8\u95dc\u8072\u660e\r\n\r\n\u8acb\u4e0d\u8981\u51fa\u552e\u8f1d\u714c\u5718\u968a\u958b\u767c\u4e4b\u63d2\u4ef6\uff0c\u6216\u8005\u51fa\u552e\u63d2\u4ef6\u529f\u80fd\uff0c\r\n\u6211\u5011\u7684\u63d2\u4ef6\u90fd\u662f\u7121\u511f\u63d0\u4f9b\u7d66\u5404\u4f3a\u670d\u5668\u4f7f\u7528\uff0c\r\n\u6545\u6211\u5011\u958b\u767c\u9019\u4e9b\u63d2\u4ef6\u4e26\u6c92\u6709\u4efb\u4f55\u7684\u5be6\u8cea\u6536\u76ca\uff0c\r\n\r\n\u6545\u9019\u985e\u884c\u70ba\u662f\u6703\u4e00\u5b9a\u7a0b\u5ea6\u4e0a\u964d\u4f4e\u6211\u5011\u7684\u958b\u767c\u610f\u9858\uff0c\r\n\u4e5f\u6709\u53ef\u80fd\u6703\u4f7f\u6211\u5011\u6709\u5c07\u514d\u8cbb\u958b\u6e90\u63d2\u4ef6\u8b8a\u70ba\u4ed8\u8cbb\u9589\u6e90\u63d2\u4ef6\u7684\u60f3\u6cd5\uff0c\u671b\u7406\u89e3\u3002\r\n\r\n\u5982\u6709\u76c8\u5229\u9700\u6c42\uff0c\u8acb\u806f\u7d61\u6211\u5011\uff1ahttps://discord.gg/9c287zPpUZ\r\n\r\n- SuperHarvest \u7e41\u9ad4\u4e2d\u6587\u8a9e\u8a00\u8a2d\u5b9a\u6a94\u6848 - by. \u5c0f\u5343\u3001RiceChen_");
                this.enable = Config.color(config.getString("enable", "\u00a7a\u958b\u555f\u00a7r"));
                this.disable = Config.color(config.getString("disable", "\u00a7c\u95dc\u9589\u00a7r"));
                this.notify = Config.color(config.getString("notify", "\u00a7e\u5feb\u901f{0} \u00a7a\u5df2\u958b\u555f\u00a77\u3002"));
                this.notifyStatus = Config.color(config.getString("toggleNotify", "\u00a77\u72c0\u614b\u63d0\u793a\u5df2 \u00a7r{0}\u00a77\uff01"));
                this.perms = Config.color(config.getString("permission", "\u00a7c\u60a8\u6c92\u6709\u6b0a\u9650\u4f7f\u7528\u6b64\u6307\u4ee4\uff01"));
                this.toggle = Config.color(config.getString("toggle", "\u00a7e\u5feb\u901f{0} \u00a77\u5df2\u00a7r{1}\u00a77\u3002"));
                this.toggleAll = Config.color(config.getString("toggleall", "\u00a77SuperHarvest \u5df2\u00a7e{0}\u00a77\u3002"));
                this.toggleLang = Config.color(config.getString("togglelang", "\u00a77\u60a8\u5df2\u5c07 SuperHarvest \u7684\u8a9e\u8a00\u8a2d\u5b9a\u70ba\uff1a\u00a7e{0} \u00a77\u3002"));
                this.farm = Config.color(config.getString("farming", "\u00a7e\u6536\u5272\u00a7r"));
                this.mine = Config.color(config.getString("mining", "\u00a7e\u6316\u7926\u00a7r"));
                this.log = Config.color(config.getString("logging", "\u00a7e\u4f10\u6728\u00a7r"));
                this.about = Config.color(config.getString("about", "\u00a77\u6b64\u63d2\u4ef6\u7531 \u00a76{0} \u00a77\u88fd\u4f5c\u3002"));
                this.classicMode = Config.color(config.getString("classic-mode", "\u00a77SuperHarvest \u5df2\u958b\u555f\u00a76\u7d93\u5178\u6a21\u5f0f\u00a77\u3002"));
                this.sneakingMode = Config.color(config.getString("sneaking-mode", "\u00a77SuperHarvest \u5df2\u958b\u555f\u00a7e\u8e72\u4e0b\u6a21\u5f0f\u00a77\u3002"));
                break;
            }
            case "zh_cn": {
                config.options().header("# - \u76f8\u5173\u58f0\u660e\r\n\r\n\u8bf7\u4e0d\u8981\u51fa\u552e\u8f89\u714c\u56e2\u961f\u5f00\u53d1\u4e4b\u63d2\u4ef6\uff0c\u6216\u8005\u51fa\u552e\u63d2\u4ef6\u529f\u80fd\uff0c\r\n\u6211\u4eec\u7684\u63d2\u4ef6\u90fd\u662f\u65e0\u507f\u63d0\u4f9b\u7ed9\u5404\u4f3a\u670d\u5668\u4f7f\u7528\uff0c\r\n\u6545\u6211\u4eec\u5f00\u53d1\u8fd9\u4e9b\u63d2\u4ef6\u5e76\u6ca1\u6709\u4efb\u4f55\u7684\u5b9e\u8d28\u6536\u76ca\uff0c\r\n\r\n\u6545\u8fd9\u7c7b\u884c\u4e3a\u662f\u4f1a\u4e00\u5b9a\u7a0b\u5ea6\u4e0a\u964d\u4f4e\u6211\u4eec\u7684\u5f00\u53d1\u610f\u613f\uff0c\r\n\u4e5f\u6709\u53ef\u80fd\u4f1a\u4f7f\u6211\u4eec\u6709\u5c06\u514d\u8d39\u5f00\u6e90\u63d2\u4ef6\u53d8\u4e3a\u4ed8\u8d39\u95ed\u6e90\u63d2\u4ef6\u7684\u60f3\u6cd5\uff0c\u671b\u7406\u89e3\u3002\r\n\r\n\u5982\u6709\u76c8\u5229\u9700\u6c42\uff0c\u8bf7\u8054\u7edc\u6211\u4eec\uff1ahttps://discord.gg/9c287zPpUZ\r\n\r\n- SuperHarvest \u7b80\u4f53\u4e2d\u6587\u8bed\u8a00\u8bbe\u5b9a\u6863\u6848 - by. \u5c0f\u5343\u3001RiceChen_\r\n");
                this.enable = Config.color(config.getString("enable", "\u00a7a\u542f\u7528\u00a7r"));
                this.disable = Config.color(config.getString("disable", "\u00a7c\u7981\u7528\u00a7r"));
                this.notify = Config.color(config.getString("notify", "\u00a7e\u5feb\u901f{0} \u00a7a\u5df2\u542f\u7528\u00a77\u3002"));
                this.notifyStatus = Config.color(config.getString("toggleNotify", "\u00a77\u72b6\u6001\u63d0\u793a\u5df2 \u00a7r{0}\u00a77\uff01"));
                this.perms = Config.color(config.getString("permission", "&c\u4f60\u6ca1\u6709\u6743\u9650\u4f7f\u7528\u6b64\u6307\u4ee4"));
                this.toggle = Config.color(config.getString("toggle", "\u00a7e\u5feb\u901f{0} \u00a77\u5df2\u00a7r{1}\u00a77\u3002"));
                this.toggleAll = Config.color(config.getString("toggleall", "\u00a77SuperHarvest \u5df2\u00a7e{0}\u00a77\u3002"));
                this.toggleLang = Config.color(config.getString("togglelang", "\u00a77\u60a8\u5df2\u5c06 SuperHarvest \u7684\u8bed\u8a00\u8bbe\u7f6e\u4e3a\uff1a\u00a7e{0} \u00a77\u3002"));
                this.farm = Config.color(config.getString("farming", "\u00a7e\u6536\u5272\u00a7r"));
                this.mine = Config.color(config.getString("mining", "\u00a7e\u6316\u77ff\u00a7r"));
                this.log = Config.color(config.getString("logging", "\u00a7e\u4f10\u6728\u00a7r"));
                this.about = Config.color(config.getString("about", "\u00a77\u6b64\u63d2\u4ef6\u7531 \u00a76{0} \u00a77\u88fd\u4f5c\u3002"));
                this.classicMode = Config.color(config.getString("classic-mode", "\u00a77SuperHarvest \u5df2\u542f\u7528\u00a76\u7ecf\u5178\u6a21\u5f0f\u00a77\u3002"));
                this.sneakingMode = Config.color(config.getString("sneaking-mode", "\u00a77SuperHarvest \u5df2\u542f\u7528\u00a7e\u8e72\u4e0b\u6a21\u5f0f\u00a77\u3002"));
                break;
            }
            default: {
                this.enable = Config.color(config.getString("enable", "&aenable\u00a7r"));
                this.disable = Config.color(config.getString("disable", "&cdisable\u00a7r"));
                this.notify = Config.color(config.getString("notify", "\u00a7eFast {0} is now on"));
                this.notifyStatus = Config.color(config.getString("toggleNotify", "\u00a77Status notify is now {0}!"));
                this.perms = Config.color(config.getString("permission", "&cYou don't have enough permission to execute this command."));
                this.toggle = Config.color(config.getString("toggle", "\u00a7eFast {0} \u00a77is now {1}"));
                this.toggleAll = Config.color(config.getString("toggleall", "\u00a77SuperHarvest is now {0}"));
                this.toggleLang = Config.color(config.getString("togglelang", "\u00a77The language is now {0}"));
                this.farm = Config.color(config.getString("farming", "\u00a7efarming\u00a7r"));
                this.mine = Config.color(config.getString("mining", "\u00a7emining\u00a7r"));
                this.log = Config.color(config.getString("logging", "\u00a7elogging\u00a7r"));
                this.about = Config.color(config.getString("about", "\u00a77This plugin is made by {0}"));
                this.classicMode = Config.color(config.getString("classic-mode", "\u00a77SuperHarvest is now on Classic mode."));
                this.sneakingMode = Config.color(config.getString("sneaking-mode", "\u00a77SuperHarvest is now on Sneaking mode."));
            }
        }
        config.set("enable", (Object)this.enable);
        config.set("disable", (Object)this.disable);
        config.set("notify", (Object)this.notify);
        config.set("notifyStatus", (Object)this.notifyStatus);
        config.set("permission", (Object)this.perms);
        config.set("toggle", (Object)this.toggle);
        config.set("toggleall", (Object)this.toggleAll);
        config.set("togglelang", (Object)this.toggleLang);
        config.set("farming", (Object)this.farm);
        config.set("mining", (Object)this.mine);
        config.set("logging", (Object)this.log);
        config.set("about", (Object)this.about);
        config.set("classic-mode", (Object)this.classicMode);
        config.set("sneaking-mode", (Object)this.sneakingMode);
        try {
            config.save(langF);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

