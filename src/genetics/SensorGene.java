package genetics;

import main.Option;
import main.Options;
import util.Range;

public class SensorGene extends Gene {

    public double viewDistance;
    public double fieldOfView;
    
    public SensorGene(double viewDistance, double fieldOfView){
    	this.viewDistance = viewDistance;
        this.fieldOfView = fieldOfView;
    }
    
    public SensorGene(){
        this.viewDistance = new Range(Options.minViewDistance.get(), Options.maxViewDistance.get()).random();
        this.fieldOfView = new Range(Options.minFieldOfView.get(), Options.maxFieldOfView.get()).random();
    }
    
    public SensorGene clone(){
    	return new SensorGene(this.viewDistance, this.fieldOfView);
    }
    
    public void mutate() {

        if (Math.random() <= Options.viewDistanceMutationRate.get()) {
          this.viewDistance += new Range(Options.minViewDistance.get(), Options.maxViewDistance.get()).mutation(Options.mutationFraction.get());
        }

        if (Math.random() <= Options.fieldOfViewMutationRate.get()) {
          this.fieldOfView += new Range(Options.minFieldOfView.get(), Options.maxFieldOfView.get()).mutation(Options.mutationFraction.get());
          this.fieldOfView = new Range(0, 2 * Math.PI).check(this.fieldOfView);
        }
    }
    
    public SensorGene mate(SensorGene partner) {
    	return this.clone();
//        return genetics.mate(this, partner, function(child) {
//          return new Self(child);
//        });
      }
	
}
