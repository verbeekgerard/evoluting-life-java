package eu.luminis.evolution;

import eu.luminis.robots.SimRobot;

public class Score {
	public SimRobot context;
	public double score;
	public double occurenceSum;
	
	public Score(SimRobot context, double score){
		this.context = context;
		this.score = score;
	}
}
