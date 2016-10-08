package eu.luminis.genetics;

import eu.luminis.general.Options;
import eu.luminis.util.Range;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorGene extends Gene {
    private double viewDistance;
    private double fieldOfView;
    
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

    @Override
    public Map<String, Double> getInitiateProperties() {
        Map<String, Double> map = new HashMap<>();
        map.put("viewDistance", this.viewDistance);
        map.put("fieldOfView", this.fieldOfView);

        return map;
    }

    @Override
    public Gene initiate(Map<String, Double> properties) {
        return new SensorGene(properties.get("viewDistance"), properties.get("fieldOfView"));
    }

    public double getViewDistance() {
        return viewDistance;
    }

    public double getFieldOfView() {
        return fieldOfView;
    }
}