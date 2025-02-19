package api;

import board.GlobalBoard;
import common.Move;
import common.Player;
import common.Position;
import mcts.MCTSAgent;

import java.util.Map;

import static api.Serializer.serialize;

public class Controller {

    private GlobalBoard board;
    private MCTSAgent mctsAgent;

    public Controller() {
        init();
    }

    /**
     * Initializes a new game by creating a fresh board and AI agent.
     */
    public void init() {
        board = new GlobalBoard();
        mctsAgent = new MCTSAgent(8);
    }

    /**
     * Resets the game state by reinitializing the board and AI agent.
     */
    public void reset() {
        init();
    }

    /**
     * Performs a move for the human player.
     *
     * @param boardIndex The index of the local board where the move is made.
     * @param position The position within the local board.
     * @return A serialized representation of the updated game state.
     */
    public Map<String, Object> performHumanMove(int boardIndex, int position) {
        Move move = new Move(boardIndex, Position.fromIndex(position), Player.HUMAN);
        board.performMove(move);
        mctsAgent.updateTree(move);
        return serialize(board);
    }

    /**
     * Performs a move for the AI player.
     *
     * @return A serialized representation of the updated game state.
     */
    public Map<String, Object> performAIMove() {
        Move move = mctsAgent.getNextMove();
        board.performMove(move);
        return serialize(board);
    }

    /**
     * Checks whether a move made by the human player is valid.
     *
     * @param boardIndex The index of the local board.
     * @param position The position within the local board.
     * @return True if the move is valid, false otherwise.
     */
    public boolean isValidHumanMove(int boardIndex, int position) {
        return board.isValidHumanMove(boardIndex, position);
    }

}
