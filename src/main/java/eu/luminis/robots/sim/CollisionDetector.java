package eu.luminis.robots.sim;

import eu.luminis.geometry.Vector;

class CollisionDetector {
    public boolean colliding(Vector positionA, Vector positionB, double sizeA, double sizeB) {
        if (positionA == positionB)
            return false;

        double d2 = positionA.squaredDistance(positionB);
        double s2 = sizeA / 2 + sizeB / 2; s2 *= s2;

        // Collide when the size circles touch
        return d2 <= s2;
    }
}
