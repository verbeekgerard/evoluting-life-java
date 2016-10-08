package eu.luminis.ui;

import eu.luminis.entities.Animal;
import eu.luminis.entities.Plant;
import eu.luminis.entities.Position;
import eu.luminis.entities.World;
import eu.luminis.general.Event;
import eu.luminis.general.EventType;
import eu.luminis.general.FoodSupply;
import eu.luminis.general.Population;
import eu.luminis.sensors.Eyes;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.Observable;
import java.util.Observer;

public class Canvas extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
    private final double WEDGE_ANGLE = Math.PI * 0.25;

	private FoodSupply foodSupply;
	private Population population;
	private World world;

	public Canvas(FoodSupply foodSupply, Population population, World world) {
        JFrame frame = new JFrame("Evoluting-life-java");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.setSize(new Double(world.getWidth()).intValue(), new Double(world.getHeight() + 20).intValue());
		frame.setVisible(true);
		frame.setResizable(false);
		
		this.foodSupply = foodSupply;
		this.population = population;
        this.world = world;
	}

	@Override
	public void update(Observable o, Object arg) {
		Event event = (Event) arg;
		if (event.type.equals(EventType.CYCLE_END)) {
			this.repaint();
		}
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, world.getWidth(), world.getHeight());

		drawFieldOfView(population.getWinningEntity(), g);

		for (Plant plant : foodSupply.getPlants()) {
			drawPlant(plant, g);
		}

		for (Animal animal : population.getEntities()) {
			drawCollisionBody(animal, g);
			drawAnimal(animal, population.getWinningEntity(), g);
		}
	}

	public void drawFieldOfView(Animal animal, Graphics g) {
		if (population.getWinningEntity() != null) {
			Graphics2D g2 = (Graphics2D) g;

			Position p = animal.position;
			Eyes e = animal.getEyes();

			Color c = new Color(.9f, .9f, .9f, .1f);
			g2.setColor(c);
			g2.fillArc(new Double(p.x - (e.viewDistance / 2)).intValue(),
					new Double(p.y - (e.viewDistance / 2)).intValue(), new Double(e.viewDistance).intValue(),
					new Double(e.viewDistance).intValue(),
					new Double(Math.toDegrees(-1 * p.a + e.fieldOfView / 2)).intValue(),
					new Double(-1 * Math.toDegrees(e.fieldOfView)).intValue());
		}
	}

	public void drawAnimal(Animal animal, Animal bestAnimal, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		double entitySize = animal.getSize();
		Position p = animal.position;
		double ba = p.a + Math.PI; // Find the angle 180deg of entity

		// Find left back triangle point
		double lx = Math.cos(ba + (WEDGE_ANGLE / 2)) * entitySize;
		double ly = Math.sin(ba + (WEDGE_ANGLE / 2)) * entitySize;

		// Find right back triangle point
		double rx = Math.cos(ba - (WEDGE_ANGLE / 2)) * entitySize;
		double ry = Math.sin(ba - (WEDGE_ANGLE / 2)) * entitySize;

		// Find the curve control point
		double cx = Math.cos(ba) * entitySize * 0.3;
		double cy = Math.sin(ba) * entitySize * 0.3;

		g2.setStroke(new BasicStroke((float) (2 + Math.floor(5 * animal.age / animal.getOldAge()))));

		// Draw the triangle
		GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		polygon.moveTo(p.x, p.y);
		polygon.lineTo(p.x + lx, p.y + ly);
		polygon.quadTo(p.x + cx, p.y + cy, p.x + rx, p.y + ry);
		polygon.closePath();

		if (animal.age > 30)
			g2.setColor(Color.BLACK);
		else
			g2.setColor(Color.WHITE);
			
		g2.draw(polygon);

		// Color code entity based on food eaten compared to most successful
		if (bestAnimal != null) {
			double currentBest = bestAnimal.fitness();
			int green = (int) Math.floor(255 * (1 - (currentBest == 0 ? 0 : animal.fitness() / currentBest)));
			Color color = new Color(255, green > 0 ? green > 255 ? 255 : green : 0, 0);
			g2.setColor(color);
		}
		g2.fill(polygon);
	}
	
	public void drawCollisionBody(Animal animal, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		Color c = new Color(1.0f, 1.0f, 1.0f, .1f);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(0.5f));
		g2.drawOval(
				new Double(animal.position.x).intValue() - new Double(animal.getSize()/2).intValue(), 
				new Double(animal.position.y).intValue() - new Double(animal.getSize()/2).intValue(),
				new Double(animal.getSize()).intValue(), new Double(animal.getSize()).intValue());
	}
	
	public void drawPlant(Plant plant, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.GREEN);
		g2.fillOval(
				new Double(plant.position.x).intValue() - new Double(plant.getSize()/2).intValue(), 
				new Double(plant.position.y).intValue() - new Double(plant.getSize()/2).intValue(),
				new Double(plant.getSize()).intValue(), new Double(plant.getSize()).intValue());

		g2.setColor(Color.BLACK);
		g2.drawOval(
				new Double(plant.position.x).intValue() - new Double(plant.getSize()/2).intValue(),
				new Double(plant.position.y).intValue() - new Double(plant.getSize()/2).intValue(),
				new Double(plant.getSize()).intValue(), new Double(plant.getSize()).intValue());
	}
}