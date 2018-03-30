package eu.luminis.util;

import java.util.concurrent.ThreadLocalRandom;

public class Range {
	private double lower = 0;
	private double upper = 0;
	
	public Range(double lower, double upper) {
		this.lower = lower;
		this.upper = upper;

         if (lower > upper){
        	 throw new Error("upper (" + upper + ") smaller than lower (" + lower + ")");
         } 
	}

    public double getLowerBound() {
        return lower;
    }

    public double getUpperBound() {
        return upper;
    }

	public double gaussian() {
		double width = upper - lower;
		double mean = (upper + lower) / 2;
		double sigma = width / 4;

		return ThreadLocalRandom.current().nextGaussian() * sigma + mean;
    }

	public double random() {
		return ThreadLocalRandom.current().nextDouble(lower, upper);
    }

    public double gaussianMutation(double mutationFraction) {
        double randomFraction = new Range(-1 * mutationFraction, mutationFraction).gaussian();
        return (upper - lower) * randomFraction;
    }

    public double mutation(double mutationFraction) {
        double randomFraction = new Range(-1 * mutationFraction, mutationFraction).random();
        return (upper - lower) * randomFraction;
    }

	public double assureBounds(double value) {
		return (value > upper) ? upper : (value < lower) ? lower : value;
	}

	public double assureFlippedBounds(double value) {
        return (value > upper) ? lower : (value < lower) ? upper : value;
    }
	
	public double assureLowerBound(double value) {
		return (value < lower) ? lower : value;
	}

	public double assureUpperBound(double value){
		return (value > upper) ? upper : value;
	}
}