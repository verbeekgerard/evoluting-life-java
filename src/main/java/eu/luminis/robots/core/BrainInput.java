package eu.luminis.robots.core;

import eu.luminis.Options;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

public class BrainInput {
    public static int getNodesCount() {
        return 8;
    }

    private double[] values;
    private final double viewDistance;
    private final double fieldOfView;
    private final double distance;
    private final double angle;
    private final double angleVelocity;

    BrainInput(IBrainInputParameters parameters, double distance, double angle, double angleVelocity) {
        this.viewDistance = parameters.getViewDistance();
        this.fieldOfView = parameters.getFieldOfView();
        this.distance = new Range(0, this.viewDistance).assureBounds(distance);
        this.angle = angle;
        this.angleVelocity = angleVelocity;
    }

    public double[] getValues() {
        if (values == null) {
            values = createValues();
        }

        return values;
    }

    private double[] createValues() {
        values = new double[] {
            distance == viewDistance ? 0 : (fieldOfView / 2 + angle) / fieldOfView, // left
            distance == viewDistance ? 0 : (fieldOfView / 2 - angle) / fieldOfView, // right
            (fieldOfView / 2 + angle) / fieldOfView, // left
            (fieldOfView / 2 - angle) / fieldOfView, // right
            angleVelocity < 0 ? -1 * angleVelocity : 0, // left
            angleVelocity > 0 ? angleVelocity : 0, // right
            (viewDistance - distance) / viewDistance,
            new Range(0, 1).random()
        };

        //normalizeValues();

        return values;
    }

    private void normalizeValues() {
        double normalizationFactor = Options.maxBias.get();

        for (int i = 0; i < values.length; i++) {
            values[i] *= normalizationFactor;
        }
    }
}
