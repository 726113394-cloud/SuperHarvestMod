package com.superharvest.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * Common config. Custom chainable block list is editable at runtime via /sh chain.
 */
public final class ModConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        COMMON = new Common(builder);
        COMMON_SPEC = builder.build();
    }

    private ModConfig() {
    }

    public static final class Common {
        public final ForgeConfigSpec.BooleanValue enableFarming;
        public final ForgeConfigSpec.BooleanValue enableMining;
        public final ForgeConfigSpec.BooleanValue enableLogging;
        public final ForgeConfigSpec.BooleanValue enableCustom;

        public final ForgeConfigSpec.BooleanValue sneakOnly;
        public final ForgeConfigSpec.BooleanValue breakLeaves;
        public final ForgeConfigSpec.BooleanValue allowSugarCane;
        public final ForgeConfigSpec.BooleanValue damageTools;
        public final ForgeConfigSpec.BooleanValue dropExperience;
        public final ForgeConfigSpec.BooleanValue requireCorrectTool;

        public final ForgeConfigSpec.IntValue maxChainSize;
        public final ForgeConfigSpec.IntValue maxLeafSize;
        public final ForgeConfigSpec.IntValue blocksPerTick;
        public final ForgeConfigSpec.IntValue neighborRadius;
        public final ForgeConfigSpec.IntValue oreMaxDistance;
        public final ForgeConfigSpec.IntValue cropMaxDistance;
        public final ForgeConfigSpec.IntValue logMaxDistance;
        public final ForgeConfigSpec.IntValue customMaxDistance;

        public final ForgeConfigSpec.BooleanValue customRequirePickaxe;
        public final ForgeConfigSpec.BooleanValue customAllowAnyTool;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> customChainableBlocks;

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("features");
            enableFarming = builder.define("enableFarming", true);
            enableMining = builder.define("enableMining", true);
            enableLogging = builder.define("enableLogging", true);
            enableCustom = builder
                    .comment("Enable chain for blocks in customChainableBlocks")
                    .define("enableCustom", true);
            breakLeaves = builder.define("breakLeaves", true);
            allowSugarCane = builder.define("allowSugarCane", true);
            builder.pop();

            builder.push("behavior");
            sneakOnly = builder
                    .comment("Need sneak to chain")
                    .define("sneakOnly", true);
            damageTools = builder.define("damageTools", true);
            dropExperience = builder.define("dropExperience", true);
            requireCorrectTool = builder
                    .comment("Require hoe / pickaxe / axe for farming / mining / logging")
                    .define("requireCorrectTool", true);
            builder.pop();

            builder.push("limits");
            maxChainSize = builder.defineInRange("maxChainSize", 256, 8, 2048);
            maxLeafSize = builder.defineInRange("maxLeafSize", 64, 0, 512);
            blocksPerTick = builder.defineInRange("blocksPerTick", 8, 1, 64);
            neighborRadius = builder.defineInRange("neighborRadius", 1, 1, 4);
            oreMaxDistance = builder.defineInRange("oreMaxDistance", 24, 4, 128);
            cropMaxDistance = builder.defineInRange("cropMaxDistance", 12, 2, 64);
            logMaxDistance = builder.defineInRange("logMaxDistance", 16, 2, 64);
            customMaxDistance = builder.defineInRange("customMaxDistance", 32, 4, 128);
            builder.pop();

            builder.push("custom");
            customRequirePickaxe = builder
                    .comment("If true, custom chain needs pickaxe; if false and customAllowAnyTool, any tool works")
                    .define("customRequirePickaxe", false);
            customAllowAnyTool = builder
                    .comment("Custom chain works with empty hand / any item")
                    .define("customAllowAnyTool", true);
            customChainableBlocks = builder
                    .comment("Block IDs that chain when broken (supports other mods).",
                            "Example: minecraft:cobblestone, create:zinc_ore, thermal:ore_tin",
                            "Edit in-game: /sh chain add|remove|list")
                    .defineListAllowEmpty("customChainableBlocks",
                            new ArrayList<>(List.of(
                                    "minecraft:cobblestone",
                                    "minecraft:cobbled_deepslate",
                                    "minecraft:stone",
                                    "minecraft:netherrack",
                                    "minecraft:deepslate",
                                    "minecraft:dirt",
                                    "minecraft:gravel",
                                    "minecraft:sand",
                                    "minecraft:ice",
                                    "minecraft:packed_ice"
                            )),
                            o -> o instanceof String s && s.contains(":"));
            builder.pop();
        }
    }
}
