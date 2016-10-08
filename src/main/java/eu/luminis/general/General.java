package eu.luminis.general;

import eu.luminis.entities.World;

import java.util.Observable;

public class General extends Observable {
	
	private Thread mainThread;
	private boolean loop = true;
	private FoodSupply foodSupply;
	private Population population;
	private World world;
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
	
	public String exportPopulation() {
		return this.population.exportPopulation();
	}
	
	public void importPopulation(String json) {
		stop();
		this.population.importPopulation(json);
		start();
	}
	
	public void stop() {
		loop = false;
		try {
			// Wait for the thread to join
			mainThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		loop = true;
		startMainLoop();
	}
	
	public void startMainLoop() {
		mainThread = new Thread(() -> {
			while (loop) {
				mainLoop();
				long sleepTime = (long) Options.mainLoopSleep.get();
				if (sleepTime > 0){
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		mainThread.start();
	}
	
	public void mainLoop(){
          // Keep track of our iteration count
          iteration++;

          // Run a tick of foodSupply life cycle
          foodSupply.run();

          // Run a tick of population life cycle
          population.run(foodSupply.getPlants());

          broadcast(EventType.CYCLE_END, iteration);
	}
	
	public void broadcast(EventType eventType, Object value) {
		setChanged();
        super.notifyObservers(new Event(eventType, value));
	}

	public FoodSupply getFoodSupply() {
		return foodSupply;
	}

	public Population getPopulation() {
		return population;
	}

	public World getWorld() {
		return world;
	}
}