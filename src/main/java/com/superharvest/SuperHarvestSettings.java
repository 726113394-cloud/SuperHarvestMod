package com.superharvest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.superharvest.util.CustomBlocksStore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

/**
 * JSON settings. Categories match tools:
 * - hoe / crops   → cropBlocks
 * - pickaxe / ores → oreBlocks
 * - axe / logs    → logBlocks
 * - custom        → customBlocks (any, e.g. stone)
 */
public final class SuperHarvestSettings {
    public boolean sneakOnly = true;
    public boolean enableFarming = true;
    public boolean enableMining = true;
    public boolean enableLogging = true;
    public boolean enableCustom = true;
    public boolean breakLeaves = true;
    public boolean damageTools = true;
    public boolean requireCorrectTool = true;

    public boolean autoDetectOres = true;
    public boolean autoDetectWood = true;
    public boolean autoDetectCrops = true;
    public boolean autoDetectTools = false;

    public int maxChain = 256;
    public int maxLeaf = 64;
    public int blocksPerTick = 8;
    public int neighborRadius = 1;

    public List<String> cropBlocks = new ArrayList<>();
    public List<String> oreBlocks = new ArrayList<>();
    public List<String> logBlocks = new ArrayList<>();
    public List<String> customBlocks = new ArrayList<>();
    public List<String> toolItems = new ArrayList<>();
    public List<String> cropTools = new ArrayList<>();
    public List<String> oreTools = new ArrayList<>();
    public List<String> logTools = new ArrayList<>();

    public static final SuperHarvestSettings INSTANCE = new SuperHarvestSettings();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private SuperHarvestSettings() {
    }

    public static File file() {
        return new File(CustomBlocksStore.configDir(), "superharvest.json");
    }

    public static void load() {
        File f = file();
        if (f.isFile()) {
            try (FileReader r = new FileReader(f, StandardCharsets.UTF_8)) {
                SuperHarvestSettings loaded = GSON.fromJson(r, SuperHarvestSettings.class);
                if (loaded != null) INSTANCE.copyFrom(loaded);
            } catch (Exception ignored) {
            }
        }
        if (INSTANCE.autoDetectOres || INSTANCE.autoDetectWood || INSTANCE.autoDetectCrops) {
            rescanAll();
        }
        INSTANCE.customBlocks = normalizeList(INSTANCE.customBlocks);
        // keep custom txt in sync without resurrecting deleted defaults
        CustomBlocksStore.save(INSTANCE.customBlocks);
        save();
    }

    public static void save() {
        try (FileWriter w = new FileWriter(file(), StandardCharsets.UTF_8)) {
            GSON.toJson(INSTANCE, w);
        } catch (Exception ignored) {
        }
    }

    private static List<String> normalizeList(List<String> in) {
        List<String> out = new ArrayList<>();
        if (in == null) return out;
        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (String s : in) {
            String n = CustomBlocksStore.normalize(s);
            if (n != null) set.add(n);
        }
        out.addAll(set);
        return out;
    }

    private void copyFrom(SuperHarvestSettings o) {
        sneakOnly = o.sneakOnly;
        enableFarming = o.enableFarming;
        enableMining = o.enableMining;
        enableLogging = o.enableLogging;
        enableCustom = o.enableCustom;
        breakLeaves = o.breakLeaves;
        damageTools = o.damageTools;
        requireCorrectTool = o.requireCorrectTool;
        autoDetectOres = o.autoDetectOres;
        autoDetectWood = o.autoDetectWood;
        autoDetectCrops = o.autoDetectCrops;
        autoDetectTools = o.autoDetectTools;
        maxChain = o.maxChain;
        maxLeaf = o.maxLeaf;
        blocksPerTick = o.blocksPerTick;
        neighborRadius = o.neighborRadius;
        if (o.cropBlocks != null) cropBlocks = new ArrayList<>(o.cropBlocks);
        if (o.oreBlocks != null) oreBlocks = new ArrayList<>(o.oreBlocks);
        if (o.logBlocks != null) logBlocks = new ArrayList<>(o.logBlocks);
        if (o.customBlocks != null) customBlocks = new ArrayList<>(o.customBlocks);
        if (o.toolItems != null) toolItems = new ArrayList<>(o.toolItems);
        if (o.cropTools != null) cropTools = new ArrayList<>(o.cropTools);
        if (o.oreTools != null) oreTools = new ArrayList<>(o.oreTools);
        if (o.logTools != null) logTools = new ArrayList<>(o.logTools);
    }

    public boolean addCustom(String id) {
        String n = CustomBlocksStore.normalize(id);
        if (n == null) return false;
        for (String s : customBlocks) if (s.equalsIgnoreCase(n)) return false;
        customBlocks.add(n);
        save();
        CustomBlocksStore.save(customBlocks);
        return true;
    }

    public boolean removeCustom(String id) {
        String n = CustomBlocksStore.normalize(id);
        if (n == null) return false;
        boolean ok = customBlocks.removeIf(s -> s.equalsIgnoreCase(n));
        if (ok) {
            save();
            CustomBlocksStore.save(customBlocks);
        }
        return ok;
    }

    public boolean isCustom(String fullId) {
        String n = CustomBlocksStore.normalize(fullId);
        if (n == null) return false;
        for (String s : customBlocks) if (s.equalsIgnoreCase(n)) return true;
        return false;
    }

    public boolean isOreId(String fullId) {
        String n = CustomBlocksStore.normalize(fullId);
        if (n == null) return false;
        for (String s : oreBlocks) if (s.equalsIgnoreCase(n)) return true;
        String path = pathOf(n);
        return path.contains("ore");
    }

