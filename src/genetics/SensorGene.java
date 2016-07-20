package genetics;

import main.Option;
import util.Range;

public class SensorGene extends Gene {

	
	public Option minViewDistance = new Option(16 * 1);
	public Option maxViewDistance = new Option(16 * 14);
	public Option minFieldOfView = new Option(Math.PI / 32);
	public Option maxFieldOfView = new Option(Math.PI);

	public Option viewDistanceMutationRate = new Option(0.1);
	public Option fieldOfViewMutationRate = new Option(0.1);
	public Option mutationFraction = new Option(0.001);
	
    public double viewDistance;
    public double fieldOfView;
    
    public SensorGene(double viewDistance, double fieldOfView){
    	this.viewDistance = viewDistance;
        this.fieldOfView = fieldOfView;
    }
    
    public SensorGene(){
        this.viewDistance = new Range(minViewDistance.get(), maxViewDistance.get()).random();
        this.fieldOfView = new Range(minFieldOfView.get(), maxFieldOfView.get()).random();
    }
    
    public SensorGene clone(){
    	return new SensorGene(this.viewDistance, this.fieldOfView);
    }
    
    public void mutate() {

        if (Math.random() <= viewDistanceMutationRate.get()) {
          this.viewDistance += new Range(minViewDistance.get(), maxViewDistance.get()).mutation(mutationFraction.get());
        }

        if (Math.random() <= fieldOfViewMutationRate.get()) {
          this.fieldOfView += new Range(minFieldOfView.get(), maxFieldOfView.get()).mutation(mutationFraction.get());
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
