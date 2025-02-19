package mcts.tree;

/**
 * Represents the Monte Carlo Tree Search (MCTS) tree structure.
 * The tree consists of nodes where each node represents a possible game state.
 */
public class Tree {

    private Node root;

    /**
     * Initializes the tree with a root node.
     * The root node represents the starting game state for MCTS.
     */
    public Tree() {
        root = new Node();
    }

    /**
     * Retrieves the root node of the tree.
     *
     * @return The root node.
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Sets a new root node.
     * This is used to update the tree when a move is made.
     *
     * @param root The new root node to be set.
     */
    public void setRoot(Node root) {
        this.root = root;
    }
}
