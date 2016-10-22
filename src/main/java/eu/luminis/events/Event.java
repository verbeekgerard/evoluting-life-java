package eu.luminis.events;

public class Event {
	public final EventType type;
	public final Object value;
	
	public Event(EventType type, Object value) {
		this.type = type;
		this.value = value;
	}
}
