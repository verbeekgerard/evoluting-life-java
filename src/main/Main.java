package main;

import java.util.Observable;

import entities.World;

public class Main extends Observable {
	
	public FoodSupply foodSupply;
	public Population population;
	public World world;
	
	
	private static Main singleton = new Main();
	
	public static Main getInstance() {
      return singleton;
	}

	private Main() {
//	
		this.world = new World();
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

	}
	
	private int iteration = 0;
	
	public void startMainLoop(){
		
		while (true){
			mainLoop(); /*
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} */
		}
	}
	
	public void mainLoop(){

          // Keep track of our iteration count
          iteration++;
          
          // Run a tick of foodSupply life cycle
          foodSupply.run();

          // Run a tick of population life cycle
          population.run(foodSupply.plants);

          broadcast(EventType.CYCLE_END, iteration);
	}
	
	public void broadcast(EventType eventType, Object value) {
		setChanged();
        super.notifyObservers(new Event(eventType, value));
	}
	
}