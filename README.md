# 扫雷游戏（Minesweeper）· 控制台版

一个用 Java 实现的终端扫雷游戏：9×9 棋盘、10 颗雷，首击安全，空白区域 BFS 级联展开，支持插旗、彩色数字与整局计时。

## 功能特性

### 基础功能

- **9×9 棋盘，10 颗雷**，格子包含三种状态：未翻开（Covered）、已翻开（Revealed）、插旗（Flagged）。
- **坐标系统**：行、列均从 1 开始（1~9），与界面表头一致。
- **首击安全**：首次翻开后才动态布雷，且首击格必不为雷（落在首击格时重抽）。
- **指令解析**：
  - `o 行 列` 或 `open 行 列`：翻开格子
  - `f 行 列` 或 `flag 行 列`：插旗 / 取消插旗
  - 指令格式错误、坐标非数字时给出提示，不会使程序崩溃。
- **操作保护**：已翻开的格子不能再翻开，也不能插旗。
- **插旗下翻开确认**：翻开插旗格子时会提示，输入 `yes` 确认翻开，输入 `no` 取消。
- **BFS 空白级联展开**：翻开的格子周围雷数为 0 时，自动展开其 8 邻域的未翻开格子，直到遇到数字格。
- **胜负结算**：踩雷立即结束并展示全部地雷位置；所有非雷格全部翻开时获胜。
- **每次操作后渲染棋盘**，并显示行列号。

### 进阶功能

- **ANSI 颜色数字**：1 蓝、2 绿、3 红、4 紫、5 黄、6 青、7 灰、8 红。
- **整局计时**：从开局到结束统计耗时，精确到 0.01 秒。

## 运行环境

- **JDK 14 及以上**（`color.java` 使用了 switch 表达式）。
- 支持 **ANSI 转义序列** 和 **UTF-8** 的终端，例如 Windows Terminal、PowerShell 7、macOS Terminal、Linux 终端。

## 编译与运行

在项目根目录执行：

```bash
javac -encoding UTF-8 -d out src/*.java
java -cp out MinesweeperMain
```

> Windows 旧版 CMD 中如果中文或 emoji 显示异常，先执行 `chcp 65001` 切换到 UTF-8 编码，或改用 Windows Terminal。

## 玩法说明

### 指令

| 指令 | 说明 | 示例 |
| --- | --- | --- |
| `o 行 列` / `open 行 列` | 翻开指定格子 | `o 3 4` |
| `f 行 列` / `flag 行 列` | 插旗 / 取消插旗 | `f 3 4` |

指令需在一行内输入，行列范围为 1~9。

### 棋盘图例

| 符号 | 含义 |
| --- | --- |
| `■` | 未翻开的格子 |
| `🚩` | 已插旗的格子 |
| 空白 | 已翻开且周围没有雷 |
| 彩色数字 `1`~`8` | 已翻开，数字表示周围 8 格的雷数 |
| `💣` | 地雷（踩雷结束后展示全部地雷） |

### 规则

1. 开局棋盘全部覆盖，首次翻开时才会布雷，首击格一定是安全的。
2. 翻开的格子不能再次翻开，也不能插旗；插旗格翻开前需要二次确认。
3. 翻开的格子周围没有雷时，会自动展开相连的空白区域。
4. 踩到雷游戏立即结束；翻开所有非雷格即获胜。
5. 结束后会显示本局耗时。

## 项目结构

```text
Minesweeper/
├── README.md
└── src/
    ├── MinesweeperMain.java   # 程序入口：指令读取与解析、主循环、计时
    ├── GameBoard.java         # 棋盘模型：布雷、邻居雷数计算、BFS 级联、胜负判断
    ├── Cell.java              # 格子实体：雷、翻开、插旗、周围雷数
    ├── GameUI.java            # 控制台渲染：表头、棋盘、帮助信息
    └── color.java             # ANSI 颜色工具：数字到颜色的映射
```

## 核心实现说明

