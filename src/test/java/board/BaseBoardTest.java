package board;

import common.Move;
import common.Player;
import common.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseBoardTest {

    private BaseBoard board;

    @BeforeEach
    void setUp() {
        board = new MCTSBoard();
    }

    static Stream<Arguments> provideFirstTwoMoves() {
        return Stream.of(
            Arguments.of(
              new Move(0, Position.TOP_LEFT, Player.AI),
              new Move(Position.TOP_LEFT.getIndex(), Position.MIDDLE_CENTER, Player.HUMAN),
                    Map.of("first", Map.of("index", 0, "position", 0),
                            "second", Map.of("index", 0, "position", 4))
            ),
            Arguments.of(
                new Move(0, Position.TOP_CENTER, Player.AI),
                new Move(Position.TOP_CENTER.getIndex(), Position.MIDDLE_CENTER, Player.HUMAN),
                    Map.of("first", Map.of("index", 0, "position", 1),
                            "second", Map.of("index", 1, "position", 4))
            ),
            Arguments.of(
                new Move(0, Position.TOP_RIGHT, Player.AI),
                new Move(Position.TOP_RIGHT.getIndex(), Position.MIDDLE_CENTER, Player.HUMAN),
                    Map.of("first", Map.of("index", 0, "position", 2),
                            "second", Map.of("index", 2, "position", 4))
            ),
            Arguments.of(
                new Move(0, Position.MIDDLE_LEFT, Player.AI),
                new Move(Position.MIDDLE_LEFT.getIndex(), Position.MIDDLE_CENTER, Player.HUMAN),
                    Map.of("first", Map.of("index", 0, "position", 3),
                            "second", Map.of("index", 3, "position", 4))
            ),
            Arguments.of(
                new Move(0, Position.MIDDLE_CENTER, Player.AI),
                new Move(Position.MIDDLE_CENTER.getIndex(), Position.MIDDLE_CENTER, Player.HUMAN),
                    Map.of("first", Map.of("index", 0, "position", 4),
                            "second", Map.of("index", 4, "position", 4))
            ),
            Arguments.of(
                new Move(0, Position.MIDDLE_RIGHT, Player.AI),
                new Move(Position.MIDDLE_RIGHT.getIndex(), Position.MIDDLE_CENTER, Player.HUMAN),
                    Map.of("first", Map.of("index", 0, "position", 5),
                            "second", Map.of("index", 5, "position", 4))
            ),
            Arguments.of(
                new Move(0, Position.BOTTOM_LEFT, Player.AI),
                new Move(Position.BOTTOM_LEFT.getIndex(), Position.MIDDLE_CENTER, Player.HUMAN),
                    Map.of("first", Map.of("index", 0, "position", 6),
                            "second", Map.of("index", 6, "position", 4))
            ),
            Arguments.of(
                new Move(0, Position.BOTTOM_CENTER, Player.AI),
                new Move(Position.BOTTOM_CENTER.getIndex(), Position.MIDDLE_CENTER, Player.HUMAN),
                    Map.of("first", Map.of("index", 0, "position", 7),
                            "second", Map.of("index", 7, "position", 4))
            ),
            Arguments.of(
                new Move(0, Position.BOTTOM_RIGHT, Player.AI),
                new Move(Position.BOTTOM_RIGHT.getIndex(), Position.MIDDLE_CENTER, Player.HUMAN),
                    Map.of("first", Map.of("index", 0, "position", 8),
                            "second", Map.of("index", 8, "position", 4))
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFirstTwoMoves")
    void testPerformFirstTwoMoves(Move first, Move second, Map<String, Map<String, Integer>> expected) {
        // Extract expected indices
        var firstMoveInfo = expected.get("first");
        var secondMoveInfo = expected.get("second");

        // Perform first move
        board.performMove(first);
        var firstBoard = board.getBoardAt(firstMoveInfo.get("index"));
        var firstPosition = firstBoard[firstMoveInfo.get("position")];

        assertEquals(first.player().getId(), firstPosition, "First move should be correctly placed.");

        // Perform second move
        board.performMove(second);
        var secondBoard = board.getBoardAt(secondMoveInfo.get("index"));
        var secondPosition = secondBoard[secondMoveInfo.get("position")];

        assertEquals(second.player().getId(), secondPosition, "Second move should be correctly placed.");
    }

}
