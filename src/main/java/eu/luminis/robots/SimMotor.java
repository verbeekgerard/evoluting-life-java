package eu.luminis.robots;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SimMotor implements IMotor {
    private SimRobot owner;

    public SimMotor(SimRobot owner) {
        this.owner = owner;
    }

    @Override
    public void stop() {
        // Does not change the position
    }

    @Override
    public void forward() {
        throw new NotImplementedException();
    }

    @Override
    public void reverse() {
        throw new NotImplementedException();
    }
}