1. **首击安全布雷**：`GameBoard.placeMine(firstR, firstC)` 在玩家第一次有效翻开时随机布雷，跳过首击格与已布雷格，随后调用 `calcAllAroundMine()` 计算每格周围雷数。
2. **邻居雷数计算**：使用 8 方向偏移数组 `dirs`，配合 `inBounds()` 做边界校验。
3. **BFS 级联展开**：`GameBoard.BFS()` 用队列实现。起点入队并翻开；队列中周围雷数为 0 的格子向 8 邻域扩散，只翻开「未翻开、未插旗、非地雷」的邻居，直到队列为空。
4. **胜负判断**：`safeNum = 行数 × 列数 - 雷数`，每翻开一个安全格减一，归零即所有非雷格均已翻开；踩雷时置 `gameOver = true`。
5. **界面渲染与颜色**：`GameUI.renderBoard()` 负责表头与棋盘输出，`color.getColorByCount()` 根据数字返回对应的 ANSI 颜色常量。
6. **计时**：`MinesweeperMain` 用 `System.currentTimeMillis()` 记录开局与结束时间，结束后输出本局耗时。

## 需求对照

| 需求 | 实现情况 | 主要位置 |
| --- | --- | --- |
| 9×9 / 10 雷网格，三种格子状态 | ✅ | `GameBoard`、`Cell` |
| 坐标系统清晰（1~9） | ✅ | `MinesweeperMain`、`GameUI` |
| 首步安全保底 | ✅ | `GameBoard.placeMine()` |
| 指令解析（`o`/`open`、`f`/`flag`） | ✅ | `MinesweeperMain.main()` |
| 已翻开不可重复操作、插旗格翻开提示 | ✅ | `GameBoard.openCell()`、`toggleFlag()` |
| BFS 空白级联展开 | ✅ | `GameBoard.BFS()` |
| 踩雷揭开全部地雷、全部非雷格翻开判胜 | ✅ | `GameBoard.openCell()`、`checkWin()` |
| 行列对齐的棋盘渲染 | ✅ | `GameUI.renderBoard()` |
| ANSI 颜色数字（加分项） | ✅ | `color.java` |
| 游戏耗时统计（加分项） | ✅ | `MinesweeperMain.main()` |
| 自定义难度选择（加分项） | ⬜ 未实现 | 固定为 9×9 / 10 雷，可在 `main()` 中调整 `new GameBoard(9, 9, 10)` |

## 已知限制

- 棋盘规格固定为 9×9、10 颗雷，暂不支持难度选择。
- 需要支持 ANSI 颜色与 UTF-8 的终端；旧版 CMD 下 emoji 宽度和颜色可能显示异常。
- emoji（`🚩`、`💣`）在部分终端中占两个字符宽度，而两位数行列号占三格，极端情况下表格对齐可能有偏差。

## 开发经验总结（踩坑记录）

> 记录本项目开发过程中真实踩过的坑，按「现象 → 原因 → 正确做法」整理，方便以后回顾。

### 1. Scanner 不能混用 `nextLine()` 和 `nextInt()`

- **现象**：输入 `o 3 4` 后指令不生效；或者从第二条指令开始整体错位、抛 `InputMismatchException`。
- **原因**：`nextInt()` 只读数字、不消费行尾的换行符，下一次 `nextLine()` 会先读到空行；同时 `nextLine()` 读的是整行 `"o 3 4"`，不能直接和 `"o"` 比较。
- **正确做法**：统一按行读取，再自行拆分：
  ```java
  String line = sc.nextLine().trim();
  String[] arr = line.split("\\s+");
  int row = Integer.parseInt(arr[1]) - 1;
  int col = Integer.parseInt(arr[2]) - 1;
  ```
- **经验**：一个输入源只用一个 Scanner；解析前先 `trim()`，并检查 `arr.length`。

### 2. IO 要分层：模型类不要自己读输入

- **现象**：`GameBoard` 里 `new Scanner(System.in)` 之后，主循环偶尔会“吞掉”玩家的输入。
- **原因**：多个 Scanner 同时包装 `System.in` 时，各自的缓冲区可能提前读取，输入被别的 Scanner 拿走。
- **正确做法**：`GameBoard` 只负责游戏逻辑；提示与读取放在 `GameUI` / `MinesweeperMain`，确认结果再传回模型（例如先 `clearFlag` 再 `openCell`）。