    public boolean isLogId(String fullId) {
        String n = CustomBlocksStore.normalize(fullId);
        if (n == null) return false;
        for (String s : logBlocks) if (s.equalsIgnoreCase(n)) return true;
        String path = pathOf(n);
        if (path.contains("planks")) return false;
        return path.contains("log") || path.contains("stem") || path.contains("wood") || path.contains("hyphae");
    }

    public boolean isCropId(String fullId) {
        String n = CustomBlocksStore.normalize(fullId);
        if (n == null) return false;
        for (String s : cropBlocks) if (s.equalsIgnoreCase(n)) return true;
        String path = pathOf(n);
        if (path.contains("stem") || path.contains("attached_melon") || path.contains("attached_pumpkin")) {
            return false;
        }
        return path.contains("wheat") || path.contains("carrot") || path.contains("potato")
                || path.contains("beetroot") || path.contains("nether_wart") || path.contains("cocoa")
                || path.contains("berry");
    }


    public boolean isHoeItem(String itemId) {
        String n = CustomBlocksStore.normalize(itemId);
        if (n == null) return false;
        String path = pathOf(n);
        if (path.endsWith("_hoe") || path.contains("_hoe")) return true;
        for (String s : cropTools) if (s.equalsIgnoreCase(n)) return true;
        return false;
    }

    public boolean isPickaxeItem(String itemId) {
        String n = CustomBlocksStore.normalize(itemId);
        if (n == null) return false;
        String path = pathOf(n);
        if (path.endsWith("_pickaxe") || path.contains("pickaxe")) return true;
        for (String s : oreTools) if (s.equalsIgnoreCase(n)) return true;
        return false;
    }

    public boolean isAxeItem(String itemId) {
        String n = CustomBlocksStore.normalize(itemId);
        if (n == null) return false;
        String path = pathOf(n);
        if ((path.endsWith("_axe") || path.contains("_axe")) && !path.contains("pickaxe")) return true;
        for (String s : logTools) if (s.equalsIgnoreCase(n)) return true;
        return false;
    }

    private static String pathOf(String id) {
        return id != null && id.contains(":") ? id.substring(id.indexOf(':') + 1) : id;
    }

    /** One-click scan: ores, logs (no planks), crops (no stems), optional tools. */
    public static void rescanAll() {
        rescanBlocks();
        if (INSTANCE.autoDetectTools) {
            rescanTools();
        }
        save();
    }

    public static void rescanTools() {
        List<String> tools = new ArrayList<>();
        try {
            for (var item : BuiltInRegistries.ITEM) {
                ResourceLocation rid = BuiltInRegistries.ITEM.getKey(item);
                if (rid == null) continue;
                String path = rid.getPath();
                if (path.contains("pickaxe") || path.endsWith("_axe") || path.contains("_hoe")) {
                    tools.add(rid.toString());
                }
            }
        } catch (Throwable ignored) {
        }
        tools.sort(String::compareTo);
        INSTANCE.toolItems = tools;
        INSTANCE.autoDetectTools = true;
        save();
    }

    public static void rescanBlocks() {
        List<String> ores = new ArrayList<>();
        List<String> logs = new ArrayList<>();
        List<String> crops = new ArrayList<>();
        try {
            for (Block b : BuiltInRegistries.BLOCK) {
                ResourceLocation rid = BuiltInRegistries.BLOCK.getKey(b);
                if (rid == null) continue;
                String id = rid.toString();
                String path = rid.getPath();
                if (INSTANCE.autoDetectOres && path.contains("ore")) {
                    ores.add(id);
                }
                // tree logs only — no planks / slabs / stairs
                if (INSTANCE.autoDetectWood && !path.contains("planks")
                        && (path.endsWith("_log") || path.endsWith("_wood")
                        || path.endsWith("_stem") || path.endsWith("_hyphae")
                        || path.contains("roots") || path.endsWith("mangrove_roots"))) {
                    logs.add(id);
                }
                if (INSTANCE.autoDetectCrops && isCropPath(path) && b instanceof Block) {
                    if (b instanceof CropBlock || b instanceof NetherWartBlock
                            || b instanceof CocoaBlock || b instanceof SweetBerryBushBlock
                            || path.contains("wheat") || path.contains("carrot") || path.contains("potato")
                            || path.contains("beetroot")) {
                        crops.add(id);
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        ores.sort(String::compareTo);
        logs.sort(String::compareTo);
        crops.sort(String::compareTo);
        INSTANCE.oreBlocks = mergePreserveManual(INSTANCE.oreBlocks, ores);
        INSTANCE.logBlocks = mergePreserveManual(INSTANCE.logBlocks, logs);
        INSTANCE.cropBlocks = mergePreserveManual(INSTANCE.cropBlocks, crops);
        save();
    }

    private static boolean isCropPath(String path) {
        if (path.contains("stem") || path.contains("attached_")) return false;
        return path.contains("wheat") || path.contains("carrot") || path.contains("potato")
                || path.contains("beetroot") || path.contains("nether_wart") || path.contains("cocoa")
                || path.contains("sweet_berry");
    }

    private static List<String> mergePreserveManual(List<String> oldList, List<String> scanned) {
        LinkedHashSet<String> out = new LinkedHashSet<>(normalizeList(scanned));
        // keep any custom user-added ids that scan missed
        if (oldList != null) {
            for (String s : oldList) {
                String n = CustomBlocksStore.normalize(s);
                if (n != null && (n.contains("mod") || !scanned.contains(n))) {
                    // keep manual mods ids and anything not auto
                    if (!out.contains(n) && (n.contains(":") && !n.startsWith("minecraft:") || !scanned.contains(n))) {
                        out.add(n);
                    }
                }
            }
        }
        return new ArrayList<>(out);
    }
}
