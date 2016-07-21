package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import entities.Animal;
import entities.Plant;
import entities.Position;
import entities.World;
import genetics.Genome;
import main.FoodSupply;
import main.Main;
import main.Population;

public class Canvas extends JPanel implements Observer {

	private FoodSupply foodSupply;
	private Population population;
	
	public Canvas(FoodSupply foodSupply, Population population) {
		this.foodSupply = foodSupply;
		this.population = population;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
	    for (Plant plant : foodSupply.plants) {
	    	drawPlant(plant, g);
	    }
//	    synchronized(population.entities) {
//		    for (Iterator<Animal> it = population.entities.iterator(); it.hasNext(); ) {
//		    	Animal animal = it.next();
//		    	
//		    }
//	    }
	    
	    for (Animal animal : population.entities) {
	    	drawAnimal(animal, g);
	    }
  	}
	
	public void drawAnimal(Animal animal, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.DARK_GRAY);

        g2.fillOval(
        		new Double(animal.position.x).intValue(), 
				new Double(animal.position.y).intValue(), 
				new Double(animal.getHealth()).intValue(), 
				new Double(animal.getHealth()).intValue());

    }
	
	public void drawPlant(Plant plant, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
//		g2.drawArc(35, 45, 75, 95, 0, 90);
//        context.beginPath();
		
//        context.lineWidth = 2;
        // context.lineWidth = 2 + Math.floor(plant.life.age / (plant.life.oldAge / 3));

        double r = Math.floor( 255 * (1 - plant.getNutritionN()) );
//        var color = "rgb(" + r + ",220,0)"; //187

        if (plant.nutrition < 0) {
        	
        	g2.setColor(Color.RED);
//        	g2.set
//            context.strokeStyle = color;
//            context.fillStyle = "#000";
        }
        else {
//            context.strokeStyle = "#000";
//            context.fillStyle = color;
        	g2.setColor(Color.BLUE);
        }

        
//        g2.drawArc(
//        		new Double(plant.position.x).intValue(), 
//        		new Double(plant.position.y).intValue(), 
//        		new Double(plant.getSize()).intValue(), 
//				new Double(plant.getSize()).intValue(), 
//				new Double(Math.PI*2).intValue(), 
//				new Double(Math.PI*2).intValue()
//        );
        g2.fillOval(
        		new Double(plant.position.x).intValue(), 
				new Double(plant.position.y).intValue(), 
				new Double(plant.getSize()).intValue(), 
				new Double(plant.getSize()).intValue());
//        context.arc( plant.position.x, plant.position.x, plant.getSize(), 0, Math.PI*2, true );

//        context.stposePath();
    }
	
//  	public static void main(String args[]) {
//	    JFrame frame = new JFrame("Evoluting-life-java");
//	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	    frame.add(new Canvas());
//	    frame.setSize(500, 500);
//	    frame.setVisible(true);
//	    frame.setResizable(false);
//  	}
}
