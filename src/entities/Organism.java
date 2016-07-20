package entities;

import genetics.Genome;

public abstract class Organism {

	public Genome genome;
	public Position position;
	public World world;
	public double age;
	
	public abstract double getHealth();
	public abstract double getSize();
	public abstract double consume();
	
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
//          this.wanderedNotifier.notifyAsync(1);
        	System.out.println("I wandered off :(");
          return false;
        }

        // Kill entities if it's exceeded starvation threshold
        if (this.getHealth() <= 0) {
        	System.out.println("I starved X(");
//          this.starvedNotifier.notifyAsync(1);
          return false;
        }

        // Randomly kill entities who've entered old age
        if (this.age > this.getOldAge()) {
        	
          // Vulnerable entities have 1/100 chance of death
          if (Math.random() * 100 <= 1) {
        	  System.out.println("I'm to old for this shit " + this.age + " - " + this.getOldAge());
         //   this.diedOfAgeNotifier.notifyAsync(1);
            return false;
          }
        }

        return true;
	}
	
}

//  this.wanderedNotifier = new Subject();
//  this.starvedNotifier = new Subject();
//  this.diedOfAgeNotifier = new Subject();
//