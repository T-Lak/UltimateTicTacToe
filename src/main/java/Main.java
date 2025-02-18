import board.MCTSBoard;
import common.Move;
import common.Player;
import common.Position;

public class Main {

    public static void main(String[] args) {
        MCTSBoard board = new MCTSBoard();

        board.performMove(new Move(0, Position.MIDDLE_CENTER, Player.AI));
        board.performMove(new Move(4, Position.MIDDLE_CENTER, Player.HUMAN));
    }

}
