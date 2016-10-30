package eu.luminis;

import eu.luminis.events.EventBroadcaster;
import eu.luminis.events.EventType;
import eu.luminis.genetics.Genome;
import eu.luminis.robots.sim.SimWorld;

import java.util.List;

public class Simulation {

    private static final Simulation singleton = new Simulation();
    static Simulation getInstance() {
        return singleton;
    }

    private final SimWorld world;

	private Thread mainThread;
	private boolean loop = true;
    private int iteration = 0;

	private Simulation() {
		this.world = new SimWorld();
	}

	public List<Genome> exportPopulation() {
		return  world.getRobotPopulation().exportPopulation();
	}
	
	public void importPopulation(List<Genome> genomes) {
		stop();
        world.getRobotPopulation().importPopulation(genomes);
		start();
	}

    SimWorld getWorld() {
        return world;
    }

	void startMainLoop() {
		mainThread = createThread();
		mainThread.start();
	}

    private Thread createThread() {
        return new Thread(() -> {
            while (loop) {
                mainLoop();
                long sleepTime = (long) Options.mainLoopSleep.get();
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void stop() {
		loop = false;
		try {
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