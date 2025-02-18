package common;

public enum Status {

    IN_PROGRESS(-1),
    DRAW(0),
    HUMAN_WIN(1),
    AI_WIN(2);

    private final int id;

    Status(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Status fromId(int id) {
        for (Status status : Status.values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("No status with id " + id);
    }

}
