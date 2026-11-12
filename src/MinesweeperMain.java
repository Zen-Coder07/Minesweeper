import java.util.Scanner;

public class MinesweeperMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GameUI ui = new GameUI();
        GameBoard board=new GameBoard(9,9,10);
        ui.printHelp();
        long start=System.currentTimeMillis();
        while (!board.isGameOver()){
            ui.renderBoard(board);
            System.out.print("输入指令：");
            try {
                String line = sc.nextLine();
                String[] arr = line.split("\\s+");
                if(arr.length==3) {
                    String op = arr[0];
                    int r = Integer.parseInt(arr[1]) - 1;
                    int c = Integer.parseInt(arr[2]) - 1;
                    if (op.equals("o") || op.equals("open")) {
                        board.openCell(r, c);
                    } else if (op.equals("f") || op.equals("flag")) {
                        board.toggleFlag(r, c);
                    }
                    else{
                        throw new Exception("请输入o/open或f/flag");
                    }
                }
                else{
                    throw new Exception("格式有误");
                }
            }catch (NumberFormatException e){
                System.out.println("行列必须为数字！");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            if(board.checkWin()){
                System.out.println("🎉🎉恭喜胜利！🎉🎉");
                break;
            }
        }
        long end = System.currentTimeMillis();
        double cost = (end - start) / 1000.0;
        System.out.printf("本局耗时：%.2f 秒\n", cost);

        sc.close();
    }
}
