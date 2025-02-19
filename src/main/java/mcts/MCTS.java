package mcts;

import common.Move;
import common.Player;
import mcts.tree.Node;
import mcts.tree.Tree;

/**
 * Implements the Monte Carlo Tree Search (MCTS) algorithm for AI decision-making.
 * The AI level determines the amount of time spent on searching for the best move.
 */
public class MCTS {

    private final Tree tree;
    private final int level;

    /**
     * Initializes MCTS with a new search tree and a specified AI difficulty level.
     *
     * @param aiLevel The AI difficulty level (1-10), where higher values result in deeper searches.
     * @throws IllegalArgumentException if the level is out of range.
     */
    public MCTS(int aiLevel) {
        if (aiLevel < 1 || aiLevel > 10)
            throw new IllegalArgumentException("Level must be between 1 and 10! Actual: " + aiLevel);

        tree = new Tree();
        level = aiLevel;
    }

    /**
     * Initializes MCTS with an existing search tree and a specified AI difficulty level.
     *
     * @param tree    The existing tree to continue search from.
     * @param aiLevel The AI difficulty level (1-10).
     * @throws IllegalArgumentException if the level is out of range.
     */
    public MCTS(Tree tree, int aiLevel) {
        if (aiLevel < 1 || aiLevel > 10)
            throw new IllegalArgumentException("Level must be between 1 and 10! Actual: " + aiLevel);

        this.tree = tree;
        level = aiLevel;
    }

    /**
     * Performs MCTS rollouts to determine the best move.
     * The search runs until the allocated time is exhausted.
     *
     * @return The best move found during the search.
     */
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

    /**
     * Selects the best node to explore using the Upper Confidence Bound (UCT) formula.
     * Traverses down the tree until a leaf node is reached.
     *
     * @param node The root node of the search.
     * @return The most promising node to expand.
     */
    private Node select(Node node) {
        while (node.hasChildren()) {
            node = UCT.findBestLeafNode(node);
            if (!node.getBoard().inProgress())
                break;
        }
        return node;
    }

    /**
     * Expands the given node by generating all possible child nodes (legal moves).
     *
     * @param node The node to expand.
     */
    private void expand(Node node) {
        node.expandChildren();
    }

    /**
     * Simulates a random game from the given node until a terminal state is reached.
     *
     * @param node The node from which the simulation starts.
     * @return The result of the simulation (AI win, Human win, or draw).
     */
    private int simulatePlayOut(Node node) {
        var tempNode = new Node(node);
        var board = tempNode.getBoard();

        while (board.inProgress()) {
            board.randomPlay();
        }

        return board.getStatus().getId();
    }

    /**
     * Backpropagates the simulation result up the tree, updating win scores.
     *
     * @param node   The node where the simulation ended.
     * @param result The result of the simulation (AI win, Human win, or draw).
     */
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

    /**
     * Calculates the end time for the search based on the AI difficulty level.
     *
     * @return The timestamp (in milliseconds) when the search should stop.
     */
    private long getEndTime() {
        return System.currentTimeMillis() + 100L * getTimeIntervalForLevel();
    }

    /**
     * Determines the time interval per search iteration based on the AI level.
     * Higher levels allow for more computation time.
     *
     * @return The time interval multiplier.
     */
    private int getTimeIntervalForLevel() {
        return 2 * (level - 1) + 1;
    }

}
