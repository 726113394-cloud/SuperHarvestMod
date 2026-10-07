package com.superharvest.client;

import com.superharvest.SuperHarvestSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Config with separate hoe / pickaxe / axe boards + custom list.
 * Shows localized names; search supports name + id fuzzy match.
 */
public final class SuperHarvestConfigScreen extends Screen {
    private final Screen parent;
    private int tab = 0; // 0 hoe 1 pick 2 axe 3 custom

    public SuperHarvestConfigScreen(Screen parent) {
        super(Component.literal("SuperHarvest 设置"));
        this.parent = parent;
    }

    static String displayName(String id) {
        try {
            ResourceLocation rid = ResourceLocation.tryParse(id);
            if (rid == null) return id;
            Item item = BuiltInRegistries.ITEM.get(rid);
            if (item != null) {
                if (item != Items.AIR) {
                    return Component.translatable(item.getDescriptionId()).getString();
                }
            }
            Block block = BuiltInRegistries.BLOCK.get(rid);
            if (block != null) {
                if (block != net.minecraft.world.level.block.Blocks.AIR) {
                    return block.getName().getString();
                }
            }
        } catch (Throwable ignored) {
        }
        return id;
    }

    @Override
    protected void init() {
        SuperHarvestSettings.load();
        int cx = width / 2;
        int y = 32;
        int w = 150;

        // tabs
        addRenderableWidget(Button.builder(Component.literal("锄头/作物"), b -> {
            tab = 0;
            rebuild();
        }).bounds(cx - 2 * w + 4, y, w - 6, 20).build());
        addRenderableWidget(Button.builder(Component.literal("镐子/矿物"), b -> {
            tab = 1;
            rebuild();
        }).bounds(cx - w + 2, y, w - 6, 20).build());
        addRenderableWidget(Button.builder(Component.literal("斧头/原木"), b -> {
            tab = 2;
            rebuild();
        }).bounds(cx + 2, y, w - 6, 20).build());
        addRenderableWidget(Button.builder(Component.literal("自定义方块"), b -> {
            tab = 3;
            rebuild();
        }).bounds(cx + w + 4, y, w - 6, 20).build());

        y += 28;
        switch (tab) {
            case 0 -> buildHoe(cx, y);
            case 1 -> buildPick(cx, y);
            case 2 -> buildAxe(cx, y);
            default -> buildCustom(cx, y);
        }
    }

