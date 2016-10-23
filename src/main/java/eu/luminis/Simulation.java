package eu.luminis;

import eu.luminis.events.EventBroadcaster;
import eu.luminis.events.EventType;
import eu.luminis.robots.sim.SimWorld;

public class Simulation {

    private static final Simulation singleton = new Simulation();
    public static Simulation getInstance() {
        return singleton;
    }

    private final SimWorld world;

	private Thread mainThread;
	private boolean loop = true;
    private int iteration = 0;

	private Simulation() {
		this.world = new SimWorld();
	}
	
	public String exportPopulation() {
		return this.world.getRobotPopulation().exportPopulation();
	}
	
	public void importPopulation(String json) {
		stop();
		this.world.getRobotPopulation().importPopulation(json);
		start();
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
        world.getObstaclePopulation().run();
        world.getRobotPopulation().run();

        EventBroadcaster.getInstance().broadcast(EventType.CYCLE_END, iteration++);
	}
}