package eu.luminis.robots;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SimServo implements IServo {
    private SimRobot owner;

    public SimServo(SimRobot owner) {
        this.owner = owner;
    }

    @Override
    public void moveTo(int angle) {
        // TODO: Change the direction the sensor is looking at
        throw new NotImplementedException();
    }
}
