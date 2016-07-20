package main;

import java.util.ArrayList;
import java.util.List;

import entities.Animal;
import entities.Plant;
import entities.Position;
import entities.World;
import genetics.Genome;

public class Population {
	public int foodTotal = 0;
	public int ageTotal = 0;
	public int healthTotal = 0;
	public int size = 0;
	public int fittest = 0;
	
    public World world;
	private List<Animal> entities = new ArrayList<>();;
	
	public Option populationSize = new Option(16*8);
	
	public Population(World world){
		this.world = world;
		
		// Fill our population with entities
      for (int i = 0; i < populationSize.get(); i++) {
          Genome genome = createGenome();
          Position position = createRandomPosition();
          Animal entity = new Animal(genome, position, world);
          // this.entityCreated.notifyAsync(newAnimal);
//          entity.population = this;
          this.entities.add(entity);
      }
		
	}
    
    public Genome createGenome() {
        return new Genome(12, 5);
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
//          var tempNumbers = cloning.shallowClone(initialRunNumbers);
//          sortSuccess.call(this); TODO sorting
    	  List<Animal> entitiesToRemove = new ArrayList<>();
          for (int i = 0; i < this.entities.size(); i++) {
        	  Animal entity = this.entities.get(i);

              entity.run(plants, this.entities);
//              this.entityRunNotifier.notify(entity);

//              recordAnimalNumbers(tempNumbers, entity);

              // Check entity lifecycle and remove dead entities
              if (!entity.lives()){
            	  entitiesToRemove.add(entity);
              };

          }
          
          for (Animal entityToRemove : entitiesToRemove){
        	  System.out.println("Removed dead entity");
        	  this.entities.remove(entityToRemove);
          }

          // Find the best ranking entity
//          findBest.call(this);

//          recordPopulationNumbers(tempNumbers, this);
//          cloning.shallowMixin(tempNumbers, this.runNumbers);

//          var options = new Options();
          if (this.entities.size() <= populationSize.get() -2) {
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
//	        newAnimal.population = this;
	        this.entities.add(newAnimal);
//	        this.entityCreated.notifyAsync(newAnimal);
	    }
	}
      
    public List<Animal> selectParents() {
	    List<Animal> parents = new ArrayList<>();
	    
	    parents.add(this.entities.get(0)); // TODO implement
	    parents.add(this.entities.get(1));
	    
//	    for (var i=0; i<2; i++) {
//	        var winningEntity = this.fitnessSelection.select(this.entities.length);
//	        var winningGenome = winningEntity.getGenome();
//	
//	        parents.push(winningGenome);
//	    }
	
	    return parents;
    }
    
}


//            var initialRunNumbers = {
//                
//            };

//            function recordAnimalNumbers(numbers, entity) {
//                numbers.foodTotal += entity.life.consumed;
//                numbers.ageTotal += entity.life.age;
//                numbers.healthTotal += entity.life.health();
//            }
//
//            function recordPopulationNumbers(numbers, population) {
//                numbers.size = population.entities.length;
//                numbers.fittest = population.entities[0].life.health();
//            }
//
//            var Self = function () {
//                var options = new Options();
//
//                this.runNumbers = cloning.shallowClone(initialRunNumbers);
//                this.entities = [];
//                this.winningAnimal = {};
//                this.entityCreated = new Subject();
//                this.newWinnerNotifier = new Subject();
//                this.entityRunNotifier = new Subject();
//
//                
//
//                var that = this;
//                this.fitnessSelection = new Selection (
//                    function (index) {
//                        var entity = that.entities[index];
//                        return new Score(entity, entity.life.rank());
//                    }
//                );
//            };
//
//            Self.prototype = (function () {
//                function sortSuccess() {
//                    /*jshint validthis: true */
//                    this.entities.sort( function (a, b) {
//                        return b.life.rank() - a.life.rank();
//                    });
//                }
//
//                var findBest = function () {
//                    var winner = this.entities[0];
//
//                    if ( winner !== this.winningAnimal ) {
//                        this.winningAnimal = winner;
//                        var winnerBrain = winner.brain.getLayers(); // Too much knowledge of the brain here?
//                        this.newWinnerNotifier.notifyAsync(winnerBrain);
//                    }
//                };
//

//

//
//                return {
//                    
//                };
//            }) ();
//
//            return Self;
//        }
//    );
