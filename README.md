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
| 客户端安装 | 可选 |

---

## 功能

| 模式 | 触发 | 效果 |
|------|------|------|
| 连环挖矿 | 蹲下 + 镐子挖矿石 | 相连的同种矿石一起挖 |
| 连环伐木 | 蹲下 + 斧头砍原木 | 相连原木 + 树叶一起砍 |
| 连环收割 | 蹲下 + 锄头收成熟作物 | 相邻成熟作物一起收 |
| **自定义连锁** | 破坏列表中的方块 | **同种方块连锁（支持其它模组方块）** |

- 默认**蹲下才连锁**（`/sh mode` 可改经典模式）
- 邻接扫描默认 3×3×3，连锁数量有上限
- 每 tick 限量处理，降低卡顿

---

## 自定义可连锁方块

可加入原版或其它模组的方块：

| 指令 | 说明 |
|------|------|
| `/sh chain list` | 查看列表 |
| `/sh chain add <id>` | 添加，如 `create:zinc_ore` |
| `/sh chain add` | 对准方块后添加 |
| `/sh chain remove <id>` | 移除 |
| `/sh chain clear` | 清空（OP） |

配置文件：`config/superharvest-common.toml` → `customChainableBlocks`  
（Fabric 部分版本：`config/superharvest-custom.txt`）

---

## 指令一览

- `/superharvest` 或 `/sh`：总开关  
- `/sh mode`：蹲下 / 经典模式  
- `/sh farming` / `mining` / `logging` / `custom`：分项开关  
- `/sh chain …`：自定义连锁方块  
- `/sh about`：关于  

---

## 配置

路径：`config/superharvest-common.toml`（视加载器略有差异）

可调：蹲下触发、连锁半径、每 tick 数量、连锁上限、破坏树叶、损坏工具、自定义方块列表等。

---

## 支持版本（v1.2.0）

| 加载器 | Minecraft | 文件 |
|--------|-----------|------|
| Forge | 1.20.1 | `superharvest-forge-1.20.1-v1.2.0.jar` |
| Fabric / Quilt | 1.20.1–1.20.4 | `superharvest-fabric-quilt-1.20.1-v1.2.0.jar` |
| Fabric / Quilt | 1.21.x | `superharvest-fabric-quilt-1.21.1-v1.2.0.jar` |
| Fabric / Quilt | 26.1.x | `superharvest-fabric-quilt-26.1.x-v1.2.0.jar` |
| NeoForge | 1.21.1 | `superharvest-neoforge-1.21.1-v1.2.0.jar` |
| NeoForge | 26.1 | `superharvest-neoforge-26.1-v1.2.0.jar` |
| NeoForge | 26.2 | `superharvest-neoforge-26.2-v1.2.0.jar` |
| NeoForge | 26.3 | `superharvest-neoforge-26.3-v1.2.0.jar` |

**Quilt：** 使用对应 Fabric jar。  
**注意：** 不同 MC 大版本不能混用同一个 jar。

---

## 配置互通（升级）

- 规范文件：**config/superharvest-custom.txt**（一行一个方块 id）
- 启动时自动合并：旧 txt、superharvest-common.toml 里的 customChainableBlocks、历史文件名
- 文件不存在会自动创建；换版本 / 换加载器升级会保留你的列表
- 指令改完会立刻写回该文件

## 安装

1. 安装对应 Forge / NeoForge / Fabric（或 Quilt）。
2. 将 jar 放入 `mods`。多人**只需服务端**；单机放本地。
3. **蹲下**后用镐 / 斧 / 锄破坏方块即可连锁；自定义方块按列表触发。

---

## 图标

`icons/` 含官方图标与挖矿 / 伐木 / 收割分图标（含你提供的官方图 `superharvest-icon-official.png`）。

---

## 免责声明

个人学习与单机 / 私服使用之社区作品，与 Mojang、Microsoft、Bukkit/Spigot 及原插件作者无官方关联。原插件版权归原作者 Dru_TNT。


# SuperHarvest (Chain Harvesting)

**Author:** Little Brain from Space  
**Contact:** lztkdxtn@qq.com  
**License:** MIT  

Gameplay is modeled after the Bukkit plugin SuperHarvest (plugin author Dru_TNT). This is an unofficial port.

