package eu.luminis.robots;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SimServo implements IServo {
    private SimRobot owner;
    private int angle;

    public SimServo(SimRobot owner) {
        this.owner = owner;
    }

    @Override
    public void moveTo(int angle) {
        this.angle = angle;
        // TODO: Change the direction the sensor is looking at
        throw new NotImplementedException();
    }

    @Override
    public int getAngle() {
        return this.angle;
    }
}
