package eu.luminis.util;

public class Physics {
	private final double friction;
	private final double maxForce;
	private final double maxVelocity;

	public Physics(double friction, double maxForce) {
		if (friction < 0.0) {
			throw new Error("friction (" + friction + ") smaller than zero");
		}

		if (friction < 0.0) {
			throw new Error("maxForce (" + maxForce + ") smaller than zero");
		}

		this.friction = friction;
		this.maxForce = maxForce;
		this.maxVelocity = Math.sqrt(maxForce / friction);
	}

	public double getFriction() {
        return this.friction;
    }

    public double getMaxForce() {
        return this.maxForce;
	}
	
	public double getMaxVelocity() {
		return this.maxVelocity;
	}

	public double calculateVelocity(double currentVelocity, double forceFraction) {
        return currentVelocity * (1 - friction * Math.abs(currentVelocity)) + forceFraction * maxForce;
//        return currentVelocity * (1 - friction) + linearChange * force;
	}
}
