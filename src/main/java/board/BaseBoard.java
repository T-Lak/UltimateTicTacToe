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

public abstract class BoardBase {

    private int[][] board;
    private Status status;
    private Move move;
    private List<Move> moveHistory;
    private List<Integer> boardsInProgress;
    private int[] decidedBoards;

    public BoardBase() {
        board = new int[9][9];
        status = Status.IN_PROGRESS;
        boardsInProgress = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
        decidedBoards = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1};
        moveHistory = new ArrayList<>();
    }

    public BoardBase(BoardBase other) {
        board = copyBoard(other.getBoard());
        status = other.getStatus();
        move = other.getMove();
        boardsInProgress = new ArrayList<>(other.getBoardsInProgress());
        decidedBoards = Arrays.copyOf(other.getDecidedBoards(), 9);
        moveHistory = new ArrayList<>(other.getMoveHistory());
    }

    protected void performMove(Move move) {
        var boardIndex = move.boardIndex();
        var position = move.position();
        var currentPlayer = move.player();
        var lastPlayer = getLastPlayer();

        if (lastPlayer != null && currentPlayer == lastPlayer)
            throw new IllegalArgumentException(
                    String.format("Player should be %s, but is %s", currentPlayer, lastPlayer)
            );

        board[boardIndex][position.getIndex()] = currentPlayer.getId();

        updateBoard(boardIndex, currentPlayer);
    }

    protected int[][] getBoard() {
        return board;
    }

    protected Move getMove() {
        return move;
    }

    protected Status getStatus() {
        return status;
    }

    protected List<Integer> getBoardsInProgress() {
        return boardsInProgress;
    }

    protected int[] getDecidedBoards() {
        return decidedBoards;
    }

    protected List<Move> getMoveHistory() {
        return moveHistory;
    }

    protected Player getLastPlayer() {
        return moveHistory.isEmpty() ? null : moveHistory.get(moveHistory.size() - 1).player();
    }

    protected Move getLastMove() {
        return moveHistory.isEmpty() ? null : moveHistory.get(moveHistory.size() - 1);
    }

//    protected int[] getRandomBoard() {
//        Random random = new Random();
//
//        var boardIndex = boardsInProgress.stream()
//                .skip(random.nextInt(boardsInProgress.size()))
//                .findFirst()
//                .orElseThrow();
//
//        return board[boardIndex];
//    }

    protected int[] getNextBoard() {
        return null;
    }

    protected int[] getBoardAt(int index) {
        return board[index];
    }

    protected boolean randomContinuationPossible() {
        var lastMove = getLastMove();
        var nextBoardIndex = lastMove.position().getIndex();

        return decidedBoards[nextBoardIndex] != -1;
    }

    private void updateBoard(int boardIndex, Player player) {
        moveHistory.add(move);
        updateLocalBoardStatus(board[boardIndex], boardIndex, player.getId());
        updateGlobalBoardStatus(player.getId());
        updateBoardsInProgress();
    }

    private int[][] copyBoard(int[][] other) {
        return Arrays.stream(other)
                .map(values -> Arrays.copyOf(values, 9))
                .toArray(int[][]::new);
    }

    private void updateBoardsInProgress() {
        boardsInProgress = IntStream.range(0, 9)
                .filter(i -> decidedBoards[i] == -1)
                .boxed()
                .collect(Collectors.toList());
    }

    private void updateGlobalBoardStatus(int playerId) {
        if (checkWin(decidedBoards, playerId)) {
            status = Status.fromId(playerId);
        } else if (checkGlobalDraw(decidedBoards)) {
            status = Status.DRAW;
        }
    }

    private void updateLocalBoardStatus(int[] board, int boardIndex, int playerId) {
        if (checkWin(board, playerId)) {
            decidedBoards[boardIndex] = playerId;
        } else if (checkLocalDraw(board)) {
            decidedBoards[boardIndex] = 0;
        }
    }

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

    private boolean checkLocalDraw(int[] board) {
        return Arrays.stream(board).noneMatch(num -> num == 0);
    }

    private boolean checkGlobalDraw(int[] board) {
        return Arrays.stream(board).noneMatch(num -> num == -1);
    }

}
