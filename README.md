# SuperHarvest（连锁采集）

**作者：** 来自太空的小头脑  
**联系：** lztkdxtn@qq.com  
**许可：** MIT  

玩法仿照 Bukkit 插件 SuperHarvest（插件作者 Dru_TNT），非官方移植。

---

## 模组类型

| 项目 | 说明 |
|------|------|
| **类型** | **服务端模组**（核心逻辑在服务端） |
| 多人游戏 | **只需服务端安装**，客户端不装也能连锁 |
| 单人游戏 | 装一份即可 |
| 客户端安装 | 可选（配置界面、Mod Menu 入口） |

---

## 功能

| 模式 | 触发 | 效果 |
|------|------|------|
| 连环挖矿 | 蹲下 + 镐子挖矿石 | 相连的同种矿石一起挖 |
| 连环伐木 | 蹲下 + 斧头砍原木 | 相连原木 + 树叶一起砍（**不含木板**） |
| 连环收割 | 蹲下 + 锄头收成熟作物 | 相邻成熟作物一起收（**不连西/南瓜茎**） |
| **自定义连锁** | 破坏列表中的方块 | **同种方块连锁（支持其它模组方块）** |

- 默认**蹲下才连锁**（`/sh mode` 可改经典模式）
- 邻接扫描默认 3×3×3，连锁数量有上限
- 每 tick 限量处理，降低卡顿
- **切换主手工具会中断**进行中的连锁
- **自动识别**：矿物 / 原木（不含木板）/ 作物（不含茎）/ 镐斧锄工具（可在配置里关闭）

---

## 配置界面（Mod Menu 等）

在模组列表（Mod Menu / Catalogue 等）打开 SuperHarvest 配置：

- **锄头/作物、镐子/矿物、斧头/原木、自定义方块** 四个分页
- 方块列表支持 **物品名称 + id 模糊搜索**
- 右侧 **图标预览跟随鼠标** 指到哪显示哪个
- **一键扫描** 方块 / 工具
- 可浏览**全部注册方块/物品**（含其它模组）并添加
- Malilib：读写 `config/superharvest-malilib.json`，兼容 Malilib 系配置编辑

---

## 自定义可连锁方块

| 指令 | 说明 |
|------|------|
| `/sh chain list` | 查看列表 |
| `/sh chain add <id>` | 添加，如 `create:zinc_ore` |
| `/sh chain add` | 对准方块后添加 |
| `/sh chain remove <id>` | 移除 |
| `/sh chain clear` | 清空 |

配置文件：

- 规范列表：`config/superharvest-custom.txt`（一行一个 id）
- 全量设置：`config/superharvest.json`
- Malilib 侧车：`config/superharvest-malilib.json`
- Forge / NeoForge：`config/superharvest-common.toml`

---

## 指令一览

- `/superharvest` 或 `/sh`：总开关  
- `/sh mode`：蹲下 / 经典模式  
- `/sh farming` / `mining` / `logging` / `custom`：分项开关  
- `/sh chain …`：自定义连锁方块  
- `/sh config`：打开配置说明  
- `/sh about`：关于  

---

## 配置互通（升级）

- 规范文件：**`config/superharvest-custom.txt`**（一行一个方块 id）
- 启动时自动合并：旧 txt、`superharvest-common.toml` 中的 `customChainableBlocks`、历史文件名
- 文件不存在会自动创建；**仅首次安装写入默认 5 块**，删空后不会自动回填
- 换版本 / 换加载器升级会保留你的列表；指令修改立即写回

---

## 支持版本（dist/ 无版本号后缀）

| 加载器 | Minecraft | 文件 |
|--------|-----------|------|
| Forge | 1.20.1 | `superharvest-forge-1.20.1.jar` |
| Fabric / Quilt | 1.20.1 | `superharvest-fabric-quilt-1.20.1.jar` |
| Fabric / Quilt | 1.21.x | `superharvest-fabric-quilt-1.21.1.jar` |
| Fabric / Quilt | 26.1.x | `superharvest-fabric-quilt-26.1.x.jar` |
| NeoForge | 1.21.1 | `superharvest-neoforge-1.21.1.jar` |
| NeoForge | 26.1 | `superharvest-neoforge-26.1.jar` |
| NeoForge | 26.2 | `superharvest-neoforge-26.2.jar` |
| NeoForge | 26.3 | `superharvest-neoforge-26.3.jar` |

**Quilt：** 使用对应 Fabric jar。  
**注意：** 不同 MC 大版本不能混用同一个 jar；Forge 1.20.2–1.20.6 尚未单独适配。

---

## 安装

1. 安装对应 Forge / NeoForge / Fabric（或 Quilt）。
2. 将 jar 放入 `mods`。多人**只需服务端**；单机放本地。
3. **蹲下**后用镐 / 斧 / 锄破坏方块即可连锁；自定义方块按列表触发。

