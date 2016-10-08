package eu.luminis.entities;

public class CollisionDetector {
    public boolean colliding(Organism a, Organism b) {
        if (a == b)
            return false;

        // Use formula for a circle to find food
        double dx2 = (a.position.x - b.position.x); dx2 *= dx2;
        double dy2 = (a.position.y - b.position.y); dy2 *= dy2;
        double s2 = a.getSize()/2 + b.getSize()/2; s2 *= s2;

        // Collide when the size circles touch
        return dx2 + dy2 < s2;
    }
}
