package eu.luminis.robots;


public class PiSensorController implements ISensorController {
	
	PiSensor piSensor;
	
	public PiSensorController(PiServoController servoController) {
		piSensor = new PiSensor();
	}
	
    @Override
    public double sense() {
    	return piSensor.sense();
    }
}
