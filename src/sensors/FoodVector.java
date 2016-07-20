package sensors;

import entities.Organism;

public class FoodVector implements Comparable<FoodVector> {

	public double distance;
	public double angle;
	public Organism organism;
	
	public FoodVector(double distance, double angle, Organism organism){
		this.distance = distance;
		this.angle = angle;
		this.organism = organism;
	}

	@Override
	public int compareTo(FoodVector o) {
		return new Double(this.distance).compareTo(o.distance);
	}
	
}
