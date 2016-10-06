package eu.luminis.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.luminis.entities.Animal;
import eu.luminis.entities.Plant;
import eu.luminis.entities.Position;
import eu.luminis.entities.World;
import eu.luminis.evolution.RouletteWheelSelectionByRank;
import eu.luminis.genetics.Genome;
import eu.luminis.util.Range;
import java.lang.reflect.Type;

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
	
	public void importPopulation(String json) {

		this.entities = new CopyOnWriteArrayList<>();
		
		Type listType = new TypeToken<ArrayList<Genome>>(){}.getType();
		List<Genome> genomes = new Gson().fromJson(json, listType);
		
		for (int i = 0; i < genomes.size(); i++) {
			Animal animal = new Animal(genomes.get(i), createRandomPosition(), world);
			this.entities.add(animal);
		}
	}
	
	public String exportPopulation() {
		ArrayList<Genome> genomes = new ArrayList<>();
		for (int i = 0; i < this.entities.size(); i++) {
			genomes.add(this.entities.get(i).genome);
		}
		return new Gson().toJson(genomes);
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

	public List<Animal> getEntities(){
		return this.entities;
	}
}