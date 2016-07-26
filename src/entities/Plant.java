package entities;

import genetics.Genome;
import main.Options;
import util.Range;

public class Plant extends Organism {

	public double growthPercentage;
	public double size;
	public double nutrition;
	
	public Plant(Genome genome, Position position, World world){
		super(genome, position, world);
		
		growthPercentage = new Range(Options.minGrowthPercentage.get(), Options.maxGrowthPercentage.get()).random();
	    size = new Range(Options.minFoodSize.get(), Options.maxFoodSize.get()).random();
	    
	    nutrition = genome.life.nutrition;
	}
	
    public void run(){
        this.age++;
        this.size += this.size * this.growthPercentage / 100;
        if (this.size > Options.maxFoodSize.get()) {
        	this.size = Options.maxFoodSize.get();
        }
    }

    public double healthN() {
        return size / Options.maxFoodSize.get();
    }
    
	@Override
	public double getHealth() {
		return size;
	}

	@Override
	public double getSize() {
		return size;
	};
	
}
