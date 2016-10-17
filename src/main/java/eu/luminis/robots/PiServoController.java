package eu.luminis.robots;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PiServoController implements IServoController {
    private int angle;

    @Override
    public double getAngle() {
        return this.angle;
    }

    @Override
    public void changeAngle(double angularChange) {
        // TODO: Call moveTo on the R-Pi API
        throw new NotImplementedException();
    }
}
