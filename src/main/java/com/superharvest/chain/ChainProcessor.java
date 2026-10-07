package com.superharvest.chain;

import com.superharvest.config.ModConfig;
import com.superharvest.util.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Incremental chain-break jobs. Align with 1.20.1 Forge feel:
 * same-type only, leaves for trees, mature crops, limited per tick.
 */
public final class ChainProcessor {
    public enum Kind { FARM, MINE, LOG, CUSTOM }

    private static final class Job {
        final UUID player;
        final Kind kind;
        final int ox, oy, oz;
        final Block type;
        final Deque<BlockPos> going = new ArrayDeque<>();
        final Deque<BlockPos> leaves = new ArrayDeque<>();
        final Set<Long> visited = new HashSet<>();
        final Set<Long> visitedLeaves = new HashSet<>();
        int leafBudget;

        Job(UUID player, Kind kind, int ox, int oy, int oz, Block type) {
            this.player = player;
            this.kind = kind;
            this.ox = ox;
            this.oy = oy;
            this.oz = oz;
            this.type = type;
            this.leafBudget = ModConfig.COMMON.maxLeafSize.get();
        }
    }

    private static final List<Job> JOBS = new ArrayList<>();
    private static final Set<Long> CHAINING = ConcurrentHashMap.newKeySet();

    private ChainProcessor() {
    }

    private static long key(int x, int y, int z) {
        return ((long) x & 0x3FFFFFF) << 38 | ((long) (y & 0xFFF)) << 26 | ((long) z & 0x3FFFFFF);
    }

    public static void markChaining(BlockPos pos) {
        CHAINING.add(key(pos.getX(), pos.getY(), pos.getZ()));
    }

    public static boolean consumeChaining(BlockPos pos) {
        return CHAINING.remove(key(pos.getX(), pos.getY(), pos.getZ()));
    }

    public static void clear() {
        synchronized (JOBS) {
            JOBS.clear();
        }
        CHAINING.clear();
    }

    /** Cancel all chain jobs for one player (tool switch, death, etc.). */
    public static void cancelPlayer(UUID playerId) {
        synchronized (JOBS) {
            JOBS.removeIf(job -> job.player.equals(playerId));
        }
    }

    public static boolean hasJob(UUID playerId) {
        synchronized (JOBS) {
            return JOBS.stream().anyMatch(j -> j.player.equals(playerId));
        }
    }

    /** Origin is already broken by the player event; seed same-type neighbors. */
    public static void submit(ServerPlayer player, Kind kind, BlockPos origin, BlockState originState) {
        Job job = new Job(player.getUUID(), kind, origin.getX(), origin.getY(), origin.getZ(), originState.getBlock());
        job.visited.add(key(origin.getX(), origin.getY(), origin.getZ()));
        seedFrom(level(player), job, origin);
        synchronized (JOBS) {
            JOBS.add(job);
        }
    }

    private static ServerLevel level(ServerPlayer player) {
        return (ServerLevel) player.level();
    }

    private static int distLimit(Kind kind) {
        return switch (kind) {
            case MINE -> ModConfig.COMMON.oreMaxDistance.get();
            case FARM -> ModConfig.COMMON.cropMaxDistance.get();
            case LOG -> ModConfig.COMMON.logMaxDistance.get();
            case CUSTOM -> ModConfig.COMMON.customMaxDistance.get();
        };
    }

    private static void enqueue(Job job, BlockPos pos) {
        if (Math.abs(pos.getX() - job.ox) + Math.abs(pos.getY() - job.oy) + Math.abs(pos.getZ() - job.oz)
                > distLimit(job.kind)) {
            return;
        }
        if (job.visited.add(key(pos.getX(), pos.getY(), pos.getZ()))
                && job.going.size() < ModConfig.COMMON.maxChainSize.get()) {
            job.going.add(pos);
        }
    }

