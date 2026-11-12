import java.util.Scanner;

public class MinesweeperMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GameUI ui = new GameUI();
        GameBoard board=new GameBoard(9,9,10);

        ui.printHelp();
        while (!board.isGameOver()){
            ui.renderBoard(board);
            System.out.print("输入指令：");
            String op=sc.nextLine();
            int r=sc.nextInt()-1;
            int c=sc.nextInt()-1;
            if(op.equals("o")){
                board.openCell(r,c);
            }else if(op.equals("f")){
                board.toggleFlag(r,c);
            }

            if(board.checkWin()){
                System.out.println("🎉🎉恭喜胜利！🎉🎉");
                break;
            }
        }
        ui.renderBoard(board);
        sc.close();
    }
}
