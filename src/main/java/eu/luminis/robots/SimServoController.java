package eu.luminis.robots;

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
        angle += acceleration * 1;

        if (angle < -1 * Math.PI/2) {
            angle = -1 * Math.PI/2;
        }

        if (angle > Math.PI/2) {
            angle = Math.PI/2;
        }

        owner.recordAngleChange(acceleration);
    }
}
