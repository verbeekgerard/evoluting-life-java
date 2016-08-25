package eu.luminis.general;

import eu.luminis.entities.World;

import java.util.Observable;

public class General extends Observable {
	
	public FoodSupply foodSupply;
	public Population population;
	public World world;
	private int iteration = 0;
	private static General singleton = new General();
	public static General getInstance() {
      return singleton;
	}

	private General() {
		this.world = new World();
        this.foodSupply = new FoodSupply(world);
        this.population = new Population(world);
	}

	public void startMainLoop(){
		while (true){
			mainLoop();
		}
	}
	
	public void mainLoop(){
          // Keep track of our iteration count
          iteration++;

          // Run a tick of foodSupply life cycle
          foodSupply.run();

          // Run a tick of population life cycle
          population.run(foodSupply.plants);

          broadcast(EventType.CYCLE_END, iteration);
	}
	
	public void broadcast(EventType eventType, Object value) {
		setChanged();
        super.notifyObservers(new Event(eventType, value));
	}
	
}