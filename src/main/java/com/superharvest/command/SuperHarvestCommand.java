package com.superharvest.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.superharvest.config.ModConfig;
import com.superharvest.util.BlockHelper;
import com.superharvest.util.CustomBlocksStore;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Commands: /superharvest  alias /sh
 */
public final class SuperHarvestCommand {
    private SuperHarvestCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(buildRoot("superharvest"));
        dispatcher.register(buildRoot("sh"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildRoot(String name) {
        return Commands.literal(name)
                .executes(ctx -> toggleAll(ctx.getSource()))
                .then(Commands.literal("toggle")
                        .executes(ctx -> toggleAll(ctx.getSource())))
                .then(Commands.literal("farming")
                        .executes(ctx -> toggleFeature(ctx.getSource(), "enableFarming")))
                .then(Commands.literal("mining")
                        .executes(ctx -> toggleFeature(ctx.getSource(), "enableMining")))
                .then(Commands.literal("logging")
                        .executes(ctx -> toggleFeature(ctx.getSource(), "enableLogging")))
                .then(Commands.literal("custom")
                        .executes(ctx -> toggleFeature(ctx.getSource(), "enableCustom")))
                .then(Commands.literal("mode")
                        .executes(ctx -> toggleMode(ctx.getSource())))
                .then(Commands.literal("about")
                        .executes(ctx -> about(ctx.getSource())))
                .then(Commands.literal("chain")
                        .then(Commands.literal("list")
                                .executes(ctx -> listCustom(ctx.getSource())))
                        .then(Commands.literal("add")
                                .executes(ctx -> addLooking(ctx.getSource()))
                                .then(Commands.argument("block", StringArgumentType.greedyString())
                                        .executes(ctx -> addCustom(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "block")))))
                        .then(Commands.literal("remove")
                                .then(Commands.argument("block", StringArgumentType.greedyString())
                                        .executes(ctx -> removeCustom(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "block")))))
                        .then(Commands.literal("clear")
                                .executes(ctx -> clearCustom(ctx.getSource()))))
                .then(Commands.literal("reload")
                        .executes(ctx -> reload(ctx.getSource())));
    }

    private static void ok(CommandSourceStack source, String msg) {
        source.sendSuccess(() -> Component.literal(msg), true);
    }

    private static void msg(CommandSourceStack source, String msg) {
        source.sendSuccess(() -> Component.literal(msg), false);
    }

    private static int toggleAll(CommandSourceStack source) {
        boolean next = !ModConfig.COMMON.enableMining.get();
        ModConfig.COMMON.enableFarming.set(next);
        ModConfig.COMMON.enableMining.set(next);
        ModConfig.COMMON.enableLogging.set(next);
        ModConfig.COMMON.enableCustom.set(next);
        ModConfig.COMMON_SPEC.save();
        CustomBlocksStore.save(ModConfig.COMMON.customChainableBlocks.get());
        ok(source, "§7SuperHarvest 已 §e" + (next ? "启用" : "禁用") + "§7。");
        return 1;
    }

    private static int toggleFeature(CommandSourceStack source, String key) {
        boolean next;
        String name;
        switch (key) {
            case "enableFarming" -> {
                next = !ModConfig.COMMON.enableFarming.get();
                ModConfig.COMMON.enableFarming.set(next);
                name = "收割";
            }
            case "enableMining" -> {
                next = !ModConfig.COMMON.enableMining.get();
                ModConfig.COMMON.enableMining.set(next);
                name = "挖矿";
            }
            case "enableLogging" -> {
                next = !ModConfig.COMMON.enableLogging.get();
                ModConfig.COMMON.enableLogging.set(next);
                name = "伐木";
            }
            default -> {
                next = !ModConfig.COMMON.enableCustom.get();
                ModConfig.COMMON.enableCustom.set(next);
                name = "自定义连锁";
            }
        }
        ModConfig.COMMON_SPEC.save();
        CustomBlocksStore.save(ModConfig.COMMON.customChainableBlocks.get());
        ok(source, "§e" + name + " §7已" + (next ? "§a启用" : "§c禁用") + "§7。");
        return 1;
    }

