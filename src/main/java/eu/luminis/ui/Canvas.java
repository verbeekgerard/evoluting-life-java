package eu.luminis.ui;

import eu.luminis.geometry.Position;
import eu.luminis.events.Event;
import eu.luminis.events.EventType;
import eu.luminis.Options;
import eu.luminis.robots.sim.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.Observable;
import java.util.Observer;

public class Canvas extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;

    private StationaryObstaclePopulation obstaclePopulation;
	private SimRobotPopulation robotPopulation;
	private SimWorld world;

    private FrameLimiter frameLimiter = new FrameLimiter(60);

	public Canvas(SimWorld world) {
        JFrame frame = new JFrame("Evoluting-life-java");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(this);
		frame.setSize(new Double(world.getWidth()).intValue(), new Double(world.getHeight() + 20).intValue());
		frame.setVisible(true);
		frame.setResizable(false);
		
		this.obstaclePopulation = world.getObstaclePopulation();
		this.robotPopulation = world.getRobotPopulation();
        this.world = world;
	}

	@Override
	public void update(Observable o, Object arg) {
		Event event = (Event) arg;
		if (event.type.equals(EventType.CYCLE_END) && this.isShowing() && frameLimiter.isNewFrame()) {
            this.repaint();
		}
	}

    @Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, world.getWidth(), world.getHeight());

		drawFieldOfView(robotPopulation.getWinningEntity(), g);

		for (SimObstacle obstacle : obstaclePopulation.getAllRoundObstacles()) {
			drawObstacle(obstacle, g);
		}

		for (SimRobot robot : robotPopulation.getAllRobots()) {
            //drawFieldOfView(robot, g);
			drawCollisionBody(robot, g);
			drawRobot(robot, robotPopulation.getWinningEntity(), g);
		}
	}

	private void drawFieldOfView(SimRobot robot, Graphics g) {
		if (robotPopulation.getWinningEntity() == null) {
			return;
		}

		Graphics2D g2 = (Graphics2D) g;

		Position robotPosition = robot.getPosition();
		double servoAngle = robot.getServo().getAngle();
		double fieldOfView = Math.PI / 30;
		double viewDistance = Options.maxViewDistance.get();

		Color c = new Color(.9f, .9f, .9f, .1f);
		g2.setColor(c);
		g2.fillArc(new Double(robotPosition.x - (viewDistance / 2)).intValue(),
                new Double(robotPosition.y - (viewDistance / 2)).intValue(),
                new Double(viewDistance).intValue(),
                new Double(viewDistance).intValue(),
                new Double(Math.toDegrees(-1 * (robotPosition.a + servoAngle) + fieldOfView / 2)).intValue(),
                new Double(-1 * Math.toDegrees(fieldOfView)).intValue());
	}

	private void drawRobot(SimRobot robot, SimRobot bestRobot, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		double entitySize = robot.getSize();
		Position animalPosition = robot.getPosition();
		double ba = animalPosition.a + Math.PI; // Find the angle 180deg of entity

		// Find left back triangle point
        double WEDGE_ANGLE = Math.PI * 0.25;
        double lx = Math.cos(ba + (WEDGE_ANGLE / 2)) * entitySize;
		double ly = Math.sin(ba + (WEDGE_ANGLE / 2)) * entitySize;

		// Find right back triangle point
		double rx = Math.cos(ba - (WEDGE_ANGLE / 2)) * entitySize;
		double ry = Math.sin(ba - (WEDGE_ANGLE / 2)) * entitySize;

		// Find the curve control point
		double cx = Math.cos(ba) * entitySize * 0.3;
		double cy = Math.sin(ba) * entitySize * 0.3;

		int robotAge = robot.getAgeInformation().getAge();
		int robotOldAge = robot.getAgeInformation().getOldAge();
		g2.setStroke(new BasicStroke((float) (2 + Math.floor(5 * robotAge / robotOldAge))));

		// Draw the triangle
		GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		polygon.moveTo(animalPosition.x, animalPosition.y);
		polygon.lineTo(animalPosition.x + lx, animalPosition.y + ly);
		polygon.quadTo(animalPosition.x + cx, animalPosition.y + cy, animalPosition.x + rx, animalPosition.y + ry);
		polygon.closePath();

		if (robotAge > 30)
			g2.setColor(Color.BLACK);
		else
			g2.setColor(Color.WHITE);
			
		g2.draw(polygon);

		// Color code entity based on food eaten compared to most successful
		if (bestRobot != null) {
			double currentBest = bestRobot.fitness();
			int green = (int) Math.floor(255 * (1 - (currentBest == 0 ? 0 : robot.fitness() / currentBest)));
			Color color = new Color(255, green > 0 ? green > 255 ? 255 : green : 0, 0);
			g2.setColor(color);
		}
		g2.fill(polygon);
	}
	
	private void drawCollisionBody(SimRobot robot, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		float alpha = robot.isColliding() ? .9f : .1f ;
        BasicStroke stroke = robot.isColliding() ? new BasicStroke(1.5f) : new BasicStroke(0.5f) ;

		Color color = new Color(1.0f, 1.0f, 1.0f, alpha);
		g2.setColor(color);
		g2.setStroke(stroke);
		g2.drawOval(
				new Double(robot.getPosition().x).intValue() - new Double(robot.getSize()/2).intValue(),
				new Double(robot.getPosition().y).intValue() - new Double(robot.getSize()/2).intValue(),
				new Double(robot.getSize()).intValue(), new Double(robot.getSize()).intValue());
	}
	
	private void drawObstacle(SimObstacle obstacle, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.GREEN);
		g2.fillOval(
				new Double(obstacle.getPosition().x).intValue() - new Double(obstacle.getSize()/2).intValue(),
				new Double(obstacle.getPosition().y).intValue() - new Double(obstacle.getSize()/2).intValue(),
				new Double(obstacle.getSize()).intValue(), new Double(obstacle.getSize()).intValue());

		g2.setColor(Color.BLACK);
		g2.drawOval(
				new Double(obstacle.getPosition().x).intValue() - new Double(obstacle.getSize()/2).intValue(),
				new Double(obstacle.getPosition().y).intValue() - new Double(obstacle.getSize()/2).intValue(),
				new Double(obstacle.getSize()).intValue(), new Double(obstacle.getSize()).intValue());
	}
}