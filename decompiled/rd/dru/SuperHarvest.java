/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.plugin.java.JavaPlugin
 */
package rd.dru;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import rd.dru.Commands;
import rd.dru.EventManager;
import rd.dru.bukkit.Metrics;
import rd.dru.config.Config;
import rd.dru.nms.NMSHandler;
import rd.dru.nms.VersionChecker;
import rd.dru.thread.SafeThread;

public class SuperHarvest
extends JavaPlugin {
    private static SuperHarvest inst;
    public static SafeThread thread;
    private static Config config;
    public static NMSHandler nms;

    public void onEnable() {
        inst = this;
        config = new Config(new File("plugins/SuperHarvest/config.yml"));
        nms = VersionChecker.getCurrentVersion().getNMS();
        nms.init(this);
        thread = new SafeThread(SuperHarvest.config.bufferMs);
        thread.start();
        new EventManager();
        this.getCommand("superharvest").setExecutor((CommandExecutor)new Commands());
        int pluginId = 15675;
        new Metrics(this, pluginId);
        Bukkit.getLogger().info("SuperHarvest is enabled.");
    }

    public void onDisable() {
        thread.stop();
        nms.onDisable();
        Bukkit.getLogger().info("SuperHarvest is disabled.");
    }

    public static SuperHarvest getInstance() {
        return inst;
    }

    public static Config getSuperConfig() {
        return config;
    }
}

