package eu.luminis.robots.pi;

import eu.luminis.robots.core.ISensorController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PiSensorController implements ISensorController {
    @Override
    public double sense() {
        // TODO: Call sense on the R-Pi API
        throw new NotImplementedException();
    }

    @Override
    public double getViewDistance() {
        // TODO: Find out what the view distance must be
        throw new NotImplementedException();
    }
}
