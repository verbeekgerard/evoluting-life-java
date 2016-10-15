package eu.luminis.robots;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PiServo implements IServo {
    private int angle;

    @Override
    public void moveTo(int angle) {
        this.angle = angle;
        // TODO: Call moveTo on the R-Pi API
        throw new NotImplementedException();
    }

    @Override
    public int getAngle() {
        return this.angle;
    }
}
