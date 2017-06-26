package eu.luminis.robots.sim;

class CollisionDetector {
    public boolean colliding(SimObstacle a, SimObstacle b) {
        if (a == b)
            return false;

        double d2 = a.getPosition().squaredDistance(b.getPosition());
        double s2 = a.getSize()/2 + b.getSize()/2; s2 *= s2;

        // Collide when the size circles touch
        return d2 <= s2;
    }
}