### 3. 分支里 `break` 只跳出循环，循环外的公共代码仍会执行

- **现象**：插旗格选 `no` 后仍然被掀开；选 `yes` 时格子被翻开两次、安全格计数多减一次。
- **原因**：`if` 块里的 `break` 只跳出 `while`，而 `while` 块外还有一句公共的 `BFS(...)`，于是两个分支都会执行到它。
- **正确做法**：把“确认”和“执行”拆开，执行只调用一次：
  ```java
  boolean shouldOpen = true;
  if (board.isFlagged(r, c)) {
      shouldOpen = ui.confirmOpenFlag(sc);
      if (shouldOpen) board.clearFlag(r, c);
  }
  if (shouldOpen) board.openCell(r, c);
  ```
- **经验**：看到 `break` / `continue` 时，顺着控制流把块外的代码再走一遍。

### 4. 修改或删除方法后，同步检查所有调用点

- **现象**：删掉 `revealAllMines()` 后 `openCell()` 仍在调用，编译报“找不到符号”；删掉 `openCell()` 末尾的 `BFS(...)` 后，普通格子翻开没有任何反应。
- **经验**：每轮改动后先 `javac` 编译，再跑一遍最小流程：翻开普通格、插旗、踩雷、胜利。不要只靠“看代码觉得对”。

### 5. 判断顺序本身就是游戏规则

- **现象**：插了旗的雷被点开时直接爆炸，没有先走确认流程。
- **原因**：`if (cell.isMine())` 排在了 `isRevealed()` / `isFlagged()` 之前。
- **正确做法**：按业务优先级排列：已翻开 → 已插旗 → 首次布雷 → 是否踩雷 → BFS 展开。
- **经验**：多个状态判断并列时，先想清楚“哪个条件应该先拦截”。

### 6. 边界条件：胜利判定与参数校验

- **现象**：胜利条件写成 `safeNum < 0`，导致永远赢不了（安全格全部翻开时 `safeNum` 恰好等于 0）；雷数配置过大时，`placeMine` 的随机重抽会死循环。
- **正确做法**：胜利条件写 `safeNum <= 0`；构造函数校验 `rows > 0`、`cols > 0`、`0 <= mineNum < rows * cols`。
- **经验**：凡是涉及 `-1`、`0`、`<` / `<=` 的地方，单独列真值表验证。

### 7. 状态与显示分离

- **现象**：踩雷后直接在 `GameBoard` 里复制了一份棋盘打印代码，既重复又容易和 `GameUI` 不一致。
- **正确做法**：模型只改状态（`revealAllMines()` 把所有雷标记为已翻开），主循环结束后统一调用 `ui.renderBoard(board)`。

### 8. 越界与异常输入要有反馈

- 坐标越界、坐标非数字、指令格式错误都应给出提示，而不是静默返回。
- `Scanner` 读到输入流结尾（EOF）时要能正常退出，避免 `hasNextLine()` 为 `false` 后反复读取导致死循环。

## Queue 使用说明（BFS 基础）

本项目的“空白格自动展开”使用 BFS 实现，核心数据结构就是 `Queue`。

### 1. 声明与创建

```java
import java.util.LinkedList;
import java.util.Queue;

Queue<int[]> queue = new LinkedList<>();
```

- `Queue` 是接口，`LinkedList` 是常用实现；只当队列用时 `ArrayDeque` 通常性能更好，也是官方推荐。
- 这里用 `int[]{r, c}` 保存坐标（数组是对象，可以作为泛型元素），也可以用自定义类或 `record`。

### 2. 常用方法