    private static List<BlockPos> neighbors(BlockPos pos) {
        int r = Math.max(1, ModConfig.COMMON.neighborRadius.get());
        List<BlockPos> list = new ArrayList<>();
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    list.add(pos.offset(x, y, z));
                }
            }
        }
        return list;
    }

    private static void seedFrom(ServerLevel level, Job job, BlockPos from) {
        for (BlockPos n : neighbors(from)) {
            consider(level, job, n, from.getY());
        }
    }

    private static void consider(ServerLevel level, Job job, BlockPos pos, int fromY) {
        BlockState st = level.getBlockState(pos);
        switch (job.kind) {
            case MINE -> {
                if (st.getBlock() == job.type && BlockHelper.isOre(st)) {
                    enqueue(job, pos);
                }
            }
            case LOG -> {
                if (BlockHelper.isLeaves(st)) {
                    if (ModConfig.COMMON.breakLeaves.get() && job.leafBudget > 0
                            && job.visitedLeaves.add(key(pos.getX(), pos.getY(), pos.getZ()))) {
                        job.leaves.add(pos);
                    }
                } else if (pos.getY() >= fromY && st.getBlock() == job.type) {
                    enqueue(job, pos);
                }
            }
            case FARM -> {
                BlockPos target = pos;
                BlockState ts = st;
                if (BlockHelper.isFarmland(st)) {
                    target = pos.above();
                    ts = level.getBlockState(target);
                }
                if (BlockHelper.isSugarCane(ts)) {
                    if (ModConfig.COMMON.allowSugarCane.get() && target.getY() == job.oy) {
                        enqueue(job, target);
                    }
                } else if (BlockHelper.isCrop(ts) && BlockHelper.canCropHarvest(ts)) {
                    enqueue(job, target);
                }
            }
            case CUSTOM -> {
                if (st.getBlock() == job.type && BlockHelper.isCustomChainable(st)) {
                    enqueue(job, pos);
                }
            }
        }
    }

    public static void onServerTick(net.minecraftforge.event.TickEvent.ServerTickEvent event) {
        if (event.phase != net.minecraftforge.event.TickEvent.Phase.END) {
            return;
        }
        int budget = ModConfig.COMMON.blocksPerTick.get();
        List<Job> redo = new ArrayList<>();
        synchronized (JOBS) {
            for (Job job : JOBS) {
                if (budget <= 0) {
                    redo.add(job);
                    continue;
                }
                ServerPlayer player = findPlayer(job.player);
                if (player == null || !player.isAlive()) {
                    continue;
                }
                ServerLevel level = level(player);
                budget--;
                if (!step(job, player, level)) {
                    redo.add(job);
                }
            }
            JOBS.clear();
            JOBS.addAll(redo);
        }
    }

    private static boolean toolStillValid(Job job, ServerPlayer player) {
        var tool = player.getMainHandItem();
        return switch (job.kind) {
            case MINE -> !ModConfig.COMMON.requireCorrectTool.get() || BlockHelper.isPickaxe(tool);
            case FARM -> !ModConfig.COMMON.requireCorrectTool.get() || BlockHelper.isHoe(tool);
            case LOG -> !ModConfig.COMMON.requireCorrectTool.get() || BlockHelper.isAxe(tool);
            case CUSTOM -> !ModConfig.COMMON.customRequirePickaxe.get() || BlockHelper.isPickaxe(tool);
        };
    }

    private static ServerPlayer findPlayer(UUID id) {
        var server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        return server == null ? null : server.getPlayerList().getPlayer(id);
    }

    /** @return true = job finished */
    private static boolean step(Job job, ServerPlayer player, ServerLevel level) {
        // switch tool / empty hand → stop this chain immediately
        if (!toolStillValid(job, player)) {
            return true;
        }
        if (!job.going.isEmpty()) {
            BlockPos pos = job.going.poll();
            if (pos == null) {
                return job.leaves.isEmpty();
            }
            BlockState state = level.getBlockState(pos);
            if (state.isAir() || state.getBlock() != job.type) {
                return job.going.isEmpty() && job.leaves.isEmpty();
            }
            markChaining(pos);
            if (!player.gameMode.destroyBlock(pos)) {
                return true;
            }
            seedFrom(level, job, pos);
            return job.going.isEmpty() && job.leaves.isEmpty();
        }
        int n = 0;
        while (!job.leaves.isEmpty() && n < 6 && job.leafBudget > 0) {
            BlockPos pos = job.leaves.poll();
            if (pos == null) break;
            if (BlockHelper.isLeaves(level.getBlockState(pos))) {
                markChaining(pos);
                player.gameMode.destroyBlock(pos);
                job.leafBudget--;
            }
            n++;
        }
        return job.going.isEmpty() && job.leaves.isEmpty();
    }
}
