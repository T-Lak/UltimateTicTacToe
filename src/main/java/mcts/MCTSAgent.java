package mcts;

import board.MCTSBoard;
import common.EventType;
import common.Move;
import event.EventListener;
import event.EventManager;
import mcts.tree.Node;
import mcts.tree.Tree;

import static common.EventType.MOVE_MADE;

public class MCTSAgent implements EventListener {

    private final Tree tree;
    private final MCTS mcts;

    public MCTSAgent(EventManager eventManager, int aiLevel) {
        tree = new Tree();
        mcts = new MCTS(tree, aiLevel);
        eventManager.subscribe(MOVE_MADE, this);
    }

    public Move getNextMove() {
        return mcts.rollout();
    }

    public void updateTree(Move move) {
        Node root = tree.getRoot();
        Node node = null;

        for (var child : root.getChildren()) {
            var lastMove =  child.getBoard().getLastMove();
            if (lastMove == move) {
                node = child;
                break;
            }
        }

        if (node != null) {
            tree.setRoot(node);
        } else {
            var board = new MCTSBoard(root.getBoard());
            board.performMove(move);
            tree.setRoot(new Node(root, board));
        }
    }

    @Override
    public void update(EventType eventType, Move move) {
        /*Node root = tree.getRoot();
        Node node = null;

        for (var child : root.getChildren()) {
            var lastMove =  child.getBoard().getLastMove();
            if (lastMove == move) {
                node = child;
                break;
            }
        }

        if (node != null) {
            tree.setRoot(node);
        } else {
            System.out.println("else called");
            var board = new MCTSBoard(root.getBoard());
            board.performMove(move);
            tree.setRoot(new Node(root, board));
        }*/
    }
}
