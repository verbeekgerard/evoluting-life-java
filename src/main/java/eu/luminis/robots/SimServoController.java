package eu.luminis.robots;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SimServoController implements IServoController {
    private SimRobot owner;
    private double angle;


    public SimServoController(SimRobot owner) {
        this.owner = owner;
    }

    @Override
    public double getAngle() {
        return this.angle;
    }

    @Override
    public void changeAngle(double acceleration) {
        angle = angle + acceleration*1;
        if (angle < 0  ) {
            angle = 0;
        }
        if (angle > Math.PI) {
            angle = Math.PI;
        }
        owner.recordAngleChange(acceleration);
    }
}
