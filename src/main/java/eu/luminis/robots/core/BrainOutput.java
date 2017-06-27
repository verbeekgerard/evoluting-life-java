package eu.luminis.robots.core;

public class BrainOutput {
    public static int getNodesCount() {
        return 6;
    }

    private double[] values;

    BrainOutput(double[] values) {
        this.values = values;
    }

    public double getMotorAccelerationLeft() {
        return this.values[0] - this.values[1];
    }

    public double getMotorAccelerationRight() {
        return this.values[2] - this.values[3];
    }

    public double getServoAcceleration() {
        return this.values[4] - this.values[5];
    }
}
