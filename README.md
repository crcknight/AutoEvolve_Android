# 棋阵演化 AutoEvolve

一款基于遗传算法的自走棋阵容智能演化 Android App，软件工程期末大作业项目。

## 功能概述

- **英雄库**：浏览内置英雄数据（费用、血量、攻击、攻速、羁绊）
- **阵容编辑**：拖拽英雄到棋盘，实时显示费用，保存自定义阵容
- **模拟对战**：选择双方阵容，展示逐回合战斗日志与结果
- **智能演化**：基于遗传算法演化最优阵容+站位，展示适应度曲线

## 技术栈

| 技术 | 用途 |
|---|---|
| Java 17 | 编程语言 |
| Android SDK 34 | 目标平台 |
| Gradle 8.7 | 构建工具 |
| Room 2.6.1 | SQLite ORM 持久化 |
| Material Design | UI 组件 |

## 一键运行

### 方式一：Android Studio（推荐）

```bash
# 1. 克隆仓库
git clone <仓库地址>
cd AutoEvolve_Android

# 2. 用 Android Studio 打开项目
#    File -> Open -> 选择 AutoEvolve_Android 文件夹

# 3. 等待 Gradle 同步完成（首次约 2-5 分钟）

# 4. 连接真机或启动模拟器

# 5. 点击工具栏 "Run" 按钮（▶）
```

### 方式二：命令行构建 APK

```bash
# 1. 克隆仓库
git clone <仓库地址>
cd AutoEvolve_Android

# 2. 构建 Debug APK
./gradlew assembleDebug

# 3. 安装到已连接设备
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 项目结构

```
app/src/main/java/com/autoevolve/
├── MainActivity.java              # 主入口，底部导航栏
├── AppDatabase.java               # Room 数据库
├── entity/                        # 数据实体
│   ├── HeroEntity.java
│   ├── FormationEntity.java
│   └── SynergyEntity.java
├── dao/                           # 数据访问对象
│   ├── HeroDao.java
│   ├── FormationDao.java
│   └── SynergyDao.java
├── model/                         # 业务模型
│   ├── Hero.java
│   ├── Formation.java
│   ├── Slot.java
│   ├── Position.java
│   ├── BattleResult.java
│   └── Synergy.java
├── fragment/                      # 四大界面模块
│   ├── HeroFragment.java          # 英雄库
│   ├── FormationFragment.java     # 阵容编辑
│   ├── BattleFragment.java        # 模拟对战
│   ├── EvolveFragment.java        # 智能演化
│   └── FitnessCurveView.java      # 适应度曲线 Canvas
├── simulator/                     # 战斗模拟
│   └── BattleSimulator.java
└── agent/                         # 智能体核心
    ├── EvolutionAgent.java
    ├── GeneticAlgorithm.java
    ├── FitnessEvaluator.java
    └── Chromosome.java
```

## 智能体设计

- **类型**：搜索型智能体（Search Agent）
- **算法**：遗传算法（Genetic Algorithm）
- **编码**：整数数组，前 k 位为英雄索引，后 k 位为站位索引
- **适应度**：与 opponent 池模拟对战的胜率
- **闭环**：perceive → decide → execute → feedback

## 作者

- 单人独立完成
- 软件工程 2026~2027-1 期末大作业
