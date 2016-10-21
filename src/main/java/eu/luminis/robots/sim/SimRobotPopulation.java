package eu.luminis.robots.sim;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.luminis.evolution.RouletteWheelSelectionByRank;
import eu.luminis.general.EventBroadcaster;
import eu.luminis.general.EventType;
import eu.luminis.general.Options;
import eu.luminis.genetics.Genome;
import eu.luminis.geometry.Position;
import eu.luminis.robots.core.BrainInput;
import eu.luminis.robots.core.BrainOutput;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class SimRobotPopulation {
    private SimWorld world;
    private final BorderDimensionsPositionGenerator borderDimensionsPositionGenerator;
    private List<SimRobot> simRobots = new CopyOnWriteArrayList<>(); // Slow lost but no exceptions in UI
    private SimRobot winningEntity;

    private double populationSize;

    public SimRobotPopulation(SimWorld world) {
        this.world = world;
        this.borderDimensionsPositionGenerator = new BorderDimensionsPositionGenerator(world);
        this.populationSize = Options.populationSize.get();

        for (int i = 0; i < populationSize; i++) {
            Genome genome = createGenome();
            spawnNewEntity(genome);
        }
    }

    public List<SimRobot> getAllRobots() {
        return simRobots;
    }

    public SimRobot getWinningEntity() {
        return winningEntity;
    }

    public void importPopulation(String json) {
        simRobots = new CopyOnWriteArrayList<>();

        Type listType = new TypeToken<ArrayList<Genome>>(){}.getType();
        List<Genome> genomes = new Gson().fromJson(json, listType);

        genomes.forEach(this::spawnNewEntity);
    }

    public String exportPopulation() {
        ArrayList<Genome> genomes = simRobots.stream()
                .map(SimRobot::getGenome)
                .collect(Collectors.toCollection(ArrayList::new));

        return new Gson().toJson(genomes);
    }

    public void run() {
        Collections.sort(this.simRobots);
        // Find the best ranking entity
        winningEntity = this.simRobots.get(0);

        List<SimRobot> entitiesToRemove = new ArrayList<>();
        for (SimRobot entity : simRobots) {
            entity.runCycle();

            if (!entity.survives()) {
                entitiesToRemove.add(entity);
            }
        }

        for (int i = 0; i < entitiesToRemove.size() / 2; i++) {
            List<SimRobot> parents = selectParents();
            produceChildren(parents);
        }

        for (SimRobot entityToRemove : entitiesToRemove) {
            this.simRobots.remove(entityToRemove);
        }

        while (this.simRobots.size() <= populationSize - 2) {
            List<SimRobot> parents = selectParents();
            produceChildren(parents);
        }
    }

    private Genome createGenome() {
        return new Genome(BrainInput.getNodesCount(), BrainOutput.getNodesCount());
    }

    private Position createRandomPosition() {
        return borderDimensionsPositionGenerator.createRandomPositionWithinRelativeBorder(0.98);
    }

    private void produceChildren(List<SimRobot> parents) {
        List<Genome> children = parents.get(0).getGenome().mate(parents.get(1).getGenome());
        for (Genome child : children) {
            child.mutate();

            spawnNewEntity(child);
        }
    }

    private void spawnNewEntity(Genome genome) {
        Position position = createRandomPosition();
        SimRobot newSimRobot = new SimRobot(genome, position, world);
        this.simRobots.add(newSimRobot);

        EventBroadcaster.getInstance().broadcast(EventType.NEW_ROBOT, newSimRobot);
    }

    private List<SimRobot> selectParents() {
        List<SimRobot> parents = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            SimRobot winningEntity = (SimRobot) new RouletteWheelSelectionByRank().select(this.simRobots);
            parents.add(winningEntity);
        }

        return parents;
    }
}
