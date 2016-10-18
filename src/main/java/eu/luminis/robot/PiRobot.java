package eu.luminis.robot;

import java.io.IOException;

public class PiRobot implements Robot {

	private Motor motorLeft;
	private Motor motorRight;
	
	public PiRobot() {
		try {
			this.motorLeft = new Motor(17, 23);
			this.motorRight = new Motor(22, 27);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void moveLeft() {
		motorLeft.forward();
		motorRight.stop();
	}

	@Override
	public void moveRight() {
		motorLeft.stop();
		motorRight.forward();
	}

	@Override
	public void stop() {
		motorLeft.stop();
		motorRight.stop();
	}

	@Override
	public void moveForward() {
		motorLeft.forward();
		motorRight.forward();
	}

	
}
