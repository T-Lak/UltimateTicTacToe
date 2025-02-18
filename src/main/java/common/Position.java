package common;

public enum Position {

    TOP_LEFT(0),
    TOP_CENTER(1),
    TOP_RIGHT(2),
    MIDDLE_LEFT(3),
    MIDDLE_CENTER(4),
    MIDDLE_RIGHT(5),
    BOTTOM_LEFT(6),
    BOTTOM_CENTER(7),
    BOTTOM_RIGHT(8);

    private final int index;

    Position(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static Position fromIndex(int index) {
        for (Position position : Position.values()) {
            if (position.getIndex() == index) {
                return position;
            }
        }
        throw new IllegalArgumentException("Invalid index: " + index);
    }

}
