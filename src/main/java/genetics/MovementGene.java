package genetics;

import java.util.ArrayList;
import java.util.List;

import general.Options;
import util.Range;

public class MovementGene extends Gene {
	
	public double angularForce;
	public double linearForce;
	
	public MovementGene(){
		this.angularForce = new Range(Options.minAngularForce.get(), Options.maxAngularForce.get()).random();
        this.linearForce = new Range(Options.minLinearForce.get(), Options.maxLinearForce.get()).random();
	}
	
	public MovementGene(double angularForce, double linearForce){
		this.angularForce = angularForce;
        this.linearForce = linearForce;
	}
	
	public void mutate() {
        if (Math.random() <= Options.angularForceReplacementRate.get()) {
    		this.angularForce = new Range(Options.minAngularForce.get(), Options.maxAngularForce.get()).random();
        }
        else if (Math.random() <= Options.angularForceMutationRate.get()) {
            this.angularForce += new Range(Options.minAngularForce.get(), Options.maxAngularForce.get()).mutation(Options.mutationFraction.get());
            this.angularForce = new Range(0, 0).checkLower(this.angularForce);
        }

        if (Math.random() <= Options.linearForceReplacementRate.get()) {
            this.linearForce = new Range(Options.minLinearForce.get(), Options.maxLinearForce.get()).random();
        }
        else if (Math.random() <= Options.linearForceMutationRate.get()) {
            this.linearForce += new Range(Options.minLinearForce.get(), Options.maxLinearForce.get()).mutation(Options.mutationFraction.get());
            this.linearForce = new Range(0, 0).checkLower(this.linearForce);
        }
    }
	
	public List<MovementGene> mate(MovementGene partner) {
		return (List<MovementGene>) new Genetics().mate(this, partner);
    }
	
	public List<String> getInitiateProperties() {
		List<String> properties = new ArrayList<>();
		properties.add("angularForce");
		properties.add("linearForce");
		return properties;
	}
	
	public MovementGene initiate(List<Double> properties){
		return new MovementGene(properties.get(0), properties.get(1));
	}	
}