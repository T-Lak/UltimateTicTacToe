package api;

import board.GlobalBoard;
import common.Move;
import common.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Serializer {

    /**
     * Serializes the current state of the GlobalBoard into a structured map.
     * The serialized data includes:
     * - The board state (converted into a human-readable format).
     * - The decided state of each local board (if applicable).
     * - The last move made in the game.
     *
     * @param board The GlobalBoard instance to serialize.
     * @return A map containing the serialized board data.
     */
    public static Map<String, Object> serialize(GlobalBoard board) {
        Map<String, Object> serializedData = new HashMap<>();

        var moveHistory = board.getMoveHistory();

        serializedData.put("board", transformBoard(board.getBoard()));
        serializedData.put("decidedBoards", transformDecidedBoards(board.getDecidedBoards()));
        serializedData.put("lastMove",
                transformLastMove(
                    moveHistory.get(moveHistory.size() - 1),
                    moveHistory.size())
        );

        return serializedData;
    }

    /**
     * Converts the board state from integer representation to a list of lists
     * containing human-readable symbols ("X", "O", or "").
     *
     * @param board A 2D integer array representing the board.
     * @return A list of lists containing board symbols.
     */
    public static List<List<String>> transformBoard(int[][] board) {
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

    /**
     * Converts the last move into a structured map containing:
     * - The move index.
     * - The player who made the move ("X" for human, "O" for AI).
     * - The board index where the move was made.
     * - The exact position within the local board.
     *
     * @param move      The last move made in the game.
     * @param moveIndex The index of the move in the history.
     * @return A map containing details about the last move, or null if no move exists.
     */
    private static Map<String, Object> transformLastMove(Move move, int moveIndex) {
        if (move == null) return null;

        var player = move.player() == Player.HUMAN ? "X" : "O";

        return Map.of(
                "index", moveIndex,
                "player", player,
                "board", move.boardIndex(),
                "position", move.position().getIndex()
        );
    }

    /**
     * Transforms the decided boards array into a map that associates each decided
     * local board index with its corresponding winner symbol ("X" for human, "O" for AI).
     * Boards that are not yet decided are excluded.
     *
     * @param decidedBoards An array where each index represents a local board and its decided state.
     * @return A map of decided boards with their respective winners.
     */
    private static Map<Integer, String> transformDecidedBoards(int[] decidedBoards) {
        Map<Integer, String> decidedBoardsMap = new HashMap<>();

        for (int i = 0; i < decidedBoards.length; i++) {
            if (decidedBoards[i] != -1) {
                decidedBoardsMap.put(i, getCorrespondingChar(decidedBoards[i]));
            }
        }

        return decidedBoardsMap;
    }

    /**
     * Maps an integer representation of a move to its corresponding symbol.
     * 0 -> "" (empty)
     * 1 -> "X" (human player)
     * 2 -> "O" (AI player)
     * Any other value -> "E" (error state)
     *
     * @param i The integer value representing a cell.
     * @return The corresponding character.
     */
    private static String getCorrespondingChar(int i) {
        return switch (i) {
            case 0 -> "";
            case 1 -> "X";
            case 2 -> "O";
            default -> "E";
        };
    }

}
