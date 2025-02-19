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

/**
 * Represents a board used specifically for Monte Carlo Tree Search (MCTS).
 * Extends {@link BaseBoard} and provides additional methods for AI move generation.
 */
public class MCTSBoard extends BaseBoard {

    /**
     * Default constructor that initializes an empty MCTS board.
     */
    public MCTSBoard() {
        super();
    }

    /**
     * Copy constructor that creates a deep copy of another MCTSBoard instance.
     *
     * @param other The MCTSBoard instance to copy.
     */
    public MCTSBoard(MCTSBoard other) {
        super(other);
    }

    /**
     * Generates a list of all possible next moves based on the current board state.
     *
     * @return A list of valid moves.
     */
    public List<Move> getNextMoves() {
        Move lastMove = getLastMove();
        Player nextPlayer = (lastMove == null) ? Player.AI : lastMove.getOpponent();

        // If it's the human player's turn and a random continuation is possible, no moves are available.
        if (nextPlayer == Player.HUMAN && randomContinuationPossible()) {
            return Collections.emptyList();
        }

        // Determine the next board to play on, choosing randomly if necessary.
        int nextBoardIndex = randomContinuationPossible() ? getRandomBoardIndex() : getNextBoardIndex();
        var localBoard = getBoardAt(nextBoardIndex);

        // Generate all possible moves in the selected local board.
        return IntStream.range(0, 9)
                .filter(i -> localBoard[i] == 0)
                .mapToObj(i -> new Move(
                        nextBoardIndex,
                        Position.fromIndex(i),
                        nextPlayer)
                )
                .collect(Collectors.toList());
    }

    /**
     * Performs a random valid move for the next player.
     * This is used in MCTS rollouts to simulate random play.
     */
    public void randomPlay() {
        Move lastMove = getLastMove();
        Player nextPlayer = lastMove.getOpponent();

        // Determine the next board to play on.
        int nextBoardIndex = randomContinuationPossible() ? getRandomBoardIndex() : getNextBoardIndex();

        // Select a random empty position within the chosen board.
        int positionIndex = getRandomEmptyPosition(getBoardAt(nextBoardIndex));
        Move nextMove = new Move(
                nextBoardIndex,
                Position.fromIndex(positionIndex),
                nextPlayer
        );

        performMove(nextMove);
    }

    /**
     * Selects a random local board that is still in progress.
     *
     * @return The index of a random in-progress local board.
     */
    private int getRandomBoardIndex() {
        var boardsInProgress = getBoardsInProgress();
        Random random = new Random();

        return boardsInProgress.get(random.nextInt(boardsInProgress.size()));
    }

    /**
     * Selects a random empty position within a given local board.
     *
     * @param board The local board to search in.
     * @return The index of a randomly chosen empty position.
     */
    private int getRandomEmptyPosition(int[] board) {
        List<Integer> emptyPositions = IntStream.range(0, 9)
                .filter(i -> board[i] == 0)
                .boxed()
                .toList();

        Random random = new Random();
        return emptyPositions.get(random.nextInt(emptyPositions.size()));
    }

    /**
     * Checks if the game is still in progress.
     *
     * @return {@code true} if the game is ongoing, {@code false} otherwise.
     */
    public boolean inProgress() {
        return getStatus() == Status.IN_PROGRESS;
    }

}
