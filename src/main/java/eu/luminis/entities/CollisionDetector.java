package eu.luminis.entities;

import eu.luminis.robots.Obstacle;

public class CollisionDetector {
    public boolean colliding(Obstacle a, Obstacle b) {
        if (a == b)
            return false;

        // Calculate the squared distances between the centres
        double dx2 = (a.getPosition().x - b.getPosition().x); dx2 *= dx2;
        double dy2 = (a.getPosition().y - b.getPosition().y); dy2 *= dy2;

        // Calculate the squared sum of the radii
        double s2 = a.getSize()/2 + b.getSize()/2; s2 *= s2;

        // Collide when the size circles touch
        return dx2 + dy2 <= s2;
    }
}