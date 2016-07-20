package genetics;

import main.Option;
import util.Range;

public class MovementGene {
	
	public double calculateForce(double maxVelocity, double friction){
        return maxVelocity * friction;
	}
	
	public Option linearFriction = new Option(0.05); // 0.065 // 0.024
	public Option angularFriction = new Option(0.08); // 0.25 // 0.08

	double minA = calculateForce(0.5, angularFriction.get());
	double maxA = calculateForce(5.0, angularFriction.get());
	double minL = calculateForce(5.0, linearFriction.get());
	double maxL = calculateForce(50.0,linearFriction.get());
	
	public Option minAngularForce = new Option(minA);
	public Option maxAngularForce = new Option(maxA);
	public Option minLinearForce = new Option(minL);
	public Option maxLinearForce = new Option(maxL);

	public Option angularForceMutationRate = new Option(0.15);
	public Option linearForceMutationRate = new Option(0.15);
	
	public Option mutationFraction = new Option(0.001);
	
	public double angularForce;
	public double linearForce;
	
	public MovementGene(){
		this.angularForce = new Range(minAngularForce.get(), maxAngularForce.get()).random();
        this.linearForce = new Range(minLinearForce.get(), maxLinearForce.get()).random();
	}
	
	public MovementGene(double angularForce, double linearForce){
		this.angularForce = angularForce;
        this.linearForce = linearForce;
	}
	
	public void mutate() {
		
        if (Math.random() <= angularForceMutationRate.get()) {
            this.angularForce += new Range(minAngularForce.get(), maxAngularForce.get()).mutation(mutationFraction.get());
            this.angularForce = new Range(0, 0).checkLower(this.angularForce);
        }

        if (Math.random() <= linearForceMutationRate.get()) {
            this.linearForce += new Range(minLinearForce.get(), maxLinearForce.get()).mutation(mutationFraction.get());
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

