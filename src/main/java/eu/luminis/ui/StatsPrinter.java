package eu.luminis.ui;

import eu.luminis.events.Event;
import eu.luminis.events.EventType;

import java.util.Observable;
import java.util.Observer;

public class StatsPrinter implements Observer {

	private StatsCollector collector;

	public StatsPrinter(StatsCollector statsCollector) {
		collector = statsCollector;
	}

	@Override
	public void update(Observable o, Object arg) {
		Event event = (Event) arg;
		if (event.type.equals(EventType.CYCLE_END)) {
			int iterationCount = (int)event.value;
			if (iterationCount > 500 && iterationCount % 500 == 0) {
				printStats();
			}
		}
	}

	private void printStats() {
		Stats stats = collector.getStats();
		System.out.println((stats==null)?"No stats available": stats.toString());
	}
}