    private void buildHoe(int cx, int y) {
        int w = 300;
        addRenderableWidget(Button.builder(t("连环收割", SuperHarvestSettings.INSTANCE.enableFarming), b -> {
            SuperHarvestSettings.INSTANCE.enableFarming = !SuperHarvestSettings.INSTANCE.enableFarming;
            SuperHarvestSettings.save();
            rebuild();
        }).bounds(cx - w / 2, y, w, 20).build());
        addRenderableWidget(Button.builder(t("自动识别作物", SuperHarvestSettings.INSTANCE.autoDetectCrops), b -> {
            SuperHarvestSettings.INSTANCE.autoDetectCrops = !SuperHarvestSettings.INSTANCE.autoDetectCrops;
            SuperHarvestSettings.rescanAll();
            SuperHarvestSettings.save();
            rebuild();
        }).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("管理作物列表（" + SuperHarvestSettings.INSTANCE.cropBlocks.size() + "）…"), b ->
                minecraft.setScreen(new ListScreen(this, "crop"))
        ).bounds(cx - w / 2, y += 28, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("从全部方块添加作物…"), b ->
                minecraft.setScreen(new BlockPickerScreen(this, "crop"))
        ).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("自定义锄类工具（" + SuperHarvestSettings.INSTANCE.cropTools.size() + "）…"), b ->
                minecraft.setScreen(new ToolPickerScreen(this, "crop"))
        ).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("一键扫描全部工具"), b -> {
            SuperHarvestSettings.rescanTools();
            rebuild();
        }).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("返回 / 完成"), b -> onClose())
                .bounds(cx - w / 2, y += 28, w, 20).build());
    }

    private void buildPick(int cx, int y) {
        int w = 300;
        addRenderableWidget(Button.builder(t("连环挖矿", SuperHarvestSettings.INSTANCE.enableMining), b -> {
            SuperHarvestSettings.INSTANCE.enableMining = !SuperHarvestSettings.INSTANCE.enableMining;
            SuperHarvestSettings.save();
            rebuild();
        }).bounds(cx - w / 2, y, w, 20).build());
        addRenderableWidget(Button.builder(t("自动识别矿物", SuperHarvestSettings.INSTANCE.autoDetectOres), b -> {
            SuperHarvestSettings.INSTANCE.autoDetectOres = !SuperHarvestSettings.INSTANCE.autoDetectOres;
            SuperHarvestSettings.rescanAll();
            SuperHarvestSettings.save();
            rebuild();
        }).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("管理矿物列表（" + SuperHarvestSettings.INSTANCE.oreBlocks.size() + "）…"), b ->
                minecraft.setScreen(new ListScreen(this, "ore"))
        ).bounds(cx - w / 2, y += 28, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("从全部方块添加矿物…"), b ->
                minecraft.setScreen(new BlockPickerScreen(this, "ore"))
        ).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("自定义镐类工具（" + SuperHarvestSettings.INSTANCE.oreTools.size() + "）…"), b ->
                minecraft.setScreen(new ToolPickerScreen(this, "ore"))
        ).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("一键扫描全部工具"), b -> {
            SuperHarvestSettings.rescanTools();
            rebuild();
        }).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("返回 / 完成"), b -> onClose())
                .bounds(cx - w / 2, y += 28, w, 20).build());
    }

    private void buildAxe(int cx, int y) {
        int w = 300;
        addRenderableWidget(Button.builder(t("连环伐木", SuperHarvestSettings.INSTANCE.enableLogging), b -> {
            SuperHarvestSettings.INSTANCE.enableLogging = !SuperHarvestSettings.INSTANCE.enableLogging;
            SuperHarvestSettings.save();
            rebuild();
        }).bounds(cx - w / 2, y, w, 20).build());
        addRenderableWidget(Button.builder(t("破坏树叶", SuperHarvestSettings.INSTANCE.breakLeaves), b -> {
            SuperHarvestSettings.INSTANCE.breakLeaves = !SuperHarvestSettings.INSTANCE.breakLeaves;
            SuperHarvestSettings.save();
            rebuild();
        }).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(t("自动识别原木/木板", SuperHarvestSettings.INSTANCE.autoDetectWood), b -> {
            SuperHarvestSettings.INSTANCE.autoDetectWood = !SuperHarvestSettings.INSTANCE.autoDetectWood;
            SuperHarvestSettings.rescanAll();
            SuperHarvestSettings.save();
            rebuild();
        }).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("管理原木列表（" + SuperHarvestSettings.INSTANCE.logBlocks.size() + "）…"), b ->
                minecraft.setScreen(new ListScreen(this, "log"))
        ).bounds(cx - w / 2, y += 28, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("从全部方块添加原木…"), b ->
                minecraft.setScreen(new BlockPickerScreen(this, "log"))
        ).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("自定义斧类工具（" + SuperHarvestSettings.INSTANCE.logTools.size() + "）…"), b ->
                minecraft.setScreen(new ToolPickerScreen(this, "log"))
        ).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("一键扫描全部工具"), b -> {
            SuperHarvestSettings.rescanTools();
            rebuild();
        }).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("返回 / 完成"), b -> onClose())
                .bounds(cx - w / 2, y += 28, w, 20).build());
    }

    private void buildCustom(int cx, int y) {
        int w = 300;
        addRenderableWidget(Button.builder(t("自定义连锁", SuperHarvestSettings.INSTANCE.enableCustom), b -> {
            SuperHarvestSettings.INSTANCE.enableCustom = !SuperHarvestSettings.INSTANCE.enableCustom;
            SuperHarvestSettings.save();
            rebuild();
        }).bounds(cx - w / 2, y, w, 20).build());
        addRenderableWidget(Button.builder(t("蹲下才连锁", SuperHarvestSettings.INSTANCE.sneakOnly), b -> {
            SuperHarvestSettings.INSTANCE.sneakOnly = !SuperHarvestSettings.INSTANCE.sneakOnly;
            SuperHarvestSettings.save();
            rebuild();
        }).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("管理自定义列表（" + SuperHarvestSettings.INSTANCE.customBlocks.size() + "）…"), b ->
                minecraft.setScreen(new ListScreen(this, "custom"))
        ).bounds(cx - w / 2, y += 28, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("从全部方块添加（石头/黑曜石等）…"), b ->
                minecraft.setScreen(new BlockPickerScreen(this, "custom"))
        ).bounds(cx - w / 2, y += 22, w, 20).build());
        addRenderableWidget(Button.builder(Component.literal("返回 / 完成"), b -> onClose())
                .bounds(cx - w / 2, y += 28, w, 20).build());
    }

    private static Component t(String name, boolean on) {
        return Component.literal(name + ": " + (on ? "开" : "关"));
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partial) {
        g.drawCenteredString(font, title, width / 2, 12, 0xFFFFFF);
        super.render(g, mouseX, mouseY, partial);
    }

    @Override
    public void onClose() {
        SuperHarvestSettings.save();
        if (minecraft != null) minecraft.setScreen(parent);
    }

    private void rebuild() {
        clearWidgets();
        init();
    }

    private static List<String> listFor(String kind) {
        return switch (kind) {
            case "ore" -> SuperHarvestSettings.INSTANCE.oreBlocks;
            case "log" -> SuperHarvestSettings.INSTANCE.logBlocks;
            case "crop" -> SuperHarvestSettings.INSTANCE.cropBlocks;
            default -> SuperHarvestSettings.INSTANCE.customBlocks;
        };
    }

    private static void addToList(String kind, String id) {
        List<String> list = listFor(kind);
        String n = com.superharvest.util.CustomBlocksStore.normalize(id);
        if (n == null) return;
        for (String s : list) if (s.equalsIgnoreCase(n)) return;
        list.add(n);
        SuperHarvestSettings.save();
    }

    private static void removeFromList(String kind, String id) {
        String n = com.superharvest.util.CustomBlocksStore.normalize(id);
        if (n == null) return;
        listFor(kind).removeIf(s -> s.equalsIgnoreCase(n));
        SuperHarvestSettings.save();
        if ("custom".equals(kind)) {
            com.superharvest.util.CustomBlocksStore.save(listFor(kind));
        }
    }

    static final class BlockPickerScreen extends Screen {
        private final Screen parent;
        private final String kind;
        private EditBox search;
        private final List<String> all = new ArrayList<>();
        private List<String> filtered = new ArrayList<>();
        private int scroll = 0;
        private String selectedId = "";
        private final List<String> rowIds = new ArrayList<>();
        private final List<int[]> rowRects = new ArrayList<>();

        BlockPickerScreen(Screen parent, String kind) {
            super(Component.literal("选择方块 → " + kind));
            this.parent = parent;
            this.kind = kind;
        }

        private net.minecraft.world.item.ItemStack selectedStack() {
            try {
                ResourceLocation rid = ResourceLocation.tryParse(selectedId);
                if (rid != null) {
                    Block b = BuiltInRegistries.BLOCK.get(rid);
                    if (b != null) {
                        return new net.minecraft.world.item.ItemStack(b.asItem());
                    }
                    Item item = BuiltInRegistries.ITEM.get(rid);
                    if (item != null) {
                        return new net.minecraft.world.item.ItemStack(item);
                    }
                }
            } catch (Throwable ignored) {
            }
            return net.minecraft.world.item.ItemStack.EMPTY;
        }

        @Override
        protected void init() {
            all.clear();
            for (Block b : BuiltInRegistries.BLOCK) {
                ResourceLocation id = BuiltInRegistries.BLOCK.getKey(b);
                if (id != null) all.add(id.toString());
            }
            all.sort(String::compareTo);
            search = new EditBox(font, width / 2 - 170, 28, 240, 18, Component.literal("搜索名称 / id"));
            search.setResponder(s -> {
                filter(s);
                scroll = 0;
                rebuildList();
            });
            addRenderableWidget(search);
            filter("");
            if (!filtered.isEmpty() && selectedId.isEmpty()) {
                selectedId = filtered.get(0);
            }
            rebuildList();
        }

        private void filter(String q) {
            String s = q == null ? "" : q.trim().toLowerCase(Locale.ROOT);
            filtered = new ArrayList<>();
            for (String id : all) {
                if (s.isEmpty() || id.toLowerCase(Locale.ROOT).contains(s)
                        || displayName(id).toLowerCase(Locale.ROOT).contains(s)) {
                    filtered.add(id);
                }
            }
        }

        private void rebuildList() {
            clearWidgets();
            rowIds.clear();
            rowRects.clear();
            addRenderableWidget(search);
            int listX = width / 2 - 170;
            int y = 52;
            int maxRows = Math.min(11, Math.max(0, filtered.size() - scroll));
            for (int i = 0; i < maxRows; i++) {
                final String id = filtered.get(scroll + i);
                boolean on = listFor(kind).stream().anyMatch(x -> x.equalsIgnoreCase(id));
                rowIds.add(id);
                rowRects.add(new int[]{listX, y, 240, 18});
                addRenderableWidget(Button.builder(
                        Component.literal((on ? "§a[已加] §f" : "§7[添加] §f") + displayName(id)),
                        b -> {
                            selectedId = id;
                            if (on) removeFromList(kind, id);
                            else addToList(kind, id);
                            filter(search.getValue());
                            rebuildList();
                        }).bounds(listX, y, 240, 18).build());
                y += 20;
            }
            addRenderableWidget(Button.builder(Component.literal("↑"), b -> {
                scroll = Math.max(0, scroll - 11);
                rebuildList();
            }).bounds(listX, y + 6, 40, 18).build());
            addRenderableWidget(Button.builder(Component.literal("↓"), b -> {
                if (scroll + 11 < filtered.size()) scroll += 11;
                rebuildList();
            }).bounds(listX + 45, y + 6, 40, 18).build());
            addRenderableWidget(Button.builder(Component.literal("返回"), b -> onClose())
                    .bounds(listX + 95, y + 6, 70, 18).build());
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partial) {
            for (int i = 0; i < rowIds.size() && i < rowRects.size(); i++) {
                int[] r = rowRects.get(i);
                if (mouseX >= r[0] && mouseX <= r[0] + r[2] && mouseY >= r[1] && mouseY <= r[1] + r[3]) {
                    selectedId = rowIds.get(i);
                    break;
                }
            }
            g.drawCenteredString(font, title, width / 2, 10, 0xFFFFFF);
            super.render(g, mouseX, mouseY, partial);
            // preview panel (right)
            int px = width / 2 + 90;
            int py = 52;
            g.fill(px, py, px + 76, py + 76, 0x66000000);
            g.fill(px + 1, py + 1, px + 75, py + 75, 0x33222222);
            var stack = selectedStack();
            if (!stack.isEmpty()) {
                g.renderItem(stack, px + 22, py + 22, 1);
            }
            if (!selectedId.isEmpty()) {
                g.drawCenteredString(font, displayName(selectedId), px + 38, py + 82, 0xFFFFFF);
                g.drawCenteredString(font, selectedId, px + 38, py + 96, 0xAAAAAA);
            } else {
                g.drawCenteredString(font, "点击方块查看图标", px + 38, py + 36, 0x888888);
            }
            g.drawString(font, "预览 / 图标（跟随鼠标）", px + 4, py - 14, 0x88CCFF);
        }

        @Override
        public void onClose() {
            SuperHarvestSettings.save();
            minecraft.setScreen(parent);
        }
    }


    static final class ToolPickerScreen extends Screen {
        private final Screen parent;
        private final String kind;
        private EditBox search;
        private final List<String> all = new ArrayList<>();
        private List<String> filtered = new ArrayList<>();
        private int scroll = 0;
        private String selectedId = "";
        private final List<String> rowIds = new ArrayList<>();
        private final List<int[]> rowRects = new ArrayList<>();

        ToolPickerScreen(Screen parent, String kind) {
            super(Component.literal("自定义工具 → " + kind));
            this.parent = parent;
            this.kind = kind;
        }

        private java.util.List<String> listFor() {
            return switch (kind) {
                case "ore" -> SuperHarvestSettings.INSTANCE.oreTools;
                case "log" -> SuperHarvestSettings.INSTANCE.logTools;
                default -> SuperHarvestSettings.INSTANCE.cropTools;
            };
        }

        @Override
        protected void init() {
            all.clear();
            for (Item item : BuiltInRegistries.ITEM) {
                ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
                if (id != null) all.add(id.toString());
            }
            all.sort(String::compareTo);
            search = new EditBox(font, width / 2 - 170, 28, 240, 18, Component.literal("搜索工具名称 / id"));
            search.setResponder(s -> {
                filter(s);
                scroll = 0;
                rebuildList();
            });
            addRenderableWidget(search);
            filter("");
            if (!filtered.isEmpty()) selectedId = filtered.get(0);
            rebuildList();
        }

        private void filter(String q) {
            String s = q == null ? "" : q.trim().toLowerCase(Locale.ROOT);
            filtered = new ArrayList<>();
            for (String id : all) {
                if (s.isEmpty() || id.toLowerCase(Locale.ROOT).contains(s)
                        || displayName(id).toLowerCase(Locale.ROOT).contains(s)) {
                    filtered.add(id);
                }
            }
        }

        private void rebuildList() {
            clearWidgets();
            rowIds.clear();
            rowRects.clear();
            addRenderableWidget(search);
            int listX = width / 2 - 170;
            int y = 52;
            int maxRows = Math.min(11, Math.max(0, filtered.size() - scroll));
            for (int i = 0; i < maxRows; i++) {
                final String id = filtered.get(scroll + i);
                boolean on = listFor().stream().anyMatch(x -> x.equalsIgnoreCase(id));
                rowIds.add(id);
                rowRects.add(new int[]{listX, y, 240, 18});
                addRenderableWidget(Button.builder(
                        Component.literal((on ? "§a[已加] §f" : "§7[添加] §f") + displayName(id)),
                        b -> {
                            selectedId = id;
                            if (on) listFor().removeIf(x -> x.equalsIgnoreCase(id));
                            else listFor().add(id);
                            SuperHarvestSettings.save();
                            rebuildList();
                        }).bounds(listX, y, 240, 18).build());
                y += 20;
            }
            addRenderableWidget(Button.builder(Component.literal("↑"), b -> {
                scroll = Math.max(0, scroll - 11);
                rebuildList();
            }).bounds(listX, y + 6, 40, 18).build());
            addRenderableWidget(Button.builder(Component.literal("↓"), b -> {
                if (scroll + 11 < filtered.size()) scroll += 11;
                rebuildList();
            }).bounds(listX + 45, y + 6, 40, 18).build());
            addRenderableWidget(Button.builder(Component.literal("返回"), b -> onClose())
                    .bounds(listX + 95, y + 6, 70, 18).build());
        }

        private net.minecraft.world.item.ItemStack selectedStack() {
            try {
                ResourceLocation rid = ResourceLocation.tryParse(selectedId);
                if (rid != null) {
                    Item item = BuiltInRegistries.ITEM.get(rid);
                    if (item != null) {
                        return new net.minecraft.world.item.ItemStack(item);
                    }
                }
            } catch (Throwable ignored) {
            }
            return net.minecraft.world.item.ItemStack.EMPTY;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partial) {
            for (int i = 0; i < rowIds.size() && i < rowRects.size(); i++) {
                int[] r = rowRects.get(i);
                if (mouseX >= r[0] && mouseX <= r[0] + r[2] && mouseY >= r[1] && mouseY <= r[1] + r[3]) {
                    selectedId = rowIds.get(i);
                    break;
                }
            }
            g.drawCenteredString(font, title, width / 2, 10, 0xFFFFFF);
            super.render(g, mouseX, mouseY, partial);
            int px = width / 2 + 90;
            int py = 52;
            g.fill(px, py, px + 76, py + 76, 0x66000000);
            var stack = selectedStack();
            if (!stack.isEmpty()) {
                g.renderItem(stack, px + 22, py + 22, 1);
            }
            if (!selectedId.isEmpty()) {
                g.drawCenteredString(font, displayName(selectedId), px + 38, py + 82, 0xFFFFFF);
                g.drawCenteredString(font, selectedId, px + 38, py + 96, 0xAAAAAA);
            }
            g.drawString(font, "工具预览（跟随鼠标）", px + 4, py - 14, 0x88CCFF);
        }

        @Override
        public void onClose() {
            SuperHarvestSettings.save();
            minecraft.setScreen(parent);
        }
    }

    static final class ListScreen extends Screen {
        private final Screen parent;
        private final String kind;
        private EditBox search;
        private List<String> filtered = new ArrayList<>();
        private int scroll = 0;

        ListScreen(Screen parent, String kind) {
            super(Component.literal("列表 → " + kind));
            this.parent = parent;
            this.kind = kind;
        }

        @Override
        protected void init() {
            search = new EditBox(font, width / 2 - 150, 28, 300, 18, Component.literal("搜索名称 / id"));
            search.setResponder(s -> {
                filter(s);
                scroll = 0;
                rebuild();
            });
            addRenderableWidget(search);
            filter("");
            rebuild();
        }

        private void filter(String q) {
            String s = q == null ? "" : q.trim().toLowerCase(Locale.ROOT);
            filtered = new ArrayList<>();
            for (String id : listFor(kind)) {
                if (s.isEmpty() || id.toLowerCase(Locale.ROOT).contains(s)
                        || displayName(id).toLowerCase(Locale.ROOT).contains(s)) {
                    filtered.add(id);
                }
            }
        }

        private void rebuild() {
            clearWidgets();
            addRenderableWidget(search);
            int y = 52;
            int maxRows = Math.min(11, Math.max(0, filtered.size() - scroll));
            for (int i = 0; i < maxRows; i++) {
                final String id = filtered.get(scroll + i);
                addRenderableWidget(Button.builder(
                        Component.literal("§c移除 §f" + displayName(id) + " §8" + id),
                        b -> {
                            removeFromList(kind, id);
                            filter(search.getValue());
                            rebuild();
                        }).bounds(width / 2 - 160, y, 320, 18).build());
                y += 20;
            }
            addRenderableWidget(Button.builder(Component.literal("↑ 上页"), b -> {
                scroll = Math.max(0, scroll - 11);
                rebuild();
            }).bounds(width / 2 - 160, y + 6, 70, 18).build());
            addRenderableWidget(Button.builder(Component.literal("↓ 下页"), b -> {
                if (scroll + 11 < filtered.size()) scroll += 11;
                rebuild();
            }).bounds(width / 2 - 85, y + 6, 70, 18).build());
            addRenderableWidget(Button.builder(Component.literal("重新扫描"), b -> {
                SuperHarvestSettings.rescanAll();
                filter(search.getValue());
                rebuild();
            }).bounds(width / 2 - 5, y + 6, 90, 18).build());
            addRenderableWidget(Button.builder(Component.literal("返回"), b -> onClose())
                    .bounds(width / 2 + 95, y + 6, 65, 18).build());
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partial) {
            g.drawCenteredString(font, title, width / 2, 10, 0xFFFFFF);
            super.render(g, mouseX, mouseY, partial);
        }

        @Override
        public void onClose() {
            SuperHarvestSettings.save();
            minecraft.setScreen(parent);
        }
    }
}
