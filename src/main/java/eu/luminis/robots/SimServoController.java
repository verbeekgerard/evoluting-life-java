package eu.luminis.robots;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SimServoController implements IServoController {
    private SimRobot owner;
    private int angle;

    public SimServoController(SimRobot owner) {
        this.owner = owner;
    }

    @Override
    public int getAngle() {
        return this.angle;
    }

    @Override
    public void changeAngle(double angularChange) {
        // TODO: Change the direction the sensor is looking at
        throw new NotImplementedException();
    }
}
