package eu.luminis.robots;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PiMotorsController implements IMotorsController {
    private final PiMotor leftMotor;
    private final PiMotor rightMotor;

    public PiMotorsController(PiMotor leftMotor, PiMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    @Override
    public void move(double left, double right) {
        // TODO: Call movement methods of the motors on the R-Pi API
        throw new NotImplementedException();
    }
}
