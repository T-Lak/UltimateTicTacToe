package mcts.tree;

import board.MCTSBoard;
import common.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Node {

    private final List<Node> children;
    private final MCTSBoard board;
    private Node parent;
    private int winScore;
    private int visits;

    public Node() {
        children = new ArrayList<>();
        board = new MCTSBoard();
        winScore = 0;
        visits = 0;
    }

    public Node(Node other) {
        children = new ArrayList<>();
        board = other.getBoard();
        parent = other;
        winScore = other.getWinScore();
        visits = other.getVisits();
    }

    public Node(Node otherNode, MCTSBoard otherBoard) {
        children = new ArrayList<>();
        board = otherBoard;
        parent = otherNode;
        winScore = otherNode.getWinScore();
        visits = otherNode.getVisits();
    }

    public void expandChildren() {
        List<Move> nextMoves = board.getNextMoves();

        for (var move : nextMoves) {
            var newBoard = new MCTSBoard(board);
            newBoard.performMove(move);
            children.add(new Node(this, newBoard));
        }
    }

    public Node getChildWithMaxScore() {
        return Collections.max(children, Comparator.comparing(
                Node::getVisits
        ));
    }

    public Node getRandomChild() {
        int maxPossibleMoves = children.size();
        int selectRandom = (int) (Math.random() * maxPossibleMoves);
        return children.get(selectRandom);
    }

    public List<Node> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public MCTSBoard getBoard() {
        return board;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getWinScore() {
        return winScore;
    }

    public void updateWinScore(int score) {
        winScore += score;
    }

    public int getVisits() {
        return visits;
    }

    public void incrementVisits() {
        visits++;
    }
}
