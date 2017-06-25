package eu.luminis.robots.sim;

class CollisionDetector {
    public boolean colliding(SimObstacle a, SimObstacle b) {
        if (a == b)
            return false;

        // Calculate the squared distances between the centres
        double dx2 = (a.getPosition().getX() - b.getPosition().getX()); dx2 *= dx2;
        double dy2 = (a.getPosition().getY() - b.getPosition().getY()); dy2 *= dy2;

        // Calculate the squared sum of the radii
        double s2 = a.getSize()/2 + b.getSize()/2; s2 *= s2;

        // Collide when the size circles touch
        return dx2 + dy2 <= s2;
    }
}