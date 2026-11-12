public class Cell {
    private boolean isMine;//是否为雷
    private boolean isRevealed;//是否被掀开
    private boolean isFlagged;//是否插旗
    private int aroundMineCount;//周围雷的个数
    public Cell(){
        isMine = false;
        isRevealed = false;
        isFlagged = false;
        aroundMineCount = 0;
    }
    public boolean isMine() {
        return isMine;
    }
    public boolean isRevealed() {
        return isRevealed;
    }
    public boolean isFlagged() {
        return isFlagged;
    }
    public int getAroundMineCount() {
        return aroundMineCount;
    }
    public void setMine(boolean mine) {
        isMine = mine ;
    }
    public void setRevealed() {
        isRevealed = true;
    }
    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }
    public void setAroundMineCount(int aroundMineCount) {
        this.aroundMineCount = aroundMineCount;
    }
}
