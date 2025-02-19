package utilities;

public class TestUtilities {

    public static void printBoard(int[][] globalBoard) {
        System.err.println("+-------+-------+-------+");

        for (int i = 0; i < 3; i++) { // Iterate over main board rows
            for (int j = 0; j < 3; j++) { // Iterate over local board rows
                System.err.print("| ");

                for (int k = 0; k < 3; k++) { // Iterate over main board columns
                    int idx = 3 * i + k; // Convert (i, k) to 1D index
                    int[] board = globalBoard[idx];

                    for (int l = 0; l < 3; l++) { // Iterate over local board columns
                        System.err.print(getCorrespondingChar(board[3 * j + l]) + " ");
                    }
                    System.err.print("| ");
                }

                System.err.println();
            }
            System.err.println("+-------+-------+-------+");
        }
    }

    private static String getCorrespondingChar(int i) {
        return switch (i) {
            case 0 -> "-";
            case 1 -> "X";
            case 2 -> "O";
            default -> "error";
        };
    }

}
