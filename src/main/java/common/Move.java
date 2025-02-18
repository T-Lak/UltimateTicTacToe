package common;

public record Move(int boardIndex, Position position, Player player) {

    public Player getOpponent() {
        return player == Player.AI ? Player.HUMAN : Player.AI;
    }

}
