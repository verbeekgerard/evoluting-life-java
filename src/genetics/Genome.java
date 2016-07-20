package genetics;

import java.util.ArrayList;
import java.util.List;

public class Genome {

	public BrainGene brain;
	public LifeGene life;
	public SensorGene sensor;
	public MovementGene movement;
	
	public Genome(int inputCount, int outputCount){
        brain = new BrainGene(inputCount, outputCount);
        life = new LifeGene();
        sensor = new SensorGene();
        movement = new MovementGene();
	}
	
	public Genome(BrainGene brain, LifeGene life, SensorGene sensor, MovementGene movement){
        brain = new BrainGene(brain.layers);
        life = new LifeGene(life.oldAge, life.nutrition);
        sensor = new SensorGene(sensor.viewDistance, sensor.fieldOfView);
        movement = new MovementGene(movement.angularForce, movement.linearForce);
	}
	
	public void mutate() {
        brain.mutate();
        life.mutate();
        sensor.mutate();
        movement.mutate();
    };

    public Genome clone() {
        return new Genome(this.brain, this.life, this.sensor, this.movement);
    }

    public List<Genome> mate(Genome partner) {
    	List<Genome> children = new ArrayList<>();
    	children.add(this);
    	children.add(partner);
    	
//    	BrainGene brainChildren = this.brain.mate(partner.brain);
//        LifeGene lifeChildren = this.life.mate(partner.life);
//        SensorGene sensorChildren = this.sensor.mate(partner.sensor);
//        MovementGene movementChildren = this.movement.mate(partner.movement);
//
//        List<> children = new ArrayList();
//        for (int i = 0; i < brainChildren.size(); i++) {
//            children.push(new Genome({
//                brain: brainChildren[i].getState(),
//                life: lifeChildren[i].getState(),
//                sensor: sensorChildren[i].getState(),
//                movement: movementChildren[i].getState()
//            }));
//        }
//
//        return children; // TODO
    	return children;
    }
	
}