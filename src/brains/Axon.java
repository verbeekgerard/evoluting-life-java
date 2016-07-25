package brains;

public class Axon {
	
	private double strength;
	private Neuron targetNeuron;
	
	public Axon(double strength) {
		this.strength = strength;
	}
	
	public Axon(double strength, Neuron targetNeuron) {
		this.strength = strength;
		this.targetNeuron = targetNeuron;
	}
	
	public void transmit(){
		targetNeuron.excite(strength);
	}
	
}

