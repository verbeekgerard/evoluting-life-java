package eu.luminis.entities;

import eu.luminis.general.EventBroadcaster;
import eu.luminis.genetics.Genome;
import eu.luminis.general.General;
import eu.luminis.general.EventType;

public abstract class Organism {

	protected double age;
	protected EventBroadcaster eventBroadcaster = EventBroadcaster.getInstance();

	private Genome genome;
	private Position position;
	private World world;

	public abstract double getHealth();
	public abstract double getSize();
	
	public double getAge() {
		return this.age;
	}
	public double getOldAge() {
		return genome.getLife().getOldAge();
	}
	public Genome getGenome() { return genome; }
	public Position getPosition() { return position; }

	Organism(Genome genome, Position position, World world){
		this.genome = genome;
		this.position = position;
		this.world = world;
	}
	
	public boolean lives(){
        Position p = this.position;

        if (p.x > world.getWidth() || p.x < 0 || p.y > world.getHeight() || p.y < 0) {
            eventBroadcaster.broadcast(EventType.WANDERED, 1);
        	return false;
        }

        // Kill eu.luminis.entities if it's exceeded starvation threshold
        if (this.getHealth() <= 0) {
            eventBroadcaster.broadcast(EventType.STARVED, 1);
        	return false;
        }

        // Randomly kill eu.luminis.entities who've entered old age
        if (this.age > this.getOldAge()) {
        	
        	// Vulnerable eu.luminis.entities have 1/100 chance of death
        	if (Math.random() * 100 <= 1) {
                eventBroadcaster.broadcast(EventType.DIED_OF_AGE, 1);
        		return false;
          	}
        }

        return true;
	}
}