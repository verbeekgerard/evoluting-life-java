package eu.luminis.genetics;

import eu.luminis.general.Options;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

public class SensorGene extends Gene {
    private double viewDistance;
    private double fieldOfView;
    
    public SensorGene(double viewDistance, double fieldOfView){
    	this.viewDistance = viewDistance;
        this.fieldOfView = fieldOfView;
    }
    
    public SensorGene() {
        initializeViewDistance();
        initializeFieldOfView();
    }

    public double getViewDistance() {
        return viewDistance;
    }

    public double getFieldOfView() {
        return fieldOfView;
    }

    public void mutate() {
        if (Math.random() <= Options.viewDistanceReplacementRate.get()) {
            initializeViewDistance();
        }
        else if (Math.random() <= Options.viewDistanceMutationRate.get()) {
        	this.viewDistance += new Range(Options.minViewDistance.get(), Options.maxViewDistance.get()).mutation(Options.mutationFraction.get());
        }

        if (Math.random() <= Options.fieldOfViewReplacementRate.get()) {
            initializeFieldOfView();
        }
        else if (Math.random() <= Options.fieldOfViewMutationRate.get()) {
        	this.fieldOfView += new Range(Options.minFieldOfView.get(), Options.maxFieldOfView.get()).mutation(Options.mutationFraction.get());
        	this.fieldOfView = new Range(0, 2 * Math.PI).assureBounds(this.fieldOfView);
        }
    }
    
    public List<SensorGene> mate(SensorGene partner) {
    	return (List<SensorGene>) new Genetics().mate(this, partner);
    }

    @Override
    public List<Double> getInitiateProperties() {
        List<Double> list = new ArrayList<>();
        list.add(this.viewDistance);
        list.add(this.fieldOfView);

        return list;
    }

    @Override
    public Gene initiate(List<Double> properties) {
        return new SensorGene(properties.get(0), properties.get(1));
    }

    private void initializeViewDistance() {
        this.viewDistance = new Range(Options.minViewDistance.get(), Options.maxViewDistance.get()).random();
    }

    private void initializeFieldOfView() {
        this.fieldOfView = new Range(Options.minFieldOfView.get(), Options.maxFieldOfView.get()).random();
    }
}