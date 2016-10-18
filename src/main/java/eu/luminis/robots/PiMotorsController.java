package eu.luminis.robots;

public class PiMotorsController implements IMotorsController {
    private final PiMotor leftMotor;
    private final PiMotor rightMotor;

    public PiMotorsController(PiMotor leftMotor, PiMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    @Override
    public void move(double leftChange, double rightChange) {
        // TODO: Call movement methods of the motors on the R-Pi API
        
    }
}
