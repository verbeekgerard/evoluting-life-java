package eu.luminis.robots;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SimMotorsController implements IMotorsController {
    private SimRobot owner;

    public SimMotorsController(SimRobot owner) {
        this.owner = owner;
    }

    @Override
    public void move(double left, double right) {
        // TODO: Change the position of the owner here
        throw new NotImplementedException();
    }
}