    private static int toggleMode(CommandSourceStack source) {
        boolean sneak = !ModConfig.COMMON.sneakOnly.get();
        ModConfig.COMMON.sneakOnly.set(sneak);
        ModConfig.COMMON_SPEC.save();
        CustomBlocksStore.save(ModConfig.COMMON.customChainableBlocks.get());
        ok(source, sneak
                ? "§7SuperHarvest 已启用§e蹲下模式§7（蹲下才连锁）。"
                : "§7SuperHarvest 已启用§6经典模式§7（始终连锁）。");
        return 1;
    }

    private static int about(CommandSourceStack source) {
        msg(source, "§aSuperHarvest (Forge) §7— 连锁采集");
        msg(source, "§7作者：§e来自太空的小头脑 §7邮箱：§elztkdxtn@qq.com");
        msg(source, "§7用法：§e/sh chain add|remove|list §7｜§e/sh mode §7｜§e/sh farming|mining|logging|custom");
        return 1;
    }

    private static List<String> mutableCustom() {
        java.util.Set<String> merged = new java.util.LinkedHashSet<>(CustomBlocksStore.load());
        merged.addAll(ModConfig.COMMON.customChainableBlocks.get());
        return new ArrayList<>(merged);
    }

    private static int listCustom(CommandSourceStack source) {
        List<String> set = mutableCustom();
        msg(source, "§a自定义连锁方块（" + set.size() + "）：");
        set.stream().sorted().forEach(id -> msg(source, "§7- §e" + id));
        msg(source, "§7添加：§e/sh chain add <id> §7或对准方块 §e/sh chain add");
        return 1;
    }

    private static int addCustom(CommandSourceStack source, String raw) {
        String rid = raw.trim().toLowerCase(Locale.ROOT).replace("\"", "");
        if (rid.isEmpty()) return 0;
        if (!rid.contains(":")) rid = "minecraft:" + rid;
        final String id = rid;
        if (!BlockHelper.isRegisteredBlock(id)) {
            msg(source, "§c未知方块：§f" + id + " §7（可用 create:zinc_ore 这种完整 id）");
            return 0;
        }
        List<String> list = mutableCustom();
        if (list.stream().anyMatch(s -> s.equalsIgnoreCase(id))) {
            msg(source, "§e已在列表中：§f" + id);
            return 1;
        }
        list.add(id);
        ModConfig.COMMON.customChainableBlocks.set(list);
        ModConfig.COMMON_SPEC.save();
        CustomBlocksStore.save(ModConfig.COMMON.customChainableBlocks.get());
        ok(source, "§a已添加可连锁方块：§f" + id);
        return 1;
    }

    private static int addLooking(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            msg(source, "§c请由玩家执行，或使用：/sh chain add <方块id>");
            return 0;
        }
        var hit = player.pick(6.0, 0.0F, false);
        if (hit == null || hit.getType() != net.minecraft.world.phys.HitResult.Type.BLOCK) {
            msg(source, "§c请对准要连锁的方块，或：/sh chain add <方块id>");
            return 0;
        }
        BlockPos pos = ((net.minecraft.world.phys.BlockHitResult) hit).getBlockPos();
        return addCustom(source, BlockHelper.idOf(player.level().getBlockState(pos)));
    }

    private static int removeCustom(CommandSourceStack source, String raw) {
        String rid = raw.trim().toLowerCase(Locale.ROOT).replace("\"", "");
        if (!rid.contains(":")) rid = "minecraft:" + rid;
        final String id = rid;
        List<String> list = mutableCustom();
        list.removeIf(s -> s.equalsIgnoreCase(id));
        ModConfig.COMMON.customChainableBlocks.set(list);
        ModConfig.COMMON_SPEC.save();
        CustomBlocksStore.save(ModConfig.COMMON.customChainableBlocks.get());
        ok(source, "§a已移除：§f" + id);
        return 1;
    }

    private static int clearCustom(CommandSourceStack source) {
        ModConfig.COMMON.customChainableBlocks.set(new ArrayList<>());
        ModConfig.COMMON_SPEC.save();
        CustomBlocksStore.save(ModConfig.COMMON.customChainableBlocks.get());
        ok(source, "§a已清空自定义连锁方块列表。");
        return 1;
    }

    private static int reload(CommandSourceStack source) {
        ok(source, "§7配置：config/superharvest-common.toml（保存后生效）。");
        return 1;
    }
}
