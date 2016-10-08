package eu.luminis.brains;

import java.util.ArrayList;
import java.util.List;

import eu.luminis.genetics.*;

public class Neuron {

	private List<Axon> axons = new ArrayList<>();
	private double excitation = 0;
    private double threshold;
    private double relaxation;

	public Neuron(NeuronGene gene, List<Neuron> targetNeurons){
		this.threshold = gene.getThreshold();
        this.relaxation = gene.getRelaxation();
		
        if (targetNeurons == null) {
        	// Assemble an output neuron
            for (AxonGene axonGene : gene.getAxons()) {
                Axon axon = new Axon(axonGene.getStrength());
                axons.add(axon);
            }
        } else {
        	// Assemble a neuron that transmits to its axons
        	for (int i = 0; i < gene.getAxons().size(); i++) {
                Axon axon = new Axon(gene.getAxons().get(i).getStrength(), targetNeurons.get(i));
                axons.add(axon);
            }
        }
	}

	public double transmit() {
        if (this.excitation > this.threshold) {
        	double excitation = this.excitation;
        	this.excitation = 0;

            axons.forEach(Axon::transmit);

            return excitation;
        }
        else {
            this.excitation = this.excitation > 0 ?
                    this.excitation * (1-this.relaxation) :
                    0;

            return 0;
        }
    }
	
	public void excite(double value) {
        this.excitation += value;
    }
}