public class GameUI {
    public void renderBoard(GameBoard board){
        Cell[][]grid = board.getGrid();
        int rows = board.rows;
        int cols = board.cols;
//  遍历输出
        System.out.println("  ");
        for(int j=1; j<=rows; j++){
            System.out.print(j+" ");
        }
        System.out.println();
        for(int i=0; i<rows; i++){
            System.out.print((i+1)+" ");
            for(int j=0; j<cols; j++){
                Cell cell = grid[i][j];
                if(cell.isRevealed()){
                    if(cell.isMine()){
                        System.out.print("* ");
                    }else if(cell.getAroundMineCount()==0){
                        System.out.print("  ");
                    }else {
                        System.out.print(cell.getAroundMineCount()+" ");
                    }
                }else if(cell.isFlagged()){
                    System.out.print("🚩 ");
                }else{
                    System.out.print("■ ");
                }
            }
            System.out.println();
        }
    }
    public void printHelp(){
        System.out.println("=====扫雷=====");
        System.out.println("指令：o 行 列 |翻开   f 行 列 |插旗");
    }
}
