package eu.luminis.brains;

class Axon implements ITransmitter {
	private final double strength;
	private final Neuron targetNeuron;

    public Axon(double strength, Neuron targetNeuron) {
		this.strength = strength;
		this.targetNeuron = targetNeuron;
    }
	
	public void transmit() {
		targetNeuron.excite(strength);
	}	
}