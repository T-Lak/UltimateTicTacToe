package event;

import common.EventType;
import common.Move;

public interface EventListener {

    void update(EventType eventType, Move move);

}
