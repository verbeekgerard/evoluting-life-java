package evolution;

import entities.Animal;

public class Score {
	public Animal context;
	public double score;
	public double occurenceSum;
	
	public Score(Animal context, double score){
		this.context = context;
		this.score = score;
	}
}
