package com.superharvest.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Shared custom-chainable block list file, stable across mod upgrades.
 *
 * Canonical:  config/superharvest-custom.txt   (one block id per line)
 * Also reads: config/superharvest-common.toml  (customChainableBlocks / customChainable)
 * Legacy:     config/superharvest-custom-1.0.txt (ignored if empty)
 *
 * On load: merge all sources → write canonical file. Never lose user entries on upgrade.
 */
public final class CustomBlocksStore {
    public static final String FILE_NAME = "superharvest-custom.txt";
    public static final String TOML_NAME = "superharvest-common.toml";

    private CustomBlocksStore() {
    }

    public static List<String> defaults() {
        return List.of(
                "minecraft:cobblestone",
                "minecraft:cobbled_deepslate",
                "minecraft:stone",
                "minecraft:netherrack",
                "minecraft:deepslate"
        );
    }

    public static File configDir() {
        File dir = new File("config");
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File customFile() {
        return new File(configDir(), FILE_NAME);
    }

    public static String normalize(String raw) {
        if (raw == null) return null;
        String s = raw.trim().toLowerCase(Locale.ROOT).replace("\"", "");
        if (s.isEmpty() || s.startsWith("#")) return null;
        if (!s.contains(":")) s = "minecraft:" + s;
        return s;
    }

    /** Load + migrate + ensure file exists. Returns merged unique list. */
    public static List<String> load() {
        Set<String> merged = new LinkedHashSet<>();

        // 1) canonical txt
        readLines(customFile(), merged);
        // 2) toml custom list (Forge / NeoForge / older fabric notes)
        readTomlList(new File(configDir(), TOML_NAME), merged);
        // 3) any legacy names
        for (String legacy : new String[]{
                "superharvest-custom-1.0.txt",
                "superharvest_custom.txt",
                "superharvest-customs.txt"
        }) {
            readLines(new File(configDir(), legacy), merged);
        }

        if (merged.isEmpty()) {
            merged.addAll(defaults());
        }

        // always rewrite canonical file so upgrade keeps a single source
        write(merged);
        return new ArrayList<>(merged);
    }

    public static void save(Iterable<String> ids) {
        Set<String> merged = new LinkedHashSet<>();
        for (String id : ids) {
            String n = normalize(id);
            if (n != null) merged.add(n);
        }
        write(merged);
    }

    public static void add(String id) {
        List<String> list = load();
        String n = normalize(id);
        if (n == null) return;
        if (list.stream().noneMatch(s -> s.equalsIgnoreCase(n))) {
            list.add(n);
            save(list);
        }
    }

    public static void remove(String id) {
        String n = normalize(id);
        if (n == null) return;
        List<String> list = load();
        list.removeIf(s -> s.equalsIgnoreCase(n));
        save(list);
    }

    public static void clear() {
        save(List.of());
    }

    private static void readLines(File f, Set<String> out) {
        if (f == null || !f.isFile()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String n = normalize(line);
                if (n != null) out.add(n);
            }
        } catch (Exception ignored) {
        }
    }

    /** Minimal TOML list scrape: customChainableBlocks = [ "a", "b" ] or multi-line. */
    private static void readTomlList(File f, Set<String> out) {
        if (f == null || !f.isFile()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f, StandardCharsets.UTF_8))) {
            String line;
            boolean inList = false;
            while ((line = br.readLine()) != null) {
                String t = line.trim();
                if (t.startsWith("#")) continue;
                if (!inList && (t.contains("customChainableBlocks") || t.contains("customChainable"))) {
                    inList = t.contains("[") && !t.contains("]");
                    for (String part : t.split("[\\[\\],\\\"]")) {
                        String n = normalize(part);
                        if (n != null && n.contains(":")) out.add(n);
                    }
                    if (!inList && t.contains("]")) inList = false;
                    continue;
                }
                if (inList) {
                    if (t.contains("]")) inList = false;
                    for (String part : t.split("[\\[\\],\\\"]")) {
                        String n = normalize(part);
                        if (n != null && n.contains(":")) out.add(n);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static void write(Set<String> ids) {
        File f = customFile();
        try (PrintWriter w = new PrintWriter(f, StandardCharsets.UTF_8)) {
            w.println("# SuperHarvest custom chainable blocks");
            w.println("# Author: 来自太空的小头脑 (lztkdxtn@qq.com)");
            w.println("# One block id per line. Edit here or use /sh chain add|remove");
            w.println("# This file is shared across mod versions / loaders (upgrades keep your list).");
            w.println();
            ids.stream().sorted().forEach(w::println);
        } catch (Exception ignored) {
        }
    }
}
