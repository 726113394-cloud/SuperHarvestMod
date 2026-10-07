package com.superharvest.util;

import com.superharvest.config.ModConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class BlockHelper {
    private BlockHelper() {
    }

    public static String idOf(BlockState state) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return id == null ? "" : id.toString();
    }

    public static String pathOf(BlockState state) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return id == null ? "" : id.getPath();
    }

    public static boolean isCustomChainable(BlockState state) {
        String full = idOf(state);
        String path = pathOf(state);
        for (String raw : ModConfig.COMMON.customChainableBlocks.get()) {
            if (raw == null || raw.isBlank()) continue;
            String key = raw.trim().toLowerCase(Locale.ROOT);
            if (key.equals(full) || key.equals(path) || full.endsWith(":" + key)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOre(BlockState state) {
        String p = pathOf(state);
        return p.contains("ore");
    }

    public static boolean isLogLike(BlockState state) {
        return state.is(BlockTags.LOGS) || pathOf(state).contains("stem");
    }

    public static boolean isLeaves(BlockState state) {
        return state.is(BlockTags.LEAVES) || pathOf(state).contains("leaves");
    }

    public static boolean isCrop(BlockState state) {
        Block b = state.getBlock();
        return b instanceof CropBlock || b instanceof NetherWartBlock
                || b instanceof CocoaBlock || b instanceof SweetBerryBushBlock
                || getAge(state) != null;
    }

    public static IntegerProperty getAge(BlockState state) {
        for (var prop : state.getProperties()) {
            if (prop instanceof IntegerProperty ip && "age".equals(ip.getName())) {
                return ip;
            }
        }
        return null;
    }

    public static boolean canCropHarvest(BlockState state) {
        IntegerProperty age = getAge(state);
        if (age == null) return isCrop(state);
        int max = age.getPossibleValues().stream().mapToInt(Integer::intValue).max().orElse(0);
        return state.getValue(age) >= max;
    }

    public static boolean isFarmland(BlockState state) {
        Block b = state.getBlock();
        return b == Blocks.FARMLAND || b == Blocks.SOUL_SAND || b == Blocks.SOUL_SOIL;
    }

    public static boolean isSugarCane(BlockState state) {
        return state.is(Blocks.SUGAR_CANE);
    }

    public static boolean isHoe(ItemStack s) {
        return !s.isEmpty() && (s.canPerformAction(net.minecraftforge.common.ToolActions.HOE_DIG) || s.getItem() instanceof HoeItem);
    }

    public static boolean isPickaxe(ItemStack s) {
        return !s.isEmpty() && (s.canPerformAction(net.minecraftforge.common.ToolActions.PICKAXE_DIG) || s.getItem() instanceof PickaxeItem);
    }

    public static boolean isAxe(ItemStack s) {
        return !s.isEmpty() && (s.canPerformAction(net.minecraftforge.common.ToolActions.AXE_DIG) || s.getItem() instanceof AxeItem);
    }

    /** Normalize a user-entered block id to full minecraft: / modid: form. */
    public static String normalizeBlockId(String input) {
        String s = input.trim().toLowerCase(Locale.ROOT);
        if (s.isEmpty()) return s;
        if (!s.contains(":")) {
            s = "minecraft:" + s;
        }
        return s;
    }

    public static boolean isRegisteredBlock(String id) {
        ResourceLocation loc = ResourceLocation.tryParse(id);
        return loc != null && BuiltInRegistries.BLOCK.containsKey(loc);
    }

    public static Set<String> customBlockSnapshot() {
        return new HashSet<>(ModConfig.COMMON.customChainableBlocks.get());
    }
}