---

## 图标

`icons/` 含官方图标与挖矿 / 伐木 / 收割分图标（含 `superharvest-icon-official.png`）。

---

## 免责声明

个人学习与单机 / 私服使用之社区作品，与 Mojang、Microsoft、Bukkit/Spigot 及原插件作者无官方关联。原插件版权归原作者 Dru_TNT。

---

---

# SuperHarvest (Chain Harvesting)

**Author:** Little Brain from Space  
**Contact:** lztkdxtn@qq.com  
**License:** MIT  

Gameplay is modeled after the Bukkit plugin SuperHarvest (plugin author Dru_TNT). Unofficial port.

---

## Mod Type

| Item | Description |
|------|-------------|
| **Type** | **Server-side mod** (core logic runs on the server) |
| Multiplayer | **Only the server needs it installed** |
| Singleplayer | Install one copy |
| Client installation | Optional (config UI / Mod Menu entry) |

---

## Features

| Mode | Trigger | Effect |
|------|---------|--------|
| Chain Mining | Sneak + mine ore with a pickaxe | Same-type connected ores are mined together |
| Chain Logging | Sneak + chop logs with an axe | Connected logs + leaves ( **no planks** ) |
| Chain Harvesting | Sneak + harvest mature crops with a hoe | Adjacent mature crops ( **no melon/pumpkin stems** ) |
| **Custom Chaining** | Break blocks on the list | **Same-type chain (supports modded blocks)** |

- **Sneaking required** by default (`/sh mode` switches to classic mode)
- Adjacency scan default 3×3×3; chain count is capped
- Per-tick processing limit reduces lag
- **Switching the held tool cancels** an in-progress chain
- **Auto-detect**: ores / logs (not planks) / crops (not stems) / pickaxe-axe-hoe tools (can disable)

---

## Config UI (Mod Menu etc.)

Open SuperHarvest from the mod list (Mod Menu / Catalogue / similar):

- Tabs: **Hoe & crops / Pickaxe & ores / Axe & logs / Custom blocks**
- Lists support **display-name + id fuzzy search**
- Right-side **icon preview follows the mouse**
- **One-click scan** for blocks / tools
- Browse **all registered blocks/items** (including other mods) to add
- Malilib: reads/writes `config/superharvest-malilib.json`

---

## Custom Chainable Blocks

| Command | Description |
|---------|-------------|
| `/sh chain list` | View list |
| `/sh chain add <id>` | Add (e.g. `create:zinc_ore`) |
| `/sh chain add` | Add while aiming at a block |
| `/sh chain remove <id>` | Remove |
| `/sh chain clear` | Clear |

Config files:

- Canonical list: `config/superharvest-custom.txt`
- Full settings: `config/superharvest.json`
- Malilib sidecar: `config/superharvest-malilib.json`
- Forge / NeoForge: `config/superharvest-common.toml`

---

## Command Overview

- `/superharvest` or `/sh`: main toggle  
- `/sh mode`: sneak / classic  
- `/sh farming` / `mining` / `logging` / `custom`: per-feature toggles  
- `/sh chain …`: custom chainable blocks  
- `/sh config`: config hint  
- `/sh about`: about  

---

## Supported Builds (in `dist/`, no version suffix)

| Loader | Minecraft | File |
|--------|-----------|------|
| Forge | 1.20.1 | `superharvest-forge-1.20.1.jar` |
| Fabric / Quilt | 1.20.1 | `superharvest-fabric-quilt-1.20.1.jar` |
| Fabric / Quilt | 1.21.x | `superharvest-fabric-quilt-1.21.1.jar` |
| Fabric / Quilt | 26.1.x | `superharvest-fabric-quilt-26.1.x.jar` |
| NeoForge | 1.21.1 | `superharvest-neoforge-1.21.1.jar` |
| NeoForge | 26.1 | `superharvest-neoforge-26.1.jar` |
| NeoForge | 26.2 | `superharvest-neoforge-26.2.jar` |
| NeoForge | 26.3 | `superharvest-neoforge-26.3.jar` |

**Quilt:** use the matching Fabric jar.  
**Note:** do not mix jars across major MC versions; Forge 1.20.2–1.20.6 is not adapted yet.

---

## Installation

1. Install the matching loader (Forge / NeoForge / Fabric / Quilt).
2. Put the jar in `mods`. Multiplayer: **server only**; singleplayer: local.
3. **Sneak** and break with pickaxe / axe / hoe; custom blocks follow the list.

---

## Icons

`icons/` contains the official icon and mining/logging/harvesting variants (including `superharvest-icon-official.png`).

---

## Disclaimer

A community project for personal learning and single-player/private servers. Not officially affiliated with Mojang, Microsoft, Bukkit/Spigot, or the original plugin author. Original plugin copyright: Dru_TNT.
