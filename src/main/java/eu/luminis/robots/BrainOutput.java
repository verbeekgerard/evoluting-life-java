package eu.luminis.robots;

import java.util.List;

class BrainOutput {
    private List<Double> values;

    public BrainOutput(List<Double> values) {
        this.values = values;
    }

    public double getMotorAccelerationLeft() {
        return this.values.get(0) - this.values.get(1);
    }

    public double getMotorAccelerationRight() {
        return this.values.get(2) - this.values.get(3);
    }

    public double getServoAcceleration() {
        return this.values.get(4) - this.values.get(5);
    }
}
