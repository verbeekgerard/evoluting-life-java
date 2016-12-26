package eu.luminis.genetics;

import java.util.ArrayList;
import java.util.List;

public class MovementGene extends Gene {
    private static final MovementGeneEvolver evolver = new MovementGeneEvolver();

	private double angularForce;
	private double linearForce;

	public MovementGene() {
	    angularForce = evolver.AngularForce.getNewValue();
	    linearForce = evolver.LinearForce.getNewValue();
	}

	public MovementGene(double angularForce, double linearForce) {
		this.angularForce = angularForce;
		this.linearForce = linearForce;
	}

	public double getAngularForce() {
		return angularForce;
	}

	public double getLinearForce() {
		return linearForce;
	}

	public void mutate() {
        angularForce = evolver.AngularForce.mutateValueWithLowerBound(angularForce, 0);
        linearForce = evolver.LinearForce.mutateValueWithLowerBound(linearForce, 0);
	}

	public List<MovementGene> mate(MovementGene partner) {
		return evolver.mate(this, partner);
	}

	@Override
	public List<Double> getInitiateProperties() {
		List<Double> list = new ArrayList<>();
		list.add(this.angularForce);
		list.add(this.linearForce);

		return list;
	}

	@Override
	public Gene initiate(List<Double> properties) {
		return new MovementGene(properties.get(0), properties.get(1));
	}
}