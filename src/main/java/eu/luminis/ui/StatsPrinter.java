package eu.luminis.ui;

import eu.luminis.general.Event;
import eu.luminis.general.EventType;

import java.util.Observable;
import java.util.Observer;

public class StatsPrinter implements Observer {

	StatsCollector collector;

	public StatsPrinter(StatsCollector statsCollector) {
		collector = statsCollector;
	}

	@Override
	public void update(Observable o, Object arg) {
		Event event = (Event) arg;
		if (event.type.equals(EventType.CYCLE_END)) {
			if ((int)event.value % 500 == 0) {
				printStats();
			}
		}
	}

	private void printStats() {
		System.out.println(collector.getStats().toString());
	}
}