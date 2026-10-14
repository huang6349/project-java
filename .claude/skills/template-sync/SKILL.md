---
name: template-sync
description: |
  将模板项目（project-java）的最新功能合并到基于它构建的子项目。

  适用场景：
  - 子项目需要跟进模板的框架层更新
  - 模板新增了通用能力（SDK 接入、基础设施、工具类），想同步到子项目
  - 子项目与模板长期分叉，需要建立可持续的同步机制

  触发词：同步模板、拉取模板更新、合并上游、上游同步、template sync

  ⚠️ 本技能在「子项目」中运行，不在模板仓库中运行。
---

# 模板同步技能

把上游模板仓库（`project-java`）的新功能合并到当前子项目。

**核心机制**：upstream remote + `git cherry-pick` 逐个应用。git 原生三方合并，冲突可交互解决，全程可 `--abort` 回滚。托管平台无关——模板在 GitHub、子项目在 Gitea/Gitee 均不影响，remote 只是"一个能取到对象的地址"。

## 核心概念

| 概念 | 说明 |
|---|---|
| 模板仓库 | `project-java`，本技能的来源 |
| 子项目 | 当前工作目录，基于模板构建 |
| `upstream` | 子项目中指向模板的 git remote 名 |
| `.template-sync.json` | 子项目根目录，记录同步状态（需提交到 git，团队共享） |
| `lastSync` | 水印。上次同步时的模板 HEAD，新功能 = `git log lastSync..upstream/{branch}` |
| `skipPaths` | 子项目自有文件，冲突时自动跳过。**默认空**，由用户渐进式声明 |

## 状态文件

`.template-sync.json`（子项目根目录）：

```json
{
  "template": "git@gitea.internal:ops/project-java.git",
  "branch": "master",
  "basedOn": "e6e7ab6",
  "lastSync": "a5198ba",
  "lastSyncAt": "2026-10-14T10:30:00+08:00",
  "syncedCommits": ["a5198ba", "3fce737"],
  "skipPaths": []
}
```

| 字段 | 含义 |
|---|---|
| `template` | 模板仓库地址。可指向 GitHub / Gitea / Gitee 镜像，不写死 |
| `basedOn` | 派生点。首次对齐时反查写入，之后不变（调试用） |
| `lastSync` | 水印，每次同步结束置为模板 HEAD |
| `syncedCommits` | 实际应用过的模板 commit（仅展示用） |
| `skipPaths` | 子项目自有文件，冲突时自动跳过 |

**跳过的 commit 不记录**——跳过的功能过阵子可能想合，重复询问是合理行为。

---

## 步骤 1：前置检测

```bash
git status --porcelain
```

**有输出 → 中止。** 工作区有未提交改动时不做任何合并操作，要求用户先 commit 或 stash。理由：对齐/合并失败时无法干净回滚。

检查 `.template-sync.json`：

- **存在** → 读取，进入步骤 2
- **不存在** → 对应「子项目尚无 `upstream` remote」，走**初始化引导**

### 初始化引导

**逐条询问，一次只问一个问题：**

| 顺序 | 问题 | 默认值 |
|:--:|---|---|
| Q1 | 模板仓库地址？内网连不上 GitHub 可填 Gitea/Gitee 镜像地址 | `git@github.com:huang6349/project-java.git` |
| Q2 | 分支名？ | `master` |

```bash
# 已存在 upstream 则改用 git remote set-url
git remote add upstream {地址}
git fetch upstream
```

**技能自举**：若子项目 `.claude/skills/template-sync/` 不存在（子项目派生早于本技能），先取回来：

```bash
git fetch upstream
git checkout upstream/{branch} -- .claude/skills/template-sync
```

---

## 步骤 2：历史对齐

```bash
git merge-base HEAD upstream/{branch}
```

| 输出 | 判定 | 动作 |
|---|---|---|
| 返回 SHA（exit 0） | 有共同祖先 | 跳到步骤 3 |
| 无输出（exit 1） | **无共同历史** | 执行对齐流程 |

### 对齐流程

子项目若为「拷贝代码 + 自行 `git init`」，与模板无共同祖先，直接合并会把每个双方都存在的文件都判成冲突。先建立 merge base。

**① 反查派生点**

取框架层样本文件（子项目大概率未改动），按顺序取前 3 个在子项目中**实际存在**的：

```
project-framework/src/main/java/org/myframework/core/helper/FetchLoadHelper.java
project-framework/src/main/java/org/myframework/core/config/FrameworkFlyway.java
project-framework/src/main/java/org/myframework/base/domain/SuperEntity.java
project-framework/src/main/java/org/myframework/base/response/ApiResponse.java
project-framework/src/main/java/org/myframework/base/web/BaseController.java
```

