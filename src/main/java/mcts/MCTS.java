package mcts;

import common.Move;
import common.Player;
import mcts.tree.Node;
import mcts.tree.Tree;

import java.util.concurrent.locks.LockSupport;

public class MCTS {

    private final Tree tree;
    private final int level;

    public MCTS(int aiLevel) {
        if (aiLevel < 1 || aiLevel > 10)
            throw new IllegalArgumentException("Level must be between 1 and 10! Actual: " + aiLevel);

        tree = new Tree();
        level = aiLevel;
    }

    public MCTS(Tree tree, int aiLevel) {
        if (aiLevel < 1 || aiLevel > 10)
            throw new IllegalArgumentException("Level must be between 1 and 10! Actual: " + aiLevel);

        this.tree = tree;
        level = aiLevel;
    }

    public Move rollout() {
        var root = tree.getRoot();
        var endTime = getEndTime();

        while (System.currentTimeMillis() < endTime) {
            Node promisingNode = select(root);

            if (promisingNode.getBoard().inProgress()) {
                expand(promisingNode);
            }

            Node nodeToExplore = promisingNode;

            if (promisingNode.hasChildren()) {
                nodeToExplore = promisingNode.getRandomChild();
            }

            var simulationResult = simulatePlayOut(nodeToExplore);
            backPropagate(nodeToExplore, simulationResult);
        }

        var winnderNode = root.getChildWithMaxScore();
        tree.setRoot(winnderNode);

        return winnderNode.getBoard().getLastMove();
    }

    private Node select(Node node) {
        while (node.hasChildren()) {
            node = UCT.findBestLeafNode(node);
            if (!node.getBoard().inProgress())
                break;
        }
        return node;
    }

    private void expand(Node node) {
        node.expandChildren();
    }

    private int simulatePlayOut(Node node) {
        var tempNode = new Node(node);
        var board = tempNode.getBoard();

        while (board.inProgress()) {
            board.randomPlay();
        }

        return board.getStatus().getId();
    }

    private void backPropagate(Node node, int result) {
        var tempNode = node;

        while (tempNode != null) {
            tempNode.incrementVisits();

            if (result == Player.AI.getId()) {
                tempNode.updateWinScore(100);
            } else if (result == Player.HUMAN.getId()) {
                tempNode.updateWinScore(-100);
            }

            tempNode = tempNode.getParent();
        }
    }

    private long getEndTime() {
        return System.currentTimeMillis() + 100L * getTimeIntervalForLevel();
    }

    private int getTimeIntervalForLevel() {
        return 2 * (level - 1) + 1;
    }

}
