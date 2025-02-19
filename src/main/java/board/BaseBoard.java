package board;

import common.Move;
import common.Player;
import common.Status;
import common.WinningLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class BaseBoard {

    private final int[][] board;
    private List<Integer> boardsInProgress;
    private final int[] decidedBoards;
    private final List<Move> moveHistory;
    private Status status;

    /**
     * Default constructor initializing an empty board with all boards in progress.
     */
    public BaseBoard() {
        board = new int[9][9];
        boardsInProgress = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
        decidedBoards = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1};
        moveHistory = new ArrayList<>();
        status = Status.IN_PROGRESS;
    }

    /**
     * Copy constructor for creating a deep copy of another board.
     *
     * @param other The board to copy.
     */
    public BaseBoard(BaseBoard other) {
        board = copyBoard(other.getBoard());
        boardsInProgress = new ArrayList<>(other.getBoardsInProgress());
        decidedBoards = Arrays.copyOf(other.getDecidedBoards(), 9);
        moveHistory = new ArrayList<>(other.getMoveHistory());
        status = other.getStatus();
    }

    /**
     * Performs a move on the board, updating the game state accordingly.
     * Throws an exception if the move is made by the wrong player.
     *
     * @param move The move to be performed.
     */
    public void performMove(Move move) {
        var boardIndex = move.boardIndex();
        var position = move.position();
        var currentPlayer = move.player();
        var lastPlayer = getLastPlayer();

        if (lastPlayer != null && currentPlayer == lastPlayer)
            throw new IllegalArgumentException(
                    String.format("Player should be %s, but is %s", move.getOpponent(), currentPlayer)
            );

        board[boardIndex][position.getIndex()] = currentPlayer.getId();

        updateBoard(move, boardIndex, currentPlayer);
    }

    /** Returns the last move made in the game, or null if no moves have been made. */
    public Move getLastMove() {
        return moveHistory.isEmpty() ? null : moveHistory.get(moveHistory.size() - 1);
    }

    /** Returns the current global status of the game. */
    public Status getStatus() {
        return status;
    }

    /** Returns the array indicating which local boards have been decided. */
    public int[] getDecidedBoards() {
        return decidedBoards;
    }

    /** Returns the history of all moves made in the game. */
    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    /** Returns the current state of the board. */
    public int[][] getBoard() {
        return board;
    }

    /** Returns the list of currently playable local boards. */
    protected List<Integer> getBoardsInProgress() {
        return boardsInProgress;
    }

    /** Returns the player who made the last move, or null if no moves have been made. */
    protected Player getLastPlayer() {
        return moveHistory.isEmpty() ? null : moveHistory.get(moveHistory.size() - 1).player();
    }

    /** Returns the local board at the specified index. */
    protected int[] getBoardAt(int index) {
        return board[index];
    }

    /**
     * Determines whether a random move continuation is possible.
     * This happens when the next board to play on is either undecided or already decided.
     *
     * @return true if a random continuation is possible, false otherwise.
     */
    protected boolean randomContinuationPossible() {
        var nextBoardIndex = getNextBoardIndex();

        return nextBoardIndex == -1 || (nextBoardIndex >= 0 && decidedBoards[nextBoardIndex] != -1);
    }

    /**
     * Determines which board the next move should be played on.
     * Returns -1 if no board restriction exists.
     *
     * @return The index of the next board to be played on, or -1 if unrestricted.
     */
    protected int getNextBoardIndex() {
        Move lastMove = getLastMove();
        if (lastMove == null || lastMove.position() == null) {
            return -1;
        }
        return lastMove.position().getIndex();
    }

    /**
     * Updates the board state after a move is made.
     *
     * @param move       The move that was performed.
     * @param boardIndex The index of the local board the move was made on.
     * @param player     The player who made the move.
     */
    private void updateBoard(Move move, int boardIndex, Player player) {
        moveHistory.add(move);
        updateLocalBoardStatus(board[boardIndex], boardIndex, player.getId());
        updateGlobalBoardStatus(player.getId());
        updateBoardsInProgress();
    }

    /**
     * Creates a deep copy of the board array.
     *
     * @param other The board to copy.
     * @return A new 2D array identical to the input board.
     */
    private int[][] copyBoard(int[][] other) {
        return Arrays.stream(other)
                .map(values -> Arrays.copyOf(values, 9))
                .toArray(int[][]::new);
    }

    /**
     * Updates the list of boards in progress by removing decided ones.
     */
    private void updateBoardsInProgress() {
        boardsInProgress = IntStream.range(0, 9)
                .filter(i -> decidedBoards[i] == -1)
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Updates the global game status based on whether a player has won or the game is a draw.
     *
     * @param playerId The ID of the player who made the last move.
     */
    private void updateGlobalBoardStatus(int playerId) {
        if (checkWin(decidedBoards, playerId)) {
            status = Status.fromId(playerId);
        } else if (checkGlobalDraw(decidedBoards)) {
            status = Status.DRAW;
        }
    }

    /**
     * Updates the local board status based on whether it has been won or drawn.
     *
     * @param localBoard The local board array.
     * @param boardIndex The index of the local board.
     * @param playerId   The ID of the player who made the move.
     */
    private void updateLocalBoardStatus(int[] localBoard, int boardIndex, int playerId) {
        if (checkWin(localBoard, playerId)) {
            decidedBoards[boardIndex] = playerId;
        } else if (checkLocalDraw(localBoard)) {
            decidedBoards[boardIndex] = 0;
        }
    }

    /**
     * Checks if the given player has won on the specified board.
     *
     * @param board    The board to check.
     * @param playerId The player ID to check for a win.
     * @return true if the player has won, false otherwise.
     */
    private boolean checkWin(int[] board, int playerId) {
        for (WinningLine line : WinningLine.values()) {
            int[] indices = line.getIndices();
            if (board[indices[0]] == playerId &&
                board[indices[1]] == playerId &&
                board[indices[2]] == playerId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a local board is full without a winner (a draw).
     *
     * @param board The local board to check.
     * @return true if the board is a draw, false otherwise.
     */
    private boolean checkLocalDraw(int[] board) {
        return Arrays.stream(board).noneMatch(num -> num == 0);
    }

    /**
     * Checks if the global game has ended in a draw.
     *
     * @param board The decided boards array.
     * @return true if all boards are decided without a winner, false otherwise.
     */
    private boolean checkGlobalDraw(int[] board) {
        return Arrays.stream(board).noneMatch(num -> num == -1);
    }

}
