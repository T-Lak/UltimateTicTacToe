package board;

/**
 * Represents the global board in an Ultimate Tic-Tac-Toe game.
 * This board extends {@link BaseBoard} and provides game-specific validation logic.
 */
public class GlobalBoard extends BaseBoard {

    /**
     * Default constructor that initializes an empty global board.
     */
    public GlobalBoard() {
        super();
    }

    /**
     * Copy constructor that creates a deep copy of another GlobalBoard instance.
     *
     * @param other The GlobalBoard instance to copy.
     */
    public GlobalBoard(GlobalBoard other) {
        super(other);
    }

    /**
     * Checks if a human player's move is valid based on the game's rules.
     *
     * @param boardIndex The index of the local board where the move is intended.
     * @param position   The position within the local board where the move is intended.
     * @return {@code true} if the move is valid, {@code false} otherwise.
     */
    public boolean isValidHumanMove(int boardIndex, int position) {
        int nextBoardIndex = getNextBoardIndex();

        // Determine which local board is currently playable
        int[] localBoard = getBoardAt(randomContinuationPossible() ? boardIndex : nextBoardIndex);

        // Valid move conditions:
        // - If random continuation is possible, the selected board must be undecided.
        // - Otherwise, the boardIndex must match the expected next board.
        // - The selected position in the local board must be empty (0).
        return (randomContinuationPossible() ?
                getDecidedBoards()[boardIndex] == -1 : boardIndex == nextBoardIndex)
                && localBoard[position] == 0;
    }

}
