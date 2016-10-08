package eu.luminis.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimalBrainOutput {
    private static List<String> keys = new ArrayList<>();

    private static final String leftAccelerate = "leftAccelerate";
    private static final String leftDecelerate = "leftDecelerate";
    private static final String rightAccelerate = "rightAccelerate";
    private static final String rightDecelerate = "rightDecelerate";

    static {
        keys.add(leftAccelerate);
        keys.add(leftDecelerate);
        keys.add(rightAccelerate);
        keys.add(rightDecelerate);
    }

    private Map<String, Double> output;

    public AnimalBrainOutput(List<Double> valueList) {
        output = new HashMap<>();

        for (int i = 0; i < keys.size(); i++) {
            output.put(keys.get(i), valueList.get(i));
        }
    }

    public double getAccelerationLeft() {
        return this.output.get(leftAccelerate) - this.output.get(leftDecelerate);
    }

    public double getAccelerationRight() {
        return this.output.get(rightAccelerate) - this.output.get(rightDecelerate);
    }
}