package eu.luminis.events;

import java.util.Observable;

public class EventBroadcaster extends Observable {
    private static final EventBroadcaster singleton = new EventBroadcaster();

    public static EventBroadcaster getInstance() {
        return singleton;
    }

    private EventBroadcaster() {}

    public void broadcast(EventType eventType, Object value) {
        setChanged();
        super.notifyObservers(new Event(eventType, value));
    }
}
