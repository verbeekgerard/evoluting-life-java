package eu.luminis.robots;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SimSensor implements ISensor {
    private SimRobot owner;

    public SimSensor(SimRobot owner) {
        this.owner = owner;
    }

    @Override
    public Double sense() {
        throw new NotImplementedException();
    }

    @Override
    public Double sense(long msTimeout) {
        throw new NotImplementedException();
    }
}
