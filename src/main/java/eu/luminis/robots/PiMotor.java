package eu.luminis.robots;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PiMotor implements IMotor {

    @Override
    public void stop() {
        // Does not change the position
    }

    @Override
    public void forward() {
        // TODO: Call forward on the R-Pi API
        throw new NotImplementedException();
    }

    @Override
    public void reverse() {
        // TODO: Call reverse on the R-Pi API
        throw new NotImplementedException();
    }
}
