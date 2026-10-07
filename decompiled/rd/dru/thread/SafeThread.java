/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.papermc.paper.threadedregions.scheduler.ScheduledTask
 *  org.bukkit.Bukkit
 *  org.bukkit.block.Block
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package rd.dru.thread;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import rd.dru.SuperHarvest;
import rd.dru.thread.Workload;

public class SafeThread
implements Runnable {
    Deque<Workload> queue = new ArrayDeque<Workload>();
    public HashSet<Block> cach = new HashSet();
    long limit = 3L;
    private final boolean isFolia = this.isFolia();
    Task task;

    private boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }

    public SafeThread(long limit) {
        this.limit = limit;
    }

    public synchronized void poll(Workload work) {
        this.queue.addLast(work);
    }

    @Override
    public void run() {
        long reach = System.currentTimeMillis() + this.limit;
        ArrayList<Workload> rego = new ArrayList<Workload>();
        while (!this.queue.isEmpty() && System.currentTimeMillis() < reach) {
            final Workload work = this.queue.poll();
            if (work == null) continue;
            if (this.isFolia) {
                work.player().getScheduler().run((Plugin)SuperHarvest.getInstance(), t -> {
                    if (!work.compute()) {
                        this.queue.add(work);
                    }
                }, new Runnable(){

                    @Override
                    public void run() {
                        work.compute();
                    }
                });
                continue;
            }
            if (work.compute()) continue;
            rego.add(work);
        }
        this.queue.addAll(rego);
    }

    public void start() {
        this.task = !this.isFolia ? new Task(Bukkit.getScheduler().runTaskTimer((Plugin)SuperHarvest.getInstance(), (Runnable)this, 1L, 1L)) : new Task(Bukkit.getGlobalRegionScheduler().runAtFixedRate((Plugin)SuperHarvest.getInstance(), t -> this.run(), 1L, 1L));
    }

    public void stop() {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    public static class Task {
        private Object foliaTask;
        private BukkitTask bukkitTask;

        Task(Object foliaTask) {
            this.foliaTask = foliaTask;
        }

        Task(BukkitTask foliaTask) {
            this.bukkitTask = foliaTask;
        }

        public void cancel() {
            if (this.foliaTask != null) {
                ((ScheduledTask)this.foliaTask).cancel();
            }
        }
    }
}

