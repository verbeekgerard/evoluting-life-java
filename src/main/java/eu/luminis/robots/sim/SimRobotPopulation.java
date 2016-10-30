package eu.luminis.robots.sim;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.luminis.evolution.RouletteWheelSelectionByRank;
import eu.luminis.events.EventBroadcaster;
import eu.luminis.events.EventType;
import eu.luminis.Options;
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
    private final SimWorld world;
    private final PositionGenerator positionGenerator;
    private final RouletteWheelSelectionByRank selection = new RouletteWheelSelectionByRank();
    private final double populationSize;

    private List<SimRobot> simRobots = new ArrayList<>();
    private SimRobot winningEntity;

    public SimRobotPopulation(SimWorld world) {
        this.world = world;
        this.positionGenerator = new PositionGenerator(world);
        this.populationSize = Options.populationSize.get();

        List<SimRobot> robots = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Genome genome = createGenome();
            SimRobot robot = spawnNewEntity(genome);
            robots.add(robot);
        }

        simRobots = robots;
    }

    public List<SimRobot> getAllRobots() {
        return simRobots;
    }

    public SimRobot getWinningEntity() {
        return winningEntity;
    }

    public void importPopulation(String json) {
        Type listType = new TypeToken<ArrayList<Genome>>(){}.getType();
        List<Genome> genomes = new Gson().fromJson(json, listType);

        List<SimRobot> robots = new ArrayList<>();
        for (Genome genome : genomes) {
            SimRobot robot = spawnNewEntity(genome);
            robots.add(robot);
        }

        simRobots = robots;
    }

    public String exportPopulation() {
        ArrayList<Genome> genomes = simRobots.stream()
                .map(SimRobot::getGenome)
                .collect(Collectors.toCollection(ArrayList::new));

        return new Gson().toJson(genomes);
    }

    public void run() {
        simRobots.forEach(SimObstacle::runCycle);

        List<SimRobot> robots = simRobots.stream()
                .filter(SimObstacle::survives)
                .collect(Collectors.toList());

        Collections.sort(robots);

        while (robots.size() <= populationSize - 2) {
            List<SimRobot> parents = selectParents(robots);
            List<SimRobot> children = produceChildren(parents);
            robots.addAll(children);
        }

        winningEntity = robots.get(0);
        simRobots = robots;
    }

    private Genome createGenome() {
        return new Genome(BrainInput.getNodesCount(), BrainOutput.getNodesCount());
    }

    private Position createRandomPosition() {
        return positionGenerator.createRandomPositionWithinRelativeBorder(0.98);
    }

    private List<SimRobot> produceChildren(List<SimRobot> parents) {
        List<SimRobot> childRobots = new ArrayList<>();
        List<Genome> childGenomes = parents.get(0).getGenome().mate(parents.get(1).getGenome());
        for (Genome childGenome : childGenomes) {
            childGenome.mutate();

            SimRobot childRobot = spawnNewEntity(childGenome);
            childRobots.add(childRobot);
        }

        return childRobots;
    }

    private SimRobot spawnNewEntity(Genome genome) {
        Position position = createRandomPosition();
        SimRobot newSimRobot = new SimRobot(genome, position, world);

        EventBroadcaster.getInstance().broadcast(EventType.NEW_ROBOT, newSimRobot);

        return newSimRobot;
    }

    private List<SimRobot> selectParents(List<SimRobot> robots) {
        List<SimRobot> parents = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            SimRobot selectedEntity = (SimRobot)selection.select(robots);
            parents.add(selectedEntity);
        }

        return parents;
    }
}
