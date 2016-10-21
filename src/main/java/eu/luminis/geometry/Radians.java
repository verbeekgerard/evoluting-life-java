package eu.luminis.geometry;

public class Radians {
    public static double getBounded(double angle) {
        double bounded = angle % (2 * Math.PI);
        if (bounded < 0) return bounded + 2 * Math.PI;

        return bounded;
    }

    public static double getRelativeDifference(double reference, double other) {
        double angle = getBounded(other) - getBounded(reference);

        if (angle <= -1 * Math.PI) {
            angle = 2 * Math.PI + angle;
        }
        if (angle > Math.PI) {
            angle = angle - 2 * Math.PI;
        }

        return angle;
    }

    public static double getBoundedDifference(double reference, double other) {
        return getBounded(other - reference);
    }

    public static double getBoundedArcTan(double x, double y) {
        if (x != 0) return getBounded(Math.atan2(y, x));

        if (x == 0 && y > 0) return Math.PI/2;
        if (x == 0 && y < 0) return 1.5 * Math.PI;

        return 0;
    }

    public static double getSmallestDifference(double a, double b) {
        double angle = Math.abs(getBounded(a) - getBounded(b));

        return angle > Math.PI ? 2 * Math.PI - angle : angle;
    }

    public static double getLargestDifference(double a, double b) {
        return 2 * Math.PI - getSmallestDifference(a,b);
    }
}
