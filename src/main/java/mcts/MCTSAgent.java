package mcts;

import board.MCTSBoard;
import common.Move;
import mcts.tree.Node;
import mcts.tree.Tree;

/**
 * The MCTSAgent class serves as an interface for using the Monte Carlo Tree Search (MCTS) algorithm.
 * It maintains an internal tree structure and allows updating it based on the game's progress.
 */
public class MCTSAgent {

    private final Tree tree;
    private final MCTS mcts;

    /**
     * Constructs an MCTS agent with a specified AI difficulty level.
     *
     * @param aiLevel The difficulty level (1 to 10) determining the depth of search.
     */
    public MCTSAgent(int aiLevel) {
        tree = new Tree();
        mcts = new MCTS(tree, aiLevel);
    }

    /**
     * Determines the next move using the MCTS algorithm.
     *
     * @return The best move found through MCTS simulations.
     */
    public Move getNextMove() {
        return mcts.rollout();
    }

    /**
     * Updates the internal MCTS tree after a move is made in the game.
     * If the move exists in the current tree, it sets the corresponding child node as the new root.
     * Otherwise, it creates a new root node based on the move.
     *
     * @param move The move that was played.
     */
    public void updateTree(Move move) {
        Node root = tree.getRoot();
        Node node = null;

        // Search for the child node corresponding to the played move
        for (var child : root.getChildren()) {
            var lastMove =  child.getBoard().getLastMove();
            if (lastMove == move) {
                node = child;
                break;
            }
        }

        if (node != null) {
            // If the move exists in the current tree, set it as the new root
            tree.setRoot(node);
        } else {
            // Otherwise, create a new node with an updated board state
            var board = new MCTSBoard(root.getBoard());
            board.performMove(move);
            tree.setRoot(new Node(root, board));
        }
    }
}
