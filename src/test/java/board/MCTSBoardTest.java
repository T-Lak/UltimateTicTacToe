package board;

import common.Move;
import common.Player;
import common.Position;
import common.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utilities.TestUtilities;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MCTSBoardTest {

    private MCTSBoard board;
    private final int[][] winningLines = new int[][] {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6},
    };

    @BeforeEach
    void setUp() {
        board = new MCTSBoard();
    }

    private int checkWin(int[] board) {
        for (var line : winningLines) {
            if (board[line[0]] == 0 || board[line[0]] == -1)
                continue;
            if (board[line[0]] == board[line[1]] && board[line[0]] == board[line[2]]) {
                return board[line[0]];
            }
        }
        return -1;
    }

    private int checkLocalDraw(int[] board) {
        return Arrays.stream(board).noneMatch(num -> num == 0) ? 0 : -1;
    }

    private int checkGlobalDraw(int[] board) {
        return Arrays.stream(board).noneMatch(num -> num == -1) ? 0 : -1;
    }

    @RepeatedTest(100)
    void testRandomPlay() {
        Move firstMove = new Move(4, Position.fromIndex(4), Player.AI);
        board.performMove(firstMove);

        while (board.inProgress())
            board.randomPlay();

        Status actualResult = board.getStatus();

        int[] globalResult = new int[9];

        for (int i = 0; i < 9; i++) {
            var localBoard = board.getBoardAt(i);
            int win = checkWin(localBoard);
            globalResult[i] = win != -1 ? win : checkLocalDraw(localBoard);
        }

        int globalWin = checkWin(globalResult);

        var expectedResult = globalWin != -1 ? globalWin : checkGlobalDraw(globalResult);

        if (expectedResult != actualResult.getId())
            TestUtilities.printBoard(board.getBoard());

        assertFalse(board.inProgress());
        assertEquals(expectedResult, actualResult.getId(),
                "Mismatch detected! Expected: " + expectedResult + ", but got: " + actualResult.getId());
    }

    static Stream<Arguments> provideRegularMoves() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Move(0, Position.fromIndex(4), Player.AI)
                        ),
                        Map.of(
                                "boardIndex", 4,
                                "positions", Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                                "playerToMove", Player.HUMAN
                        )
                ),
                Arguments.of(
                        Arrays.asList(
                                new Move(4, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(0), Player.AI),
                                new Move(0, Position.fromIndex(4), Player.HUMAN)

                        ),
                        Map.of(
                              "boardIndex", 4,
                              "positions", Arrays.asList(1, 2, 3, 5, 6, 7, 8, 9),
                              "playerToMove", Player.AI
                        )
                ),
                Arguments.of(
                        Arrays.asList(
                                new Move(4, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(0), Player.AI),
                                new Move(0, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(1), Player.AI),
                                new Move(1, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(6), Player.AI),
                                new Move(6, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(8), Player.AI),
                                new Move(8, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(5), Player.AI),
                                new Move(5, Position.fromIndex(3), Player.HUMAN),
                                new Move(3, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(2), Player.HUMAN),
                                new Move(2, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(7), Player.HUMAN),
                                new Move(7, Position.fromIndex(4), Player.AI)

                        ),
                        Map.of(
                                "boardIndex", 4,
                                "positions", List.of(3),
                                "playerToMove", Player.HUMAN
                        )
                ),
                Arguments.of(
                        Arrays.asList(
                                new Move(4, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(0), Player.HUMAN),
                                new Move(0, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(1), Player.HUMAN),
                                new Move(1, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(6), Player.HUMAN),
                                new Move(6, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(8), Player.HUMAN),
                                new Move(8, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(5), Player.HUMAN),
                                new Move(5, Position.fromIndex(3), Player.AI),
                                new Move(3, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(2), Player.AI),
                                new Move(2, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(7), Player.AI),
                                new Move(7, Position.fromIndex(4), Player.HUMAN)

                        ),
                        Map.of(
                                "boardIndex", 4,
                                "positions", List.of(3),
                                "playerToMove", Player.AI
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideRegularMoves")
    void testGetNextMovesRegular(List<Move> moves, Map<String, Object> expectedParameters) {
        for (Move move : moves)
            board.performMove(move);

        List<Move> nextMoves = board.getNextMoves();

        int expectedBoardIndex = (int) expectedParameters.get("boardIndex");
        List<?> rawPositions = (List<?>) expectedParameters.get("positions");
        List<Integer> expectedPositions = rawPositions.stream()
                .map(o -> (Integer) o)
                .toList();
        Player expectedPlayer = (Player) expectedParameters.get("playerToMove");

        for (Move move : nextMoves) {
            assertEquals(expectedBoardIndex, move.boardIndex());
            assertTrue(expectedPositions.contains(move.position().getIndex()));
            assertEquals(expectedPlayer, move.player());

        }
    }

    static Stream<Arguments> provideMoveToBlockedBoard() {
        return Stream.of(
                Arguments.of(
                        Arrays.asList(
                                new Move(4, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(0), Player.AI),
                                new Move(0, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(1), Player.AI),
                                new Move(1, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(6), Player.AI),
                                new Move(6, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(8), Player.AI),
                                new Move(8, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(5), Player.AI),
                                new Move(5, Position.fromIndex(3), Player.HUMAN),
                                new Move(3, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(2), Player.HUMAN),
                                new Move(2, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(7), Player.HUMAN),
                                new Move(7, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(3), Player.HUMAN),
                                new Move(3, Position.fromIndex(5), Player.AI),
                                new Move(5, Position.fromIndex(4), Player.HUMAN)

                        ),
                        List.of(0, 1, 2, 3, 5, 6, 7, 8),
                        List.of(
                                List.of(0, 1, 2, 3, 5, 6, 7, 8),
                                List.of(0, 1, 2, 3, 5, 6, 7, 8),
                                List.of(0, 1, 2, 3, 5, 6, 7, 8),
                                List.of(0, 1, 2, 3, 6, 7, 8),
                                List.of(),
                                List.of(0, 1, 2, 5, 6, 7, 8),
                                List.of(0, 1, 2, 3, 5, 6, 7, 8),
                                List.of(0, 1, 2, 3, 5, 6, 7, 8),
                                List.of(0, 1, 2, 3, 5, 6, 7, 8)
                        ),
                        Player.AI
                ),
                Arguments.of(
                        Arrays.asList(
                                new Move(4, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(0), Player.HUMAN),
                                new Move(0, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(1), Player.HUMAN),
                                new Move(1, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(6), Player.HUMAN),
                                new Move(6, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(8), Player.HUMAN),
                                new Move(8, Position.fromIndex(4), Player.AI),
                                new Move(4, Position.fromIndex(5), Player.HUMAN),
                                new Move(5, Position.fromIndex(3), Player.AI),
                                new Move(3, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(2), Player.AI),
                                new Move(2, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(7), Player.AI),
                                new Move(7, Position.fromIndex(4), Player.HUMAN),
                                new Move(4, Position.fromIndex(3), Player.AI),
                                new Move(3, Position.fromIndex(5), Player.HUMAN),
                                new Move(5, Position.fromIndex(4), Player.AI)

                        ),
                        List.of(),
                        List.of(),
                        Player.HUMAN
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideMoveToBlockedBoard")
    void testGetNextMovesForMoveToBlockedBoard(List<Move> moves, List<Integer> expectedBoardIndexes,
                                               List<List<Integer>> expectedPositions, Player expectedPlayer) {
        for (Move move : moves)
            board.performMove(move);

        List<Move> nextMoves = board.getNextMoves();

        if (expectedPlayer == Player.AI) {
            for (Move move : nextMoves) {
                assertTrue(expectedBoardIndexes.contains(move.boardIndex()));
                assertTrue(expectedPositions.get(move.boardIndex()).contains(move.position().getIndex()));
                assertEquals(expectedPlayer, move.player());
            }
        } else {
            assertTrue(nextMoves.isEmpty());
        }

    }

}
