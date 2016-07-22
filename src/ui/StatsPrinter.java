package ui;

import java.util.Observable;
import java.util.Observer;

import entities.Animal;
import main.Event;
import main.EventType;
import main.FoodSupply;
import main.Population;

public class StatsPrinter implements Observer {
	
	private int totalSarved;
	private int totalConsumed;
	private int totalWandered;
	private int totalDiedOfAge;
	private double avgHealth;
	private FoodSupply foodSupply;
	private Population population;
	
	public StatsPrinter(FoodSupply foodSupply, Population population) {
		this.foodSupply = foodSupply;
		this.population = population;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		Event event = (Event) arg;
	
		if (event.type.equals(EventType.CONSUMED)) {
			totalConsumed++;
		}
		else if (event.type.equals(EventType.EAT)) {
//			System.out.println("Eat");
		}
		else if (event.type.equals(EventType.STARVED)) {
			totalSarved++;
		}
		else if (event.type.equals(EventType.WANDERED)) {
			totalWandered++;
		}
		else if (event.type.equals(EventType.DIED_OF_AGE)) {
			totalDiedOfAge++;
		}
		else if (event.type.equals(EventType.CYCLE_END)) {
			if ((int)event.value % 100 == 0) {
				collectStats();
				printStats();
				resetStats();
			}
		}
	}
	
	private void collectStats() {
		double totalHealth = 0;
		for (Animal animal : population.entities) {
			totalHealth += animal.getHealth();
	    }
		this.avgHealth = totalHealth / population.entities.size();
	}
	
	private void printStats() {
		System.out.println(
				"avg. health:\t" + avgHealth + "\t" +
				"totalConsumed:\t" + totalConsumed + "\t" +
				"totalSarved:\t" + totalSarved + "\t" +
				"totalWandered:\t" + totalWandered + "\t" +
				"totalDiedOfAge:\t" + totalDiedOfAge + "\t" +
				"best:\t" + population.winningEntity.rank() + "\t" 
				);
	}
	
	private void resetStats() {
		this.totalConsumed = 0;
		this.totalSarved = 0;
		this.totalWandered = 0;
		this.totalDiedOfAge = 0;
	}
}
