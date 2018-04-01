package eu.luminis.genetics;

public class MovementGene extends Evolvable {
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

	public MovementGene[] mate(MovementGene partner) {
		return evolver.mate(this, partner);
	}

	@Override
	public double[] getInitiateProperties() {
		return new double[] {
				this.angularForce,
				this.linearForce
		};
	}

	@Override
	public MovementGene initiate(double[] properties) {
		return new MovementGene(properties[0], properties[1]);
	}

    @Override
    public MovementGene[] newArray(int size) {
        return new MovementGene[size];
    }
}