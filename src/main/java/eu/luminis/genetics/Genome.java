package eu.luminis.genetics;

import java.util.ArrayList;
import java.util.List;

public class Genome {

	public BrainGene brain;
	public LifeGene life;
	public SensorGene sensor;
	public MovementGene movement;
	
	public Genome(int inputCount, int outputCount){
		this.brain = new BrainGene(inputCount, outputCount);
		this.life = new LifeGene();
		this.sensor = new SensorGene();
		this.movement = new MovementGene();
	}
	
	public Genome(BrainGene brain, LifeGene life, SensorGene sensor, MovementGene movement){
        this.brain = new BrainGene(brain.layers);
        this.life = new LifeGene(life.oldAge, life.nutrition);
        this.sensor = new SensorGene(sensor.viewDistance, sensor.fieldOfView);
        this.movement = new MovementGene(movement.angularForce, movement.linearForce);
	}
	
	public void mutate() {
		this.brain.mutate();
		this.life.mutate();
		this.sensor.mutate();
		this.movement.mutate();
    }

    public List<Genome> mate(Genome partner) {

    	List<Genome> children = new ArrayList<>();

    	List<BrainGene> brainChildren = this.brain.mate(partner.brain);
    	List<LifeGene> lifeChildren = this.life.mate(partner.life);
    	List<SensorGene> sensorChildren = this.sensor.mate(partner.sensor);
        List<MovementGene> movementChildren = this.movement.mate(partner.movement);
   
        for (int i = 0; i < brainChildren.size(); i++) { 
            children.add(new Genome(
        		brainChildren.get(i),
        		lifeChildren.get(i),
        		sensorChildren.get(i),
                movementChildren.get(i)
            ));
        }

    	return children;
    }
	
}