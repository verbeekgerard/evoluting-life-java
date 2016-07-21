package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
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
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, population.world.width, population.world.height);
		
	    for (Plant plant : foodSupply.plants) {
	    	drawPlant(plant, g);
	    }
	    
	    for (Animal animal : population.entities) {
	    	drawAnimal(animal, population.winningEntity, g);
	    }
  	}
	
	public void drawAnimal(Animal animal, Animal bestAnimal, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.RED);
		if (bestAnimal == animal) {
			g2.setColor(Color.cyan);
		}

        double entitySize = animal.getSize();
        Position p = animal.position;
        double ba = p.a + Math.PI; // Find the angle 180deg of entity

        // Find left back triangle point
        double WEDGE_ANGLE = Math.PI * 0.25;
        double lx = Math.cos( ba + ( WEDGE_ANGLE / 2 ) ) * entitySize;
        double ly = Math.sin( ba + ( WEDGE_ANGLE / 2 ) ) * entitySize;

        // Find right back triangle point
        double rx = Math.cos( ba - ( WEDGE_ANGLE / 2 ) ) * entitySize;
        double ry = Math.sin( ba - ( WEDGE_ANGLE / 2 ) ) * entitySize;

        // Find the curve control point
        double cx = Math.cos( ba ) * entitySize * 0.3;
        double cy = Math.sin( ba ) * entitySize * 0.3;

        // Color code entity based on food eaten compared to most successful
//        var currentBest = currentBestAnimal.rank();
//        var g = Math.floor( 0.7 * 255 * (1 - (currentBest === 0 ? 0 : animal.life.rank() / currentBest )));
//
//        context.fillStyle = "rgb(255," + g + ",0)";
//        context.strokeStyle = "#000";
//        context.lineWidth = 2 + Math.floor(animal.age / (animal.oldAge / 5));

        // Draw the triangle
        
        GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        polygon.moveTo(p.x, p.y);
        polygon.lineTo( p.x + lx, p.y + ly );
        polygon.quadTo( p.x + cx, p.y + cy, p.x + rx, p.y + ry );
        polygon.closePath();

        g2.fill(polygon);
        g2.setColor(Color.BLACK);
        g2.draw(polygon);

    }
	
	public void drawPlant(Plant plant, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
//		g2.drawArc(35, 45, 75, 95, 0, 90);
//        context.beginPath();
		
//        context.lineWidth = 2;
        // context.lineWidth = 2 + Math.floor(plant.life.age / (plant.life.oldAge / 3));

        double r = Math.floor( 255 * (1 - plant.getNutritionN()) );
//        var color = "rgb(" + r + ",220,0)"; //187

//        if (plant.nutrition < 0) {
//        	
//        	g2.setColor(Color.RED);
////        	g2.set
////            context.strokeStyle = color;
////            context.fillStyle = "#000";
//        }
//        else {
//            context.strokeStyle = "#000";
//            context.fillStyle = color;
        	g2.setColor(Color.GREEN);
//        }

        
//        g2.drawArc(
//        		new Double(plant.position.x).intValue(), 
//        		new Double(plant.position.y).intValue(), 
//        		new Double(plant.getSize()).intValue(), 
//				new Double(plant.getSize()).intValue(), 
//				new Double(Math.PI*2).intValue(), 
//				new Double(Math.PI*2).intValue()
//        );
//        	g2.drawArc(x, y, width, height, startAngle, arcAngle);
        g2.fillOval(
        		new Double(plant.position.x).intValue(), 
				new Double(plant.position.y).intValue(), 
				new Double(plant.getSize()).intValue(), 
				new Double(plant.getSize()).intValue());
//        context.arc( plant.position.x, plant.position.x, plant.getSize(), 0, Math.PI*2, true );
        
        g2.setColor(Color.BLACK);
        g2.drawOval(
        		new Double(plant.position.x).intValue(), 
				new Double(plant.position.y).intValue(), 
				new Double(plant.getSize()).intValue(), 
				new Double(plant.getSize()).intValue());
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
