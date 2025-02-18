package mcts;

import mcts.tree.Node;

import java.util.Collections;
import java.util.Comparator;

public class UCT {

    public static Node findBestLeafNode(Node node) {
        int totalVisitsOfParent = node.getVisits();

        return Collections.max(node.getChildren(), Comparator.comparing(child -> calcUCTValue(
                totalVisitsOfParent,
                child.getVisits(),
                child.getWinScore())
        ));
    }

    private static double calcUCTValue(int totalVisits, int nodeVisits, double winScore) {
        if (nodeVisits == 0)
            return Integer.MAX_VALUE;

        double c = Math.sqrt(2);
        return (winScore / (double) nodeVisits) + c * Math.sqrt(Math.log(totalVisits) / (double) nodeVisits);
    }

}