---

## Mod Type

| Item | Description |
|------|-------------|
| **Type** | **Server-side mod** (core logic runs on the server) |
| Multiplayer | **Only the server needs it installed**; chaining works even if clients do not install it |
| Singleplayer | Install one copy |
| Client installation | Optional |

---

## Features

| Mode | Trigger | Effect |
|------|---------|--------|
| Chain Mining | Sneak + mine ore with a pickaxe | Connected ores of the same type are mined together |
| Chain Logging | Sneak + chop logs with an axe | Connected logs + leaves are chopped together |
| Chain Harvesting | Sneak + harvest mature crops with a hoe | Adjacent mature crops are harvested together |
| **Custom Chaining** | Break blocks in the list | **Chains same-type blocks (supports blocks from other mods)** |

- By default, **sneaking is required for chaining** (`/sh mode` can switch to classic mode)
- Adjacency scan defaults to 3×3×3; chain count has a limit
- Processing is limited per tick to reduce lag

---

## Custom Chainable Blocks

You can add vanilla or modded blocks:

| Command | Description |
|---------|-------------|
| `/sh chain list` | View the list |
| `/sh chain add <id>` | Add, e.g. `create:zinc_ore` |
| `/sh chain add` | Add after aiming at a block |
| `/sh chain remove <id>` | Remove |
| `/sh chain clear` | Clear (OP) |

Config file: `config/superharvest-common.toml` → `customChainableBlocks`  
(Some Fabric versions: `config/superharvest-custom.txt`)

---

## Command Overview

- `/superharvest` or `/sh`: main toggle  
- `/sh mode`: Sneak / Classic mode  
- `/sh farming` / `mining` / `logging` / `custom`: individual toggles  
- `/sh chain …`: custom chainable blocks  
- `/sh about`: About  

---

## Configuration

Path: `config/superharvest-common.toml` (varies slightly by loader)

Adjustable: sneak trigger, chain radius, per-tick amount, chain limit, break leaves, damage tools, custom block list, etc.

---

## Supported Versions (v1.2.0)

| Loader | Minecraft | File |
|--------|-----------|------|
| Forge | 1.20.1 | `superharvest-forge-1.20.1-v1.2.0.jar` |
| Fabric / Quilt | 1.20.1–1.20.4 | `superharvest-fabric-quilt-1.20.1-v1.2.0.jar` |
| Fabric / Quilt | 1.21.x | `superharvest-fabric-quilt-1.21.1-v1.2.0.jar` |
| Fabric / Quilt | 26.1.x | `superharvest-fabric-quilt-26.1.x-v1.2.0.jar` |
| NeoForge | 1.21.1 | `superharvest-neoforge-1.21.1-v1.2.0.jar` |
| NeoForge | 26.1 | `superharvest-neoforge-26.1-v1.2.0.jar` |
| NeoForge | 26.2 | `superharvest-neoforge-26.2-v1.2.0.jar` |
| NeoForge | 26.3 | `superharvest-neoforge-26.3-v1.2.0.jar` |

**Quilt:** Use the corresponding Fabric jar.  
**Note:** Do not mix the same jar across different major MC versions.

---

## Config Interoperability (Upgrading)

- Canonical file: **config/superharvest-custom.txt** (one block ID per line)
- Automatically merged on startup: old txt, `customChainableBlocks` in `superharvest-common.toml`, legacy filenames
- The file is created automatically if missing; upgrading across versions/loaders preserves your list
- Changes made via commands are immediately written back to this file

## Installation

1. Install the corresponding Forge / NeoForge / Fabric (or Quilt).
2. Put the jar into `mods`. For multiplayer, **server-side only**; for singleplayer, place it locally.
3. **Sneak**, then break blocks with a pickaxe / axe / hoe to chain; custom blocks trigger according to the list.

---

## Icons

`icons/` contains the official icon and mining / logging / harvesting sub-icons (including the official image you provided, `superharvest-icon-official.png`).

---

## Disclaimer

A community work for personal learning and single-player/private server use. It is not officially affiliated with Mojang, Microsoft, Bukkit/Spigot, or the original plugin author. The original plugin's copyright belongs to the original author, Dru_TNT.