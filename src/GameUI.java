import java.util.Scanner;

public class GameUI {

    public void renderBoard(GameBoard board){
        Cell[][]grid = board.getGrid();
        int rows = board.rows;
        int cols = board.cols;
//  遍历输出

        System.out.print("  ");
        for (int j = 1; j <= cols; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
        for (int i = 0; i < rows; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < cols; j++) {
                Cell cell = grid[i][j];
                if (cell.isRevealed()) {
                    if (cell.isMine()) {
                        System.out.print("💣");
                    } else if (cell.getAroundMineCount() == 0) {
                        System.out.print("  ");
                    } else {
                        int num = cell.getAroundMineCount();
                        String Color = color.getColorByCount(num);
                        System.out.print(Color + num + color.RESET + " ");
                    }
                } else if (cell.isFlagged()) {
                    System.out.print("🚩");
                } else {
                    System.out.print("■ ");
                }
            }
            System.out.println();
        }
    }

    //翻开插旗格子前的二次确认
    public boolean confirmOpenFlag(Scanner sc){
        System.out.println("当前格子为旗子，是否要掀开 yes/no");
        while (true){
            String choice = sc.nextLine().trim();
            if (choice.equalsIgnoreCase("yes")) {
                return true;
            }
            if (choice.equalsIgnoreCase("no")) {
                return false;
            }
            System.out.println("请输入 yes 或 no");
        }
    }

    public void printHelp(){
        System.out.println("=====扫雷=====");
        System.out.println("指令：o 行 列 |翻开   f 行 列 |插旗");
    }
}
