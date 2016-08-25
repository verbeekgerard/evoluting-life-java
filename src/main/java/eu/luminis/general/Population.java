package eu.luminis.general;

import eu.luminis.entities.Animal;
import eu.luminis.entities.Plant;
import eu.luminis.entities.Position;
import eu.luminis.entities.World;
import eu.luminis.evolution.RouletteWheelSelectionByRank;
import eu.luminis.genetics.Genome;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Population {

	public World world;
	public List<Animal> entities = new CopyOnWriteArrayList<>(); // Slow lost but no exceptions in UI
	public Animal winningEntity;

	public Option populationSize = new Option(16 * 8);

	public Population(World world) {
		this.world = world;

		// Fill our population with eu.luminis.entities
		for (int i = 0; i < populationSize.get(); i++) {
			Genome genome = createGenome();
			Position position = createRandomPosition();
			Animal entity = new Animal(genome, position, world);
			// this.entityCreated.notifyAsync(newAnimal);
			this.entities.add(entity);
		}
	}

	public Genome createGenome() {
		return new Genome(5, 4);
	}

	public Position createRandomPosition() {
		Range range = new Range(-0.98, 0.98);
		double x = world.width/2 + world.width/2 * range.random();
		double y = world.height/2 + world.height/2 * range.random();
		return new Position(x, y, Math.random() * Math.PI * 2);
	}

	// getState: function () {
	// sortSuccess.call(this);
	// var genomeStates = [];
	//
	// for (var i = 0; i < this.eu.luminis.entities.length; i++) {
	// var genome = this.eu.luminis.entities[i].getGenome();
	// genomeStates.push(genome.getState());
	// }
	//
	// return {genomes: genomeStates};
	// },

	// loadState: function (genomeStates) {
	// this.eu.luminis.entities = [];
	// var genomes = genomeStates.genomes;
	//
	// for (var i = 0; i < genomes.length; i++) {
	// var genome = new Genome(undefined, undefined, genomes[i]);
	// var position = createRandomPosition();
	// var entity = new Animal(genome, position);
	// entity.population = this;
	// this.eu.luminis.entities.push( entity );
	// }
	// },

	public void run(List<Plant> plants) {
		Collections.sort(this.entities);
		// Find the best ranking entity
		winningEntity = this.entities.get(0);

		List<Animal> entitiesToRemove = new ArrayList<>();
		for (int i = 0; i < this.entities.size(); i++) {
			Animal entity = this.entities.get(i);
			// TODO to make this fast this should run on separate threads
			entity.run(plants, this.entities);

			// Check entity life cycle and remove dead eu.luminis.entities
			if (!entity.lives()) {
				entitiesToRemove.add(entity);
			}
		}

		for (int i = 0; i < entitiesToRemove.size() / 2; i++) {
			List<Animal> parents = selectParents();
			produceChildren(parents);
		}

		for (Animal entityToRemove : entitiesToRemove) {
			this.entities.remove(entityToRemove);
		}

		while (this.entities.size() <= populationSize.get() - 2) {
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
			Animal newAnimal = new Animal(child, position, world);
			this.entities.add(newAnimal);
			General.getInstance().broadcast(EventType.NEW_ANIMAL, newAnimal);
		}
	}

	public List<Animal> selectParents() {
		List<Animal> parents = new ArrayList<>();

		for (int i = 0; i < 2; i++) {
			Animal winningEntity = new RouletteWheelSelectionByRank().select(this.entities);
			parents.add(winningEntity);
		}

		return parents;
	}
}