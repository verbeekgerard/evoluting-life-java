package eu.luminis.general;

import java.util.Observable;

public class EventBroadcaster extends Observable {
    private static EventBroadcaster singleton = new EventBroadcaster();

    public static EventBroadcaster getInstance() {
        return singleton;
    }

    private EventBroadcaster() {}

    public void broadcast(EventType eventType, Object value) {
        setChanged();
        super.notifyObservers(new Event(eventType, value));
    }
}