| 方法 | 主要用途 | 返回值 | 失败时（队列为空 / 有容量限制） |
| --- | --- | --- | --- |
| `add(e)` | 把元素放到队尾 | `true` | 队列有容量限制且已满时抛 `IllegalStateException` |
| `offer(e)` | 把元素放到队尾（推荐） | `true` / `false` | 入队失败时返回 `false`，不抛异常 |
| `poll()` | 取出队头并删除，BFS 主循环靠它逐个处理元素 | 队头元素 | 队列为空时返回 `null` |
| `remove()` | 取出队头并删除 | 队头元素 | 队列为空时抛 `NoSuchElementException` |
| `peek()` | 只查看队头、不删除，用于确认“下一个要处理的是谁” | 队头元素 | 队列为空时返回 `null` |
| `element()` | 只查看队头、不删除 | 队头元素 | 队列为空时抛 `NoSuchElementException` |
| `isEmpty()` | 判断队列是否为空，通常直接作为 BFS 的循环条件 | `boolean` | — |
| `size()` | 当前元素个数（会随入队/出队变化，不能当固定长度用） | `int` | — |

可以按“失败时的行为”把它们分成三组，每组两个，功能相同、容错方式不同：

| 操作 | 失败时抛异常 | 失败时返回特殊值（推荐） |
| --- | --- | --- |
| 入队 | `add(e)` | `offer(e)`，失败返回 `false` |
| 出队 | `remove()` | `poll()`，空队列返回 `null` |
| 查看队头 | `element()` | `peek()`，空队列返回 `null` |

简短示例：

```java
Queue<int[]> queue = new LinkedList<>();

queue.offer(new int[]{0, 0});   // 入队
queue.offer(new int[]{0, 1});

int[] first = queue.peek();     // 查看队头 {0, 0}，元素不出队
int size = queue.size();        // 当前个数：2

while (!queue.isEmpty()) {      // 队列非空就继续
    int[] cur = queue.poll();   // 取出队头并删除：先 {0, 0}，再 {0, 1}
    // 处理 cur ...
}
```

> 注意：`LinkedList` 允许存 `null`，`ArrayDeque` 不允许；用 `poll()` 拿到结果后要判空。

### 3. 本项目中的 BFS 写法

```java
import java.util.LinkedList;
import java.util.Queue;

// 起点入队
Queue<int[]> queue = new LinkedList<>();
queue.add(new int[]{r, c});
cell.setRevealed();   // 入队时立即标记“已访问”
safeNum--;

while (!queue.isEmpty()) {
    int[] cur = queue.poll();     // 取出队头
    int cr = cur[0];
    int cc = cur[1];

    // 数字格只翻开，不再向外扩散
    if (grid[cr][cc].getAroundMineCount() != 0) {
        continue;
    }

    // 向 8 个邻居扩散
    for (int[] dir : dirs) {
        int nr = cr + dir[0];
        int nc = cc + dir[1];
        if (!inBounds(nr, nc)) {
            continue;
        }

        Cell next = grid[nr][nc];
        if (!next.isRevealed() && !next.isFlagged() && !next.isMine()) {
            next.setRevealed();       // 入队前标记，避免重复入队
            safeNum--;
            queue.add(new int[]{nr, nc});
        }
    }
}
```

### 4. 用 Queue 做 BFS 的注意点

1. **入队时标记已访问**：否则同一个格子会被多个邻居重复入队。本项目用 `isRevealed()` 充当 visited 标记，起点在入队前就已标记。
2. **先判空再出队**：`poll()` 在空队列返回 `null`，直接 `cur[0]` 会触发空指针；用 `while (!queue.isEmpty())` 包住。
3. **不要用 `for` 遍历队列**：BFS 过程中队列长度会变化，应该用 `while + poll()` 逐个处理。
4. **扩散条件写全**：本项目要求“未翻开 + 未插旗 + 不是雷”三个条件同时满足才入队，漏掉任何一个都会出错。
5. **起点也走同一套流程**：起点入队后同样会被 `while` 取出；如果起点是数字格，会在 `continue` 处停止扩散。
6. **复杂度**：每个格子最多入队一次，`offer` / `poll` 均为 O(1)，整体是 O(行 × 列)。

### 5. BFS 与 DFS 怎么选

- **BFS（队列）**：按层向外扩散，适合“从一点一圈圈展开”，本项目使用它。
- **DFS（栈 / 递归）**：一条路走到底，代码更短，但大棋盘递归可能栈溢出。

## 后续可扩展方向

- 初级 / 中级 / 高级难度选择。
- 显示剩余雷数、最佳成绩记录。
- 更完善的对齐处理（按终端显示宽度计算列宽）。
