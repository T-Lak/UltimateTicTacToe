package common;

public enum Player {

    HUMAN(1),
    AI(2);

    private final int id;

    Player(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Player fromId(int id) {
        return switch (id) {
            case 1 -> AI;
            case 2 -> HUMAN;
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

}
