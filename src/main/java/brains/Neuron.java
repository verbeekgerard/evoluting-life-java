package brains;

import java.util.ArrayList;
import java.util.List;

import genetics.*;

public class Neuron {

	private List<Axon> axons = new ArrayList<>();
	private double excitation = 0;
	public NeuronGene gene;
	
	public Neuron(NeuronGene gene, List<Neuron> targetNeurons){
		this.gene = gene;
		
        if (targetNeurons == null) {
        	// Assemble an output neuron
        	for (int i = gene.axons.size()-1;i>=0; i--) {
                axons.add(new Axon(gene.axons.get(i).strength));
            }
        } else { 
        	// Assemble a neuron that transmits to its axons
        	for (int i = gene.axons.size()-1;i>=0; i--) {
        		
                axons.add(new Axon(gene.axons.get(i).strength, targetNeurons.get(i)));
            }
        }
	}
	
	public double getRelaxation() {
        return gene.relaxation;
    }
	
	public double getThreshold() {
        return gene.threshold;
    }
	
	public double transmit() {
        if (this.excitation > getThreshold()) {
        	double excitation = this.excitation;
        	this.excitation = 0;

            for (int i=0; i<axons.size(); i++) {
                axons.get(i).transmit();
            }

            return excitation;
        }
        else {
            this.excitation = this.excitation > 0 ? this.excitation * (1-this.getRelaxation()) : 0;

            return 0;
        }
    }
	
	public void excite(double value) {
        this.excitation += value;
    }
}