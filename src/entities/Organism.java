package entities;

import genetics.Genome;
import main.Main;
import main.EventType;

public abstract class Organism {

	public Genome genome;
	public Position position;
	public World world;
	public double age;
	
	public abstract double getHealth();
	public abstract double getSize();
	
	public double getOldAge() {
		return genome.life.oldAge;
	}
	
	public Organism(Genome genome, Position position, World world){
		this.genome = genome;
		this.position = position;
		this.world = world;
	}
	
	public boolean lives(){
        Position p = this.position;

        if (p.x > world.width || p.x < 0 || p.y > world.height || p.y < 0) {
        	Main.getInstance().broadcast(EventType.WANDERED, 1);
        	return false;
        }

        // Kill entities if it's exceeded starvation threshold
        if (this.getHealth() <= 0) {
        	Main.getInstance().broadcast(EventType.STARVED, 1);
        	return false;
        }

        // Randomly kill entities who've entered old age
        if (this.age > this.getOldAge()) {
        	
        	// Vulnerable entities have 1/100 chance of death
        	if (Math.random() * 100 <= 1) {
        		Main.getInstance().broadcast(EventType.DIED_OF_AGE, 1);
        		return false;
          	}
        }

        return true;
	}
	
}
