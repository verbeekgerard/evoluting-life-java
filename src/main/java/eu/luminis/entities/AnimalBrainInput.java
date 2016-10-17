package eu.luminis.entities;

import eu.luminis.general.Options;
import eu.luminis.sensors.ObstacleVector;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

public class AnimalBrainInput {
    private List<Double> inputs;

    private ObstacleVector obstacle;
    private double wallDistance;
    private double fieldOfView;
    private double viewDistance;

    public AnimalBrainInput(ObstacleVector obstacle, double wallDistance, double fieldOfView, double viewDistance) {
        this.obstacle = obstacle;
        this.wallDistance = wallDistance;
        this.fieldOfView = fieldOfView;
        this.viewDistance = viewDistance;
    }

    public static int getNodesCount() {
        return 5;
    }

    public List<Double> getInputs() {
        if (inputs == null) {
            inputs = createInputs();
        }

        return inputs;
    }

    private List<Double> createInputs() {
        inputs = new ArrayList<>();

        // left
        inputs.add(obstacle != null ? (fieldOfView / 2 + obstacle.getAngle()) / fieldOfView : 0);
        // right
        inputs.add(obstacle != null ? (fieldOfView / 2 - obstacle.getAngle()) / fieldOfView : 0);
        // distance
        inputs.add(obstacle != null ? (viewDistance - obstacle.getDistance()) / viewDistance : 0);

        // distance to wall
        inputs.add((viewDistance - wallDistance) / viewDistance);
        // random
        inputs.add(new Range(0, 1).random());

        double normalizationFactor = (Options.maxThreshold.get() + Options.minThreshold.get()) / 2;
        // Normalize inputs
        for (int i = 0; i < inputs.size(); i++) {
            Double value = inputs.get(i);
            value *= normalizationFactor;
            inputs.set(i, value);
        }

        return inputs;
    }
}