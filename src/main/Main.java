package main;

import entities.World;

public class Main {
	
	private FoodSupply foodSupply;
	private Population population;
	

	public Main() {
//      var canvas = worldCanvas;
//      var context = canvas.getContext('2d');
//	
		World world = new World();
        this.foodSupply = new FoodSupply(world);
        this.population = new Population(world);

//        // Import & Export of genes
//        var contentPersister = new ContentPersister(function () {
//          return population.getState();
//        });
//
//        var contentLoader = new ContentLoader(function (genomeStates) {
//          population.loadState(genomeStates);
//        });

        // Create printers
//        var getIteration = function () {
//          return iteration;
//        };
//
//        var getPopulationNumbers = function () {
//          return population.runNumbers;
//        };

//        var plantPrinter = new PlantPrinter();
//        foodSupply.entityRunNotifier.addObserver(plantPrinter.entityRunObserver);

//        var animalPrinter = new AnimalPrinter(population);
//        population.entityRunNotifier.addObserver(animalPrinter.entityRunObserver);

//        var statsTable = document.getElementById('stats-tbody');
//        var statsCollector = new StatsCollector(getIteration, getPopulationNumbers);
//        var statsPrinter = new StatsPrinter(statsTable, statsCollector);

//        var graphElement = document.getElementById('graph');
//        var fitnessGraph = new FitnessGraph(graphElement, getPopulationNumbers);
//
//        var countersElement = document.getElementById('time');
//        var countersCollector = new CountersCollector(getIteration, getPopulationNumbers);
//        var countersPrinter = new CountersPrinter(countersElement, countersCollector);

//        var diagramElement = document.getElementById('diagram');
//        var brainDiagram = new BrainDiagram(diagramElement);
//        population.newWinnerNotifier.addObserver(brainDiagram.newWinnerObserver);

//        var statsIntervalId = statsPrinter.periodicalReport();
//        var graphIntervalId = fitnessGraph.periodicalReport();

        // Register observers
//        var entityCreatedObserver = new Observer(function (entity) {
//          registerObservers(statsCollector, entity);
//        });

//        population.entityCreated.addObserver(entityCreatedObserver);
//        population.entityCreated.addObserver(countersCollector.bornObserver);

        
	}
	
	private int iteration = 0;
	
	public void startMainLoop(){
		// Loop ten times for now
		for (int i=0;i<10000;i++){
			mainLoop();
		}
	}
	
	public void mainLoop(){

          // Keep track of our iteration count
          iteration++;

          System.out.println("Iteration: " + iteration);
          // Clear the drawing area
//          context.clearRect(0, 0, canvas.width, canvas.height);

          // Run a tick of foodSupply life cycle
          foodSupply.run();

          // Run a tick of population life cycle
          population.run(foodSupply.plants);

          // Print
//          countersPrinter.drawCounters();

	}
      
//      function registerObservers(collector, entity) {
//          entity.wanderedNotifier.addObserver(collector.wanderedObserver);
//          entity.starvedNotifier.addObserver(collector.starvedObserver);
//          entity.diedOfAgeNotifier.addObserver(collector.diedOfAgeObserver);
//          entity.eatNotifier.addObserver(collector.eatObserver);
//          entity.consumedNotifier.addObserver(collector.consumedObserver);
//        }

	
}