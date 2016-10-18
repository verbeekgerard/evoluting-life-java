package eu.luminis.robots;

import java.io.IOException;

import eu.luminis.robot.Motor;

public class PiMotorsController implements IMotorsController {

	Motor motorLeft;
    Motor motorRight;
	
    public PiMotorsController() {
    	
        try {
        	motorLeft = new Motor(17, 23);
			motorRight = new Motor(22, 27);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void move(double leftChange, double rightChange) {
    	if (leftChange != 0 && rightChange != 0) {
    		motorLeft.forward();
            motorRight.forward();
		}
		else {
			if (leftChange == 0 && rightChange != 0) {
				motorRight.forward();
		        motorLeft.stop();
			}
			else if (leftChange != 0 && rightChange == 0) {
				motorLeft.forward();
		        motorRight.stop();
			} else {
				motorRight.stop();
				motorLeft.stop();
			}
		}
    }
}
