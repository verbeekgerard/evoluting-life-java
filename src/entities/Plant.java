package entities;

import genetics.Genome;
import main.Option;
import util.Range;

public class Plant extends Organism {

	public double growthPercentage;
	public double size;
	
	public Option minFoodSize = new Option(2);
	public Option maxFoodSize = new Option(8);
	public Option minGrowthPercentage = new Option(0.01);
	public Option maxGrowthPercentage = new Option(0.05);
	public Option populationSize = new Option(10*8);
	
	public Option minNutrition = new Option(1.0);
	public Option maxNutrition = new Option(3.0);

	public double nutrition;
	
    public void reset() {
        this.minFoodSize.reset();
        this.maxFoodSize.reset();
        this.minGrowthPercentage.reset();
        this.maxGrowthPercentage.reset();
        this.populationSize.reset();
    }
	
	public Plant(Genome genome, Position position, World world){
		super(genome, position, world);
		
		growthPercentage = new Range(minGrowthPercentage.get(), maxGrowthPercentage.get()).random();
	    size = new Range(minFoodSize.get(), maxFoodSize.get()).random();
	    
	    nutrition = genome.life.nutrition;
	}
	
    
    
    public double getNutritionN() {
        if (nutrition < 0) {
            return nutrition / minNutrition.get();
        }
        else {
            return nutrition / maxNutrition.get();
        }
    }

    public double consume(){
        this.size--;
        return nutrition; // Energy can vary per type of food in the future
    }

    public void run(){
        this.age++;
        this.size += this.size * this.growthPercentage / 100;
        if (this.size > maxFoodSize.get()) {
        	this.size = maxFoodSize.get();
        }
    }

    public double healthN() {
        return size / maxFoodSize.get();
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
