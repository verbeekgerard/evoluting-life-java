package eu.luminis.genetics;

import java.util.ArrayList;
import java.util.List;

public class Genome {

	private final NeuralNetworkGene brain;
	private final LifeGene life;
	private final SensorGene sensor;
	private final MovementGene movement;
	
	public Genome(int inputCount, int outputCount){
		this.brain = NeuralNetworkGeneBuilder.create()
			.withInputSize(inputCount)
			.withOuputSize(outputCount)
			.build();
		this.life = new LifeGene();
		this.sensor = new SensorGene();
		this.movement = new MovementGene();
	}
	
	public Genome(NeuralNetworkGene brain, LifeGene life, SensorGene sensor, MovementGene movement){
        this.brain = new NeuralNetworkGene(brain.getLayers());
        this.life = new LifeGene(life.getOldAge());
        this.sensor = new SensorGene(sensor.getViewDistance(), sensor.getFieldOfView());
        this.movement = new MovementGene(movement.getAngularForce(), movement.getLinearForce());
	}
	
	public void mutate() {
		if (Math.random() > 0.2) {
			return;
		}

		this.brain.mutate();
		this.life.mutate();
		this.sensor.mutate();
		this.movement.mutate();
    }

    public List<Genome> mate(Genome partner) {

    	List<Genome> children = new ArrayList<>();

    	List<NeuralNetworkGene> brainChildren = this.brain.mate(partner.brain);
    	LifeGene[] lifeChildren = this.life.mate(partner.life);
    	SensorGene[] sensorChildren = this.sensor.mate(partner.sensor);
        MovementGene[] movementChildren = this.movement.mate(partner.movement);
   
        for (int i = 0; i < brainChildren.size(); i++) { 
            children.add(new Genome(
        		brainChildren.get(i),
        		lifeChildren[i],
        		sensorChildren[i],
                movementChildren[i]
            ));
        }

    	return children;
    }

	public NeuralNetworkGene getBrain() {
		return brain;
	}

	public LifeGene getLife() {
		return life;
	}

	public SensorGene getSensor() {
		return sensor;
	}

	public MovementGene getMovement() {
		return movement;
	}
}
