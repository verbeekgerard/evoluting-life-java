package sensors;

import entities.Organism;

public class FoodVector {

	public double distance;
	public double angle;
	public Organism organism;
	
	public FoodVector(double distance, double angle, Organism organism){
		this.distance = distance;
		this.angle = angle;
		this.organism = organism;
	}
	
//	foodVectors.push({
//        distance: distance,
//        angle: angle,
//        organism: organism
//      });
	
	
}
