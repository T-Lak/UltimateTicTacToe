package mcts.tree;

import board.MCTSBoard;
import common.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a node in the Monte Carlo Tree Search (MCTS) tree.
 * Each node contains a game board state, tracks visits and win scores,
 * and maintains parent-child relationships for tree expansion.
 */
public class Node {

    private final List<Node> children;
    private final MCTSBoard board;
    private Node parent;
    private int winScore;
    private int visits;

    /**
     * Creates a root node with an empty game board.
     */
    public Node() {
        children = new ArrayList<>();
        board = new MCTSBoard();
        winScore = 0;
        visits = 0;
    }

    /**
     * Creates a new node by copying an existing node.
     * This constructor is used for simulation without affecting the original tree.
     *
     * @param other The node to copy.
     */
    public Node(Node other) {
        children = new ArrayList<>();
        board = new MCTSBoard(other.getBoard());
        parent = other;
        winScore = other.getWinScore();
        visits = other.getVisits();
    }

    /**
     * Creates a new node with a given parent and board state.
     * Used when expanding the tree with new game states.
     *
     * @param otherNode The parent node.
     * @param otherBoard The board state for this new node.
     */
    public Node(Node otherNode, MCTSBoard otherBoard) {
        children = new ArrayList<>();
        board = otherBoard;
        parent = otherNode;
        winScore = otherNode.getWinScore();
        visits = otherNode.getVisits();
    }

    /**
     * Expands the node by generating child nodes for all possible moves.
     * Each child represents a new possible game state.
     */
    public void expandChildren() {
        List<Move> nextMoves = board.getNextMoves();

        for (var move : nextMoves) {
            var newBoard = new MCTSBoard(board);
            newBoard.performMove(move);
            children.add(new Node(this, newBoard));
        }
    }

    /**
     * Finds the child node with the highest number of visits.
     * This helps in selecting the most promising move.
     *
     * @return The child node with the maximum visits.
     */
    public Node getChildWithMaxScore() {
        return Collections.max(children, Comparator.comparing(
                Node::getVisits
        ));
    }

    /**
     * Selects a random child node.
     * Used during simulations for exploring different game outcomes.
     *
     * @return A randomly selected child node.
     */
    public Node getRandomChild() {
        int maxPossibleMoves = children.size();
        int selectRandom = (int) (Math.random() * maxPossibleMoves);
        return children.get(selectRandom);
    }

    /**
     * Retrieves the list of child nodes.
     *
     * @return The list of children.
     */
    public List<Node> getChildren() {
        return children;
    }

    /**
     * Checks if this node has any children.
     *
     * @return True if children exist, false otherwise.
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * Retrieves the game board state of this node.
     *
     * @return The board associated with this node.
     */
    public MCTSBoard getBoard() {
        return board;
    }

    /**
     * Retrieves the parent node.
     *
     * @return The parent node.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Sets the parent node.
     *
     * @param parent The parent node to be set.
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * Retrieves the win score of this node.
     *
     * @return The current win score.
     */
    public int getWinScore() {
        return winScore;
    }

    /**
     * Updates the win score by adding the given score.
     * Used during backpropagation in MCTS.
     *
     * @param score The score to add.
     */
    public void updateWinScore(int score) {
        winScore += score;
    }

    /**
     * Retrieves the number of times this node has been visited.
     *
     * @return The visit count.
     */
    public int getVisits() {
        return visits;
    }

    /**
     * Increments the visit count of this node.
     * Used during the MCTS simulation process.
     */
    public void incrementVisits() {
        visits++;
    }
}
