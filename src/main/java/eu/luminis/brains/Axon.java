package eu.luminis.brains;

class Axon {
	private final double strength;
	private final Neuron targetNeuron;
	
	public Axon(double strength) {
		this.strength = strength;
		this.targetNeuron = null;
	}
	
	public Axon(double strength, Neuron targetNeuron) {
		this.strength = strength;
		this.targetNeuron = targetNeuron;
	}
	
	public void transmit() {
		assert targetNeuron != null;
		targetNeuron.excite(strength);
	}	
}