**必须写完整路径。** `SuperEntity.java`、`BaseController.java`、`Constants.java` 在多个模块下同名，用 basename 或通配符会取到错误的文件。

优先选**变更次数多**的文件——判别力强。只被提交过一次的文件只能把派生点锁定到「它被引入的那一刻」，区间很宽。

对每个样本，取其内容与子项目一致的**最新** commit（即该文件给出的**下界**）：

```bash
LOCAL=$(git hash-object {样本文件})
git log --format=%H --date=short -- upstream/{branch} -- {样本文件} | while read c; do
  [ "$(git rev-parse -q --verify $c:{样本文件})" = "$LOCAL" ] && echo "$c"
done | head -1
```

**关键**：`git log -- {path}` 只列出**触碰过该文件**的 commit，未触碰的中间 commit 不会出现，所以每个样本给出的实际是派生点的**下界**。因此——

> **派生点候选 = 各样本下界中「最晚」的那个**（取下界的最大值）

理由：子项目派生点必然不早于任何一个样本给出的下界；取最大值即最紧下界。

向用户展示候选及相邻的 2-3 个 commit（附日期与 message）供确认——用户若知道实际派生时间，可以选更晚的一个；或允许直接输入 SHA。

**② 空操作 merge**

```bash
git merge {派生点SHA} --allow-unrelated-histories -X ours -m "chore: 对齐模板历史"
```

`-X ours` 是核心：冲突时**全部保留子项目现有内容**，这次 merge 应当**零文件改动**，纯粹给 git 记下祖先关系。

**③ 强制验证（不可跳过）**

```bash
git show --stat --format="" HEAD
```

- **无文件变更** → 对齐成功，继续
- **有文件变更** → 派生点选错了，立即回退重来：
  ```bash
  git reset --hard ORIG_HEAD
  ```

**④** 写入 `basedOn` 字段到 `.template-sync.json`。

> 补充：`git cherry-pick` 基于每个 commit 自身的 parent 做三方合并，**不使用 merge-base**，因此派生点精度对 cherry-pick 影响很小。对齐的主要价值是恢复 `git merge upstream/{branch}` 这条路，以及让 `git log --graph` 的溯源关系正确。

---

## 步骤 3：变更分析

```bash
git log --format='%h %ad %s' --date=short --no-merges {lastSync}..upstream/{branch}
```

`lastSync` 为空时退化用 `basedOn`。按 conventional commit type 分组展示：

```
【feat】 【fix】 【refactor】 【chore】 【test】 【style】 【docs】
```

**文件级预警**——预测哪些文件会冲突。对模板改动的每个文件，比较「模板改之前的内容」与「子项目当前内容」：

```bash
# 模板中该 commit 改动之前的版本
git rev-parse {sha}^:{文件}
# 子项目当前版本
git hash-object {文件}
```

| 比较结果 | 标记 | 含义 |
|:--:|---|---|
| `git rev-parse` 失败 | 🟢 | 模板新增文件，子项目无此文件，安全 |
| `git hash-object` 失败 | 🟢 | 子项目无此文件，安全 |
| 两个 hash 相等 | 🟡 | 子项目未改动，cherry-pick 必然干净 |
| 两个 hash 不等 | 🔴 | 子项目已分歧，可能冲突，**逐个列出文件名** |
| 命中 `skipPaths` | ⚫ | 永不覆盖：冲突时保留子项目，干净应用后还原（见步骤 5） |

这个判断与 `git cherry-pick` 内部的三方合并**同源**（都以 `{sha}^` 为 base）：子项目相对 base 无改动 ⇒ 应用必然干净。

**为什么不用 `git diff {lastSync} -- {文件}`**：有输出既可能是子项目改的，也可能是模板改的——而后者正是本次要同步的目标，会把所有待同步文件都误判为「已改动」。

**注意**：这是**预警**，不是判定。多个 commit 依次应用时，后一个 commit 的 base 受前一个影响，以逐个 cherry-pick 的实际结果为准。

**天然分歧文件**（几乎必然冲突，需预提醒）：
`pom.xml`、`application-*.yml`、`CLAUDE.md`、`README.md`、`.gitignore`

---

## 步骤 4：勾选功能

按分组列出变更 commit，让用户勾选要合并的功能。

**依赖预警**：若勾选的 commit 与未勾选的 commit 修改同一批文件，提示：

> ⚠️ 你勾选的 `{sha}` 与未勾选的 `{sha2}` 都修改了 `{文件}`，取消后者可能导致编译失败。是否仍要继续？

让用户决定，不做拦截。默认全选或全不选由用户偏好决定，可提供「全选 feat+fix」这类快捷选项。

---

## 步骤 5：逐个应用

