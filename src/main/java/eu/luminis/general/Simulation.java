package eu.luminis.general;

import eu.luminis.entities.World;

public class Simulation {
	
	private Thread mainThread;
	private boolean loop = true;
	private FoodSupply foodSupply;
	private Population population;
	private World world;
	private int iteration = 0;

	private static Simulation singleton = new Simulation();
	
	public static Simulation getInstance() {
      return singleton;
	}
	
	private Simulation() {
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

    FoodSupply getFoodSupply() {
        return foodSupply;
    }

    Population getPopulation() {
        return population;
    }

    World getWorld() {
        return world;
    }

	void startMainLoop() {
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

	private void stop() {
		loop = false;
		try {
			// Wait for the thread to join
			mainThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void start() {
		loop = true;
		startMainLoop();
	}

	private void mainLoop() {
        // Keep track of our iteration count
        iteration++;

        // Run a tick of foodSupply life cycle
        foodSupply.run();

        // Run a tick of population life cycle
        population.run(foodSupply.getPlants());

        EventBroadcaster.getInstance().broadcast(EventType.CYCLE_END, iteration);
	}
}