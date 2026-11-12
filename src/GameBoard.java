import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GameBoard {
    Cell[][] grid;
    int rows;
    int cols;
    boolean isFirst;
    boolean gameOver;
    int mineNum;
    int safeNum;
    private final int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0},{1,1},{-1,-1},{1,-1},{-1,1}};//8方向偏移，用于查雷数
    public GameBoard(int rows, int cols,int mineNum) {
        this.rows = rows;
        this.cols = cols;
        this.mineNum = mineNum;
        this.isFirst = true;
        this.gameOver = false;
        this.safeNum=rows*cols-mineNum;

        //初始化网格
        grid = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    //首次后布雷
    public void placeMine(int firstR, int firstC) {
        Random rand = new Random();
        int placed=0;
        while (placed<mineNum) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if ((r==firstR && c==firstC)||grid[r][c].isMine()){
                continue;
            }
            grid[r][c].setMine(true);
            placed++;
        }
        calcAllAroundMine();//算雷数调用
    }
    //算雷数
    public void calcAllAroundMine(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j].isMine()) {continue;}
                int cnt=0;
                for (int[] dir : dirs) {
                    int nr = i + dir[0];
                    int nc = j + dir[1];
                    if (inBounds(nr, nc) && grid[nr][nc].isMine()) {cnt++;}
                }
                grid[i][j].setAroundMineCount(cnt);
            }
        }
    }
    //算范围
    private boolean inBounds(int r, int c){
        return r>=0 && r<rows && c>=0 && c<cols;
    }

    //翻格子
    public void openCell(int r, int c){
        if(gameOver)return;
        if(!inBounds(r,c))return;
        Cell cell = grid[r][c];
        if(isFirst){
            placeMine(r,c);
            isFirst = false;
        }
        if(cell.isMine()){
            gameOver=true;
            System.out.println("Bong! 踩雷了！游戏结束");
            return;
        }
        if (cell.isRevealed()||cell.isFlagged()){
            System.out.println("该格子无法被翻开");
            return;
        }
        //BFS级联展开,用Queue

//        假设点击一个0号格子：
//
//        1.起点坐标入队
//
//        2.while循环取出队列里的坐标
//
//        3.如果格子不是0，直接跳过，不扩散
//
//        4.如果是0，找它8个邻居
//
//        5.合法、未翻开、无旗、不是雷 → 翻开，计数减一，把邻居丢进队列
//
//        6.下一轮while就处理刚刚丢进去的邻居
//
//        7.直到队列全部取空，大片展开结束
        Queue<int []> queue = new LinkedList<>();
        queue.add(new int[]{r,c});
        cell.setRevealed();
        safeNum--;

        while (!queue.isEmpty()){
            int[] cur = queue.poll();
            int cr = cur[0];
            int cc = cur[1];
            Cell currCell = grid[cr][cc];
            if(currCell.getAroundMineCount()!=0){
                continue;
            }
            for(int[] dir : dirs){
                int nr = cr + dir[0];
                int nc = cc + dir[1];
                if(inBounds(nr, nc)){
                    Cell neighborCell = grid[nr][nc];
                    if(!neighborCell.isRevealed()&&!neighborCell.isFlagged()&&!neighborCell.isMine()){
                        neighborCell.setRevealed();
                        safeNum--;
                        queue.add(new int[]{nr,nc});
                    }
                }
            }
        }
    }

    //切换旗帜

    public void toggleFlag(int r, int c){
        if(gameOver)return;
        if(!inBounds(r,c))return;
        Cell cell = grid[r][c];
        if(cell.isRevealed()){
            System.out.println("已翻开的格子不能插旗");
            return;
        }
        cell.setFlagged(!cell.isFlagged());
    }
    //检查是否胜利
    public boolean checkWin(){
        return safeNum<=0;
    }
    //是否输掉游戏
    public boolean isGameOver(){
        return gameOver;
    }
    public Cell[][] getGrid(){
        return grid;
    }
    public int getRows(){
        return rows;
    }
    public int getCols(){
        return cols;
    }
}
