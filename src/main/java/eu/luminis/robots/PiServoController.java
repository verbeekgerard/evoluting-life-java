package eu.luminis.robots;

public class PiServoController implements IServoController {
    private int angle;

    @Override
    public double getAngle() {
        return this.angle;
    }

    @Override
    public void changeAngle(double acceleration) {

    }
}
