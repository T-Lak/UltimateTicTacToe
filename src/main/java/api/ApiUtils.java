package api;

import java.util.ArrayList;
import java.util.List;

public class ApiUtils {

    public static List<List<String>> transformBoardToList(int[][] board) {
        var transformedBoard = new ArrayList<List<String>>();

        for (var localBoard : board) {
            List<String> boardList = new ArrayList<>();

            for (var cellValue : localBoard) {
                boardList.add(getCorrespondingChar(cellValue));
            }

            transformedBoard.add(boardList);
        }

        return transformedBoard;
    }

    private static String getCorrespondingChar(int i) {
        return switch (i) {
            case 0 -> "";
            case 1 -> "X";
            case 2 -> "O";
            default -> "E";
        };
    }

}
