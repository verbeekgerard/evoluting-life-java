package eu.luminis.robot;

import java.io.IOException;

import eu.luminis.entities.Animal;
import eu.luminis.entities.Position;
import eu.luminis.entities.World;
import eu.luminis.genetics.Genome;
import eu.luminis.sensors.Eyes;

public class RobotAnimal extends Animal {

	private Robot robot;
	
	public RobotAnimal(Genome genome, Position position, World world, Robot robot, Eyes eyes) throws IOException {
		super(genome, position, world);
		this.robot = robot;
		this.setEyes(eyes);
	}
	
	@Override
	public void move() {
		if (this.output.get("leftAccelerate") != 0 && this.output.get("rightAccelerate") != 0) {
			robot.moveForward();
		}
		else {
			if (this.output.get("leftAccelerate") == 0 && this.output.get("rightAccelerate") != 0) {
				robot.moveLeft();
			}
			else if (this.output.get("leftAccelerate") != 0 && this.output.get("rightAccelerate") == 0) {
				robot.moveRight();
			}
			else {
				robot.stop();
			}
		}
		
	}

}
