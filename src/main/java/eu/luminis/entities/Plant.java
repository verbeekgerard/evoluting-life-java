package eu.luminis.entities;

import eu.luminis.genetics.Genome;
import eu.luminis.general.Options;
import eu.luminis.util.Range;

public class Plant extends Organism {
	private double growthPercentage;
	private double size;

	public Plant(Genome genome, Position position, World world){
		super(genome, position, world);
		
		growthPercentage = new Range(Options.minGrowthPercentage.get(), Options.maxGrowthPercentage.get()).random();
	    size = new Range(Options.minFoodSize.get(), Options.maxFoodSize.get()).random();
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