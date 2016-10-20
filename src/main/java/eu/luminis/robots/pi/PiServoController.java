package eu.luminis.robots.pi;

import eu.luminis.robots.core.IServoController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PiServoController implements IServoController {
    private int angle;

    @Override
    public double getAngle() {
        return this.angle;
    }

    @Override
    public void changeAngle(double acceleration) {
        // TODO: Call moveTo on the R-Pi API
        throw new NotImplementedException();
    }
}