按**时间正序**逐个 cherry-pick 勾选的 commit：

```bash
git cherry-pick {sha}
```

- **成功** → 下一个
- **冲突** → 暂停，进入冲突交互流程

### 冲突交互流程

```bash
# 列出所有冲突文件
git diff --name-only --diff-filter=U
```

对每个冲突文件，先展示**模板侧改动意图**：

```bash
# 查看这个 commit 想改什么
git diff {sha}^..{sha} -- {文件}
```

再让用户选择：

| 选项 | 动作 |
|:--:|---|
| `[t]` 采用模板 | `git checkout {sha} -- {文件}` |
| `[k]` 保留子项目 | `git checkout HEAD -- {文件}` |
| `[m]` 手工合并 | 保留 `<<<<<<<` 标记，用户编辑 |
| `[s]` 跳过此次改动 | 同 `[k]`，并**提议加入 `skipPaths`** |
| `[a]` 全部采用模板 | 剩余所有冲突文件执行 `[t]` |
| `[q]` 中止 | `git cherry-pick --abort` |

> **⛔ 禁止使用 `--ours` / `--theirs`。** cherry-pick 场景下这两个语义与 `merge` **相反**（`--ours` 指当前分支 HEAD，`--theirs` 指被 pick 的 commit），是经典踩坑点。一律用**显式 SHA**。

### skipPaths 的双重保护

`skipPaths` 是「**该文件绝不被模板覆盖**」的强保证，两种场景都必须生效：

**① 冲突时** —— 自动选 `[k] 保留子项目`，不询问用户。

**② 干净应用时（真正的风险点）** —— cherry-pick 会**静默写入**模板版本，不产生任何冲突。例如模板调整了 `application-dev.yml` 的结构，而子项目那里是自家的库地址，直接应用会覆盖掉。必须还原：

```bash
# HEAD~1 是 cherry-pick 之前的提交，即子项目自己的版本
git checkout HEAD~1 -- {skipPath 路径}
git commit --amend --no-edit
```

仅当该 commit 确实改动了 skipPaths 命中的路径时才执行。

### 何时加入 skipPaths

当某文件反复冲突，或用户选了 `[s]`，提议：

> `{文件}` 每次同步都会被动到，是否加入 `skipPaths`？加入后该文件将**永不**被模板覆盖。

用户同意则写入 `.template-sync.json` 的 `skipPaths`。这是**渐进式配置**——不预先猜测哪些文件该排除。

### 完成本 commit

所有冲突解决后：

```bash
git add {已解决的文件}
GIT_EDITOR=true git cherry-pick --continue
```

用 `GIT_EDITOR=true` 避免编辑器阻塞。

---

## 步骤 6：收尾

**① 更新 `.template-sync.json`**（追加，不覆盖）：

- `lastSync` = `git rev-parse upstream/{branch}`
- `lastSyncAt` = 当前时间
- `syncedCommits` 追加本次应用的 commit
- `skipPaths` 若有新增

**② 编译验证**（询问用户是否执行）：

```bash
./mvnw -q compile
```

**③ 报告结果**：应用了哪些功能、跳过了哪些、改动文件数、冲突文件数。

**④ 提示**：使用 `/commit-msg` 生成结构化提交信息。**技能本身不执行 `git commit`**。

---

## 异常处理

| 场景 | 处理 |
|---|---|
| 模板 remote 不可达 | 报错并提示改用 Gitea/Gitee 镜像地址 |
| 工作区有未提交改动 | 步骤 1 即中止，要求先 commit 或 stash |
| 反查无任何样本命中 | 退化为用户手输 SHA；仍无则**拒绝同步**（宁可不同步，也不做错误的 `-X ours` 对齐） |
| 对齐 merge 后文件有变更 | `git reset --hard ORIG_HEAD` 回退，重新选派生点 |
| cherry-pick 中途失败 | `git cherry-pick --abort` 整体回滚，或 `--skip` 跳过该 commit |
| `upstream` remote 已存在但地址不同 | `git remote set-url upstream {新地址}` |
| 子项目无 `.claude/skills/template-sync/` | 技能自举（见步骤 1） |

## 边界与不做的事

- ❌ **不执行 `git commit`** —— 只改本地工作区，提交由用户经 `/commit-msg` 完成
- ❌ **不执行 `git push`**
- ❌ **不修复同步后的编译/测试失败** —— 那是子项目自身的适配工作，属于用户决策范围
- ❌ **不做预设路径排除** —— 默认**全量参与同步**（含 `modules/**`、`project-test/**`、`src/test/**`），排除项由 `skipPaths` 渐进式声明
- ❌ **不修改子项目业务代码的语义** —— 冲突时一律交给用户决定
