package sensors;

import java.util.ArrayList;
import java.util.List;

import entities.Animal;
import entities.Plant;

public class Targets {
	
	public List<FoodVector> plants = new ArrayList<>();
	public List<FoodVector> animals = new ArrayList<>();;
	public double wallDistance;
	
//	plants: findOrganisms.call(this, plants || []),
//    animals: findOrganisms.call(this, animals || []),
//    wall: {
//      distance: wallDistance.call(this)
//    }
}
