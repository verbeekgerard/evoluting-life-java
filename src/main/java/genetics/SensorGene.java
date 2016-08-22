package genetics;

import java.util.ArrayList;
import java.util.List;

import general_temp.Options;
import util.Range;

public class SensorGene extends Gene {

    public double viewDistance;
    public double fieldOfView;
    
    public SensorGene(double viewDistance, double fieldOfView){
    	this.viewDistance = viewDistance;
        this.fieldOfView = fieldOfView;
    }
    
    public SensorGene() {
        this.viewDistance = new Range(Options.minViewDistance.get(), Options.maxViewDistance.get()).random();
        this.fieldOfView = new Range(Options.minFieldOfView.get(), Options.maxFieldOfView.get()).random();
    }
    
    public void mutate() {
        if (Math.random() <= Options.viewDistanceReplacementRate.get()) {
            this.viewDistance = new Range(Options.minViewDistance.get(), Options.maxViewDistance.get()).random();
        }
        else if (Math.random() <= Options.viewDistanceMutationRate.get()) {
        	this.viewDistance += new Range(Options.minViewDistance.get(), Options.maxViewDistance.get()).mutation(Options.mutationFraction.get());
        }

        if (Math.random() <= Options.fieldOfViewReplacementRate.get()) {
            this.fieldOfView = new Range(Options.minFieldOfView.get(), Options.maxFieldOfView.get()).random();
        }
        else if (Math.random() <= Options.fieldOfViewMutationRate.get()) {
        	this.fieldOfView += new Range(Options.minFieldOfView.get(), Options.maxFieldOfView.get()).mutation(Options.mutationFraction.get());
        	this.fieldOfView = new Range(0, 2 * Math.PI).check(this.fieldOfView);
        }
    }
    
    public List<SensorGene> mate(SensorGene partner) {
    	return (List<SensorGene>) new Genetics().mate(this, partner);
    }
    
    public List<String> getInitiateProperties() {
		List<String> properties = new ArrayList<>();
		properties.add("viewDistance");
		properties.add("fieldOfView");
		return properties;
	}
	
	public SensorGene initiate(List<Double> properties){
		return new SensorGene(properties.get(0), properties.get(1));
	}
	
}
