package board;

import common.Move;
import common.Player;
import event.EventManager;

import static common.EventType.MOVE_MADE;

public class GlobalBoard extends BaseBoard {

    private final EventManager eventManager;

    public GlobalBoard(EventManager eventManager) {
        super();
        this.eventManager = eventManager;
    }

    public GlobalBoard(GlobalBoard other) {
        super(other);
        eventManager = other.getEventManager();
    }

    @Override
    public void performMove(Move move) {
        super.performMove(move);

        if (move.player() == Player.HUMAN)
            eventManager.notify(MOVE_MADE, move);
    }

    @Override
    public int[][] getBoard() {
        return super.getBoard();
    }

    public boolean isValidHumanMove(int boardIndex, int position) {
//        int[] localBoard;
//
//        if (randomContinuationPossible()) {
//            var decidedBoards = getDecidedBoards();
//            localBoard = getBoardAt(boardIndex);
//            return decidedBoards[boardIndex] == -1 && localBoard[position] == 0;
//        } else {
//            var nextBoardIndex = getNextBoardIndex();
//            localBoard = getBoardAt(nextBoardIndex);
//            return boardIndex == nextBoardIndex && localBoard[position] == 0;
//        }

        int nextBoardIndex = getNextBoardIndex();
        int[] localBoard = getBoardAt(randomContinuationPossible() ? boardIndex : nextBoardIndex);

        return (randomContinuationPossible() ?
                getDecidedBoards()[boardIndex] == -1 : boardIndex == nextBoardIndex)
                && localBoard[position] == 0;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}
