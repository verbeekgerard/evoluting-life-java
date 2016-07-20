package genetics;

import main.Option;
import main.Options;
import util.Range;

public class MovementGene {
	
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
		
        if (Math.random() <= Options.angularForceMutationRate.get()) {
            this.angularForce += new Range(Options.minAngularForce.get(), Options.maxAngularForce.get()).mutation(Options.mutationFraction.get());
            this.angularForce = new Range(0, 0).checkLower(this.angularForce);
        }

        if (Math.random() <= Options.linearForceMutationRate.get()) {
            this.linearForce += new Range(Options.minLinearForce.get(), Options.maxLinearForce.get()).mutation(Options.mutationFraction.get());
            this.linearForce = new Range(0, 0).checkLower(this.linearForce);
        }
    }

	public MovementGene clone(){
        return new MovementGene(angularForce, linearForce);
    }

	public MovementGene mate(MovementGene partner) {
		return clone();
//        return genetics.mate(this, partner, function (child) {
//            return new Self(child);
//        }); TODO 
    }
	
}

