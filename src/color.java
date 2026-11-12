public class color {
    // ANSI 颜色
    public static final String RESET = "\u001B[0m";
    public static final String BLUE = "\u001B[34m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String PURPLE = "\u001B[35m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String GRAY = "\u001B[90m";

    // 根据周围雷数拿到颜色
    public static String getColorByCount(int cnt) {
        return switch (cnt) {
            case 1 -> BLUE;
            case 2 -> GREEN;
            case 3 -> RED;
            case 4 -> PURPLE;
            case 5 -> YELLOW;
            case 6 -> CYAN;
            case 7 -> GRAY;
            case 8 -> RED;
            default -> RESET;
        };
    }

}
