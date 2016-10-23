package eu.luminis.robots.core;

import eu.luminis.general.Options;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

public class BrainInput {
    public static int getNodesCount() {
        return 6;
    }

    private List<Double> values;
    private final double distance;
    private final double viewDistance;
    private final double angle;
    private final double fieldOfView;
    private double angleVelocity;

    BrainInput(double distance, double viewDistance, double angle, double fieldOfView, double angleVelocity) {
        this.distance = distance;
        this.viewDistance = viewDistance;
        this.angle = angle;
        this.fieldOfView = fieldOfView;
        this.angleVelocity = angleVelocity;
    }

    public List<Double> getValues() {
        if (values == null) {
            values = createValues();
        }

        return values;
    }

    private List<Double> createValues() {
        values =  new ArrayList<>();

        values.add(distance == viewDistance ? 0 : (fieldOfView / 2 + angle) / fieldOfView); // left
        values.add(distance == viewDistance ? 0 : (fieldOfView / 2 - angle) / fieldOfView); // right
        values.add(angleVelocity < 0 ? -1 * angleVelocity : 0); // left
        values.add(angleVelocity > 0 ? angleVelocity : 0); // right
        values.add((viewDistance - distance) / viewDistance);
        values.add(new Range(0, 1).random());

        normalizeValues();

        return values;
    }

    private void normalizeValues() {
        double normalizationFactor = (Options.maxThreshold.get() + Options.minThreshold.get()) / 2;

        for (int i = 0; i < values.size(); i++) {
            double normalizedValue = values.get(i) * normalizationFactor;
            values.set(i, normalizedValue);
        }
    }
}