package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

public class MovementGene extends Gene {
	private double angularForce;
	private double linearForce;
	
	public MovementGene() {
		initializeAngularForce();
		initializeLinearForce();
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
		mutateAngularForce();
		mutateLinearForce();
	}

	public List<MovementGene> mate(MovementGene partner) {
		return new Genetics().mate(this, partner);
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

	private void mutateAngularForce() {
		if (Math.random() <= Options.angularForceReplacementRate.get()) {
			initializeAngularForce();
			return;
		}

		if (Math.random() <= Options.angularForceMutationRate.get()) {
			this.angularForce += new Range(Options.minAngularForce.get(), Options.maxAngularForce.get()).mutation(Options.mutationFraction.get());
			this.angularForce = new Range(0, 0).assureLowerBound(this.angularForce);
		}
	}

	private void mutateLinearForce() {
		if (Math.random() <= Options.linearForceReplacementRate.get()) {
			initializeLinearForce();
			return;
		}

		if (Math.random() <= Options.linearForceMutationRate.get()) {
			this.linearForce += new Range(Options.minLinearForce.get(), Options.maxLinearForce.get()).mutation(Options.mutationFraction.get());
			this.linearForce = new Range(0, 0).assureLowerBound(this.linearForce);
		}
	}

	private void initializeAngularForce() {
		this.angularForce = new Range(Options.minAngularForce.get(), Options.maxAngularForce.get()).random();
	}

	private void initializeLinearForce() {
		this.linearForce = new Range(Options.minLinearForce.get(), Options.maxLinearForce.get()).random();
	}
}