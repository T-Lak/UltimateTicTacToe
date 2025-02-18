package common;

public enum WinningLine {

    ROW_1(0, 1, 2),
    ROW_2(3, 4, 5),
    ROW_3(6, 7, 8),
    COL_1(0, 3, 6),
    COL_2(1, 4, 7),
    COL_3(2, 5, 8),
    DIAG_1(0, 4, 8),
    DIAG_2(2, 4, 6);

    private final int[] indices;

    WinningLine(int... indices) {
        this.indices = indices;
    }

    public int[] getIndices() {
        return indices;
    }

}
