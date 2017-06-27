package eu.luminis.robots.sim;

import eu.luminis.Options;
import eu.luminis.events.EventBroadcaster;
import eu.luminis.events.EventType;
import eu.luminis.evolution.TournamentSelection;
import eu.luminis.genetics.Genome;
import eu.luminis.robots.core.BrainInput;
import eu.luminis.robots.core.BrainOutput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SimRobotPopulation {
    private final SimWorld world;
    // private final RouletteWheelSelectionByRank selection = new RouletteWheelSelectionByRank();
    private final TournamentSelection selection = new TournamentSelection();
    private final double populationSize;

    private List<SimRobot> simRobots = new ArrayList<>();
    private SimRobot winningEntity;

    public SimRobotPopulation(SimWorld world) {
        this.world = world;
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

    public void importPopulation(List<Genome> genomes) {
        simRobots = genomes.stream()
                .map(this::spawnNewEntity)
                .collect(Collectors.toList());
    }

    public List<Genome> exportPopulation() {
        return simRobots.stream()
                .map(SimRobot::getGenome)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void run() {
        simRobots.parallelStream().forEach(SimObstacle::runCycle);

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
        return SimRobotBuilder
                .simRobot()
                .withGenome(genome)
                .withWorld(world)
                .build();
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
