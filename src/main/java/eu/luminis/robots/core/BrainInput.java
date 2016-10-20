package eu.luminis.robots.core;

import eu.luminis.general.Options;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

public class BrainInput {
    public static int getNodesCount() {
        return 4;
    }

    private List<Double> values;
    private final double distance;
    private final double angle;
    private double fieldOfView;

    public BrainInput(double distance, double angle, double fieldOfView) {
        this.distance = distance;
        this.angle = angle;
        this.fieldOfView = fieldOfView;
    }

    public List<Double> getValues() {
        if (values == null) {
            values = createValues();
        }

        return values;
    }

    private List<Double> createValues() {
        values =  new ArrayList<>();

        values.add((fieldOfView / 2 + angle) / fieldOfView); // left
        values.add((fieldOfView / 2 - angle) / fieldOfView); // right
        values.add(distance);
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
