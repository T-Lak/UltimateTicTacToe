package board;

import common.Move;
import common.Player;
import common.Position;
import common.Status;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MCTSBoard extends BaseBoard {

    public MCTSBoard() {
        super();
    }

    public MCTSBoard(MCTSBoard other) {
        super(other);
    }

    @Override
    public void performMove(Move move) {
        super.performMove(move);
    }

    @Override
    public Status getStatus() {
        return super.getStatus();
    }

    @Override
    public Move getLastMove() {
        return super.getLastMove();
    }

    public boolean inProgress() {
        return getStatus() == Status.IN_PROGRESS;
    }

    public List<Move> getNextMoves() {
        Move lastMove = getLastMove();
        Player nextPlayer = (lastMove == null) ? Player.AI : lastMove.getOpponent();

        if (nextPlayer == Player.HUMAN && randomContinuationPossible()) {
            return Collections.emptyList();
        }

        int nextBoardIndex = randomContinuationPossible() ? getRandomBoardIndex() : getNextBoardIndex();
        var localBoard = getBoardAt(nextBoardIndex);

        return IntStream.range(0, 9)
                .filter(i -> localBoard[i] == 0)
                .mapToObj(i -> new Move(nextBoardIndex, Position.fromIndex(i), nextPlayer))
                .collect(Collectors.toList());
    }

    public void randomPlay() {
        Move lastMove = getLastMove();
        Player nextPlayer = lastMove.getOpponent();

        int nextBoardIndex = randomContinuationPossible() ? getRandomBoardIndex() : getNextBoardIndex();

        int positionIndex = getRandomEmptyPosition(getBoardAt(nextBoardIndex));
        Move nextMove = new Move(nextBoardIndex, Position.fromIndex(positionIndex), nextPlayer);

        performMove(nextMove);
    }

    private int getRandomBoardIndex() {
        var boardsInProgress = getBoardsInProgress();
        Random random = new Random();

        return boardsInProgress.get(random.nextInt(boardsInProgress.size()));
    }

    private int getRandomEmptyPosition(int[] board) {
        List<Integer> emptyPositions = IntStream.range(0, 9)
                .filter(i -> board[i] == 0)
                .boxed()
                .toList();

        Random random = new Random();
        return emptyPositions.get(random.nextInt(emptyPositions.size()));
    }

}
