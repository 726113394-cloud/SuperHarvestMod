/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package rd.dru.thread;

import org.bukkit.entity.Player;

public interface Workload {
    public Player player();

    public boolean compute();
}

