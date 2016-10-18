package eu.luminis.robot;

public class MockRobot implements Robot {

	@Override
	public void moveLeft() {
		System.out.println("Left");
	}

	@Override
	public void moveRight() {
		System.out.println("Right");
	}

	@Override
	public void stop() {
		System.out.println("Stop");
	}

	@Override
	public void moveForward() {
		System.out.println("Forward");
	}

}
