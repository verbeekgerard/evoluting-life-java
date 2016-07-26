package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import entities.Animal;
import entities.Plant;
import entities.Position;
import entities.World;
import evolution.RouletteWheelSelectionByRank;
import genetics.Genome;

public class Population {
	
    public World world;
	public List<Animal> entities = new CopyOnWriteArrayList<>(); // Slow lost but no exceptions in UI
	public Animal winningEntity;
	
	public Option populationSize = new Option(16*8);
	
	public Population(World world){
		this.world = world;
		
		// Fill our population with entities
      for (int i = 0; i < populationSize.get(); i++) {
          Genome genome = createGenome();
          Position position = createRandomPosition();
          Animal entity = new Animal(genome, position, world);
          // this.entityCreated.notifyAsync(newAnimal);
          this.entities.add(entity);
      }
	}
    
    public Genome createGenome() {
        return new Genome(7, 4);
    }
    
    public Position createRandomPosition() {
    	return new Position(world.width * Math.random(), world.height * Math.random(), Math.random() * Math.PI * 2);
    }
    
//    getState: function () {
//          sortSuccess.call(this);
//          var genomeStates = [];
//
//          for (var i = 0; i < this.entities.length; i++) {
//              var genome = this.entities[i].getGenome();
//              genomeStates.push(genome.getState());
//          }
//
//          return {genomes: genomeStates};
//      },

//      loadState: function (genomeStates) {
//          this.entities = [];
//          var genomes = genomeStates.genomes;
//
//          for (var i = 0; i < genomes.length; i++) {
//              var genome = new Genome(undefined, undefined, genomes[i]);
//              var position = createRandomPosition();
//              var entity = new Animal(genome, position);
//              entity.population = this;
//              this.entities.push( entity );
//          }
//      },

      public void run(List<Plant> plants) {

    	  Collections.sort(this.entities);
          // Find the best ranking entity
          winningEntity = this.entities.get(0);

    	  List<Animal> entitiesToRemove = new ArrayList<>();
          for (int i = 0; i < this.entities.size(); i++) {
        	  Animal entity = this.entities.get(i);
        	  // TODO to make this fast this should run on seperate threads
              entity.run(plants, this.entities);

              // Check entity lifecycle and remove dead entities
              if (!entity.lives()) {
            	  entitiesToRemove.add(entity);
              }
          }
          
          for (int i=0; i< entitiesToRemove.size()/2; i++) {
        	  List<Animal> parents = selectParents();
              produceChildren(parents);
          }   

          for (Animal entityToRemove : entitiesToRemove){
        	  this.entities.remove(entityToRemove);
          }

          while (this.entities.size() <= populationSize.get() -2) {
        	  List<Animal> parents = selectParents();
              produceChildren(parents);
          }   
      }
      
    public void produceChildren(List<Animal> parents) {
	    List<Genome> children = parents.get(0).genome.mate(parents.get(1).genome);
	    for (Genome child : children) {
	        child.mutate();
	
	        // Spawn a new entity from it
	        Position position = createRandomPosition();
	        Animal newAnimal = new Animal( child, position, world );
	        this.entities.add(newAnimal);
	        Main.getInstance().broadcast(EventType.NEW_ANIMAL, newAnimal);
	    }
	}
      
    public List<Animal> selectParents() {
	    List<Animal> parents = new ArrayList<>();
	    
	    for (int i=0; i<2; i++) {
	        Animal winningEntity = new RouletteWheelSelectionByRank().select(this.entities);
	        parents.add(winningEntity);
	    }
	
	    return parents;
    }
    
    
}
