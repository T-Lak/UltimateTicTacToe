package api;

import board.GlobalBoard;
import common.EventType;
import common.Move;
import common.Player;
import common.Position;
import event.EventManager;
import mcts.MCTSAgent;

import java.util.List;

import static api.ApiUtils.transformBoardToList;
import static common.EventType.MOVE_MADE;

public class Controller {

    private final GlobalBoard board;
    private final MCTSAgent mctsAgent;

    public Controller() {
        var eventManager = new EventManager(MOVE_MADE);
        board = new GlobalBoard(eventManager);
        mctsAgent = new MCTSAgent(eventManager, 8);
    }

    public List<List<String>> performHumanMove(int boardIndex, int position) {
        Move move = new Move(boardIndex, Position.fromIndex(position), Player.HUMAN);
        board.performMove(move);
        mctsAgent.updateTree(move);
        return transformBoardToList(board.getBoard());
    }

    public List<List<String>> performAIMove() {
        Move move = mctsAgent.getNextMove();
        board.performMove(move);
        return transformBoardToList(board.getBoard());
    }

    public boolean isValidHumanMove(int boardIndex, int position) {
        return board.isValidHumanMove(boardIndex, position);
    }

}
