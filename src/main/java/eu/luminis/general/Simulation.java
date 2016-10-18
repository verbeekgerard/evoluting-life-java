package eu.luminis.general;

import eu.luminis.robots.SimRobotPopulation;
import eu.luminis.robots.SimWorld;
import eu.luminis.robots.StationaryObstaclePopulation;

public class Simulation {
	
	private Thread mainThread;
	private boolean loop = true;
	private SimWorld world;
	private int iteration = 0;

	private static Simulation singleton = new Simulation();
	
	public static Simulation getInstance() {
      return singleton;
	}
	
	private Simulation() {
		this.world = new SimWorld();
        
	}
	
	public String exportPopulation() {
		return this.getRobotPopulation().exportPopulation();
	}
	
	public void importPopulation(String json) {
		stop();
		this.getRobotPopulation().importPopulation(json);
		start();
	}

	StationaryObstaclePopulation getStationaryObstaclePopulation() {
        return world.getObstaclePopulation();
    }

    SimRobotPopulation getRobotPopulation() {
        return world.getRobotPopulation();
    }

    SimWorld getWorld() {
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
        getStationaryObstaclePopulation().run();

        // Run a tick of population life cycle
        getRobotPopulation().run();

        EventBroadcaster.getInstance().broadcast(EventType.CYCLE_END, iteration);
	}
}