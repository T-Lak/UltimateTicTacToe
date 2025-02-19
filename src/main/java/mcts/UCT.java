package mcts;

import mcts.tree.Node;

import java.util.Collections;
import java.util.Comparator;

/**
 * Implements the Upper Confidence Bound for Trees (UCT) formula used in Monte Carlo Tree Search (MCTS).
 * The UCT formula helps balance exploration and exploitation when selecting nodes during search.
 */
public class UCT {

    /**
     * Selects the best child node using the UCT formula.
     * It compares all child nodes and picks the one with the highest UCT value.
     *
     * @param node The parent node whose children are evaluated.
     * @return The child node with the highest UCT score.
     */
    public static Node findBestLeafNode(Node node) {
        int totalVisitsOfParent = node.getVisits();

        return Collections.max(node.getChildren(), Comparator.comparing(child -> calcUCTValue(
                totalVisitsOfParent,
                child.getVisits(),
                child.getWinScore())
        ));
    }

    /**
     * Calculates the Upper Confidence Bound (UCT) value for a node.
     *
     * UCT formula:
     * UCT = (winScore / nodeVisits) + c * sqrt(ln(totalVisits) / nodeVisits)
     * where `c` is an exploration constant (typically sqrt(2)).
     *
     * @param totalVisits Total number of visits to the parent node.
     * @param nodeVisits  Number of visits to the current node.
     * @param winScore    Win score of the current node.
     * @return The UCT value used to select the best node.
     */
    private static double calcUCTValue(int totalVisits, int nodeVisits, double winScore) {
        if (nodeVisits == 0)
            return Integer.MAX_VALUE;

        double c = Math.sqrt(2);
        return (winScore / (double) nodeVisits) + c * Math.sqrt(Math.log(totalVisits) / (double) nodeVisits);
    }

}
