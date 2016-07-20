package brains;

public class Axon {
	
	private int strength;
	private Neuron targetNeuron;
	
	public Axon() {
	}
	
	public Axon(Neuron targetNeuron) {
		this.targetNeuron = targetNeuron;
	}
	
	public void transmit(){
		targetNeuron.excite(strength);
	}
	
}