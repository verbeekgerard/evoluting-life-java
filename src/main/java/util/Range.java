package util;

import java.util.concurrent.ThreadLocalRandom;

public class Range {

	double lower = 0;
	double upper = 0;
	
	public Range(double lower, double upper) {
		this.lower = lower;
		this.upper = upper;

         if (lower > upper){
        	 throw new Error("upper (" + upper + ") smaller than lower (" + lower + ")");
         } 
	}
	
	public double random() {
		return ThreadLocalRandom.current().nextDouble(lower, upper);
    }
	
	public double check(double value){
		return (value > upper) ? upper : (value < lower) ? lower : value;
	}
	
	public double checkLower(double value){
		return (value < lower) ? lower : value;
	}
	
	public double mutation(double mutationFraction){
		double randomFraction = new Range(-1 * mutationFraction, mutationFraction).random();
        return (upper - lower) * randomFraction;
	}

}
