package eu.luminis.geometry;

import org.junit.Test;

import static org.junit.Assert.*;

public class RadiansTest {
    // getBounded
    @Test
    public void getBounded_returns_15PI_when_minus_05PI_is_provided() throws Exception {
        double result = Radians.getBounded(-0.5 * Math.PI);
        assertEquals(1.5, result/Math.PI, 0.0001);
    }

    @Test
    public void getBounded_returns_05PI_when_25PI_is_provided() throws Exception {
        double result = Radians.getBounded(2.5 * Math.PI);
        assertEquals(0.5, result/Math.PI, 0.0001);
    }

    @Test
    public void getBounded_returns_19PI_when_minus_01PI_is_provided() throws Exception {
        double result = Radians.getBounded(-0.1 * Math.PI);
        assertEquals(1.9, result/Math.PI, 0.0001);
    }

    @Test
    public void getBounded_returns_PI_when_minus_PI_is_provided() throws Exception {
        double result = Radians.getBounded(-1 * Math.PI);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }
    
    // getSmallestDifference
    @Test
    public void getSmallestDifference_returns_0_when_15PI_and_minus05PI_are_provided() throws Exception {
        double result = Radians.getSmallestDifference(1.5 * Math.PI, -0.5 * Math.PI);
        assertEquals(0.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getSmallestDifference_returns_minusPI_when_15PI_and_minus15PI_are_provided() throws Exception {
        double result = Radians.getSmallestDifference(1.5 * Math.PI, -1.5 * Math.PI);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getSmallestDifference_returns_PI_when_minus15PI_and_15PI_are_provided() throws Exception {
        double result = Radians.getSmallestDifference(-1.5 * Math.PI, 1.5 * Math.PI);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getSmallestDifference_returns_075PI_when_125PI_and_minus15PI_are_provided() throws Exception {
        double result = Radians.getSmallestDifference(1.25 * Math.PI, -1.5 * Math.PI);
        assertEquals(0.75, result/Math.PI, 0.0001);
    }

    @Test
    public void getSmallestDifference_returns_075PI_when_minus125PI_and_15PI_are_provided() throws Exception {
        double result = Radians.getSmallestDifference(-1.25 * Math.PI, 1.5 * Math.PI);
        assertEquals(0.75, result/Math.PI, 0.0001);
    }

    @Test
    public void getSmallestDifference_returns_075PI_when_175PI_and_minus15PI_are_provided() throws Exception {
        double result = Radians.getSmallestDifference(1.75 * Math.PI, -1.5 * Math.PI);
        assertEquals(0.75, result/Math.PI, 0.0001);
    }

    @Test
    public void getSmallestDifference_returns_075PI_when_minus175PI_and_15PI_are_provided() throws Exception {
        double result = Radians.getSmallestDifference(-1.75 * Math.PI, 1.5 * Math.PI);
        assertEquals(0.75, result/Math.PI, 0.0001);
    }

    @Test
    public void getSmallestDifference_returns_PI_when_minus05PI_and_05PI_are_provided() throws Exception {
        double result = Radians.getSmallestDifference(-0.5 * Math.PI, 0.5 * Math.PI);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getSmallestDifference_returns_minusPI_when_05PI_and_minus05PI_are_provided() throws Exception {
        double result = Radians.getSmallestDifference(0.5 * Math.PI, -0.5 * Math.PI);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }
    
    // getRelativeDifference
    @Test
    public void getRelativeDifference_returns_0_when_15PI_and_minus05PI_are_provided() throws Exception {
        double result = Radians.getRelativeDifference(1.5 * Math.PI, -0.5 * Math.PI);
        assertEquals(0.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getRelativeDifference_returns_minusPI_when_15PI_and_minus15PI_are_provided() throws Exception {
        double result = Radians.getRelativeDifference(1.5 * Math.PI, -1.5 * Math.PI);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getRelativeDifference_returns_PI_when_minus15PI_and_15PI_are_provided() throws Exception {
        double result = Radians.getRelativeDifference(-1.5 * Math.PI, 1.5 * Math.PI);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getRelativeDifference_returns_minus075PI_when_125PI_and_minus15PI_are_provided() throws Exception {
        double result = Radians.getRelativeDifference(1.25 * Math.PI, -1.5 * Math.PI);
        assertEquals(-0.75, result/Math.PI, 0.0001);
    }

    @Test
    public void getRelativeDifference_returns_075PI_when_minus125PI_and_15PI_are_provided() throws Exception {
        double result = Radians.getRelativeDifference(-1.25 * Math.PI, 1.5 * Math.PI);
        assertEquals(0.75, result/Math.PI, 0.0001);
    }

    @Test
    public void getRelativeDifference_returns_075PI_when_175PI_and_minus15PI_are_provided() throws Exception {
        double result = Radians.getRelativeDifference(1.75 * Math.PI, -1.5 * Math.PI);
        assertEquals(0.75, result/Math.PI, 0.0001);
    }

    @Test
    public void getRelativeDifference_returns_minus075PI_when_minus175PI_and_15PI_are_provided() throws Exception {
        double result = Radians.getRelativeDifference(-1.75 * Math.PI, 1.5 * Math.PI);
        assertEquals(-0.75, result/Math.PI, 0.0001);
    }

    @Test
    public void getRelativeDifference_returns_PI_when_minus05PI_and_05PI_are_provided() throws Exception {
        double result = Radians.getRelativeDifference(-0.5 * Math.PI, 0.5 * Math.PI);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getRelativeDifference_returns_minusPI_when_05PI_and_minus05PI_are_provided() throws Exception {
        double result = Radians.getRelativeDifference(0.5 * Math.PI, -0.5 * Math.PI);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }

    // getBoundedDifference
    @Test
    public void getBoundedDifference_returns_0_when_15PI_and_minus05PI_are_provided() throws Exception {
        double result = Radians.getBoundedDifference(1.5 * Math.PI, -0.5 * Math.PI);
        assertEquals(0.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedDifference_returns_minusPI_when_15PI_and_minus15PI_are_provided() throws Exception {
        double result = Radians.getBoundedDifference(1.5 * Math.PI, -1.5 * Math.PI);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedDifference_returns_PI_when_minus15PI_and_15PI_are_provided() throws Exception {
        double result = Radians.getBoundedDifference(-1.5 * Math.PI, 1.5 * Math.PI);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedDifference_returns_075PI_when_175PI_and_minus15PI_are_provided() throws Exception {
        double result = Radians.getBoundedDifference(1.75 * Math.PI, -1.5 * Math.PI);
        assertEquals(0.75, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedDifference_returns_125PI_when_minus175PI_and_15PI_are_provided() throws Exception {
        double result = Radians.getBoundedDifference(-1.75 * Math.PI, 1.5 * Math.PI);
        assertEquals(1.25, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedDifference_returns_PI_when_minus05PI_and_05PI_are_provided() throws Exception {
        double result = Radians.getBoundedDifference(-0.5 * Math.PI, 0.5 * Math.PI);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedDifference_returns_minusPI_when_05PI_and_minus05PI_are_provided() throws Exception {
        double result = Radians.getBoundedDifference(0.5 * Math.PI, -0.5 * Math.PI);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }

    // getBoundedArcTan
    @Test
    public void getBoundedArcTan_returns_0_when_y_is_0_and_x_is_greater_than_0() throws Exception {
        double result = Radians.getBoundedArcTan(1, 0);
        assertEquals(0.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedArcTan_returns_0_when_y_is_0_and_x_is_smaller_than_0() throws Exception {
        double result = Radians.getBoundedArcTan(-1, 0);
        assertEquals(1.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedArcTan_returns_0_when_y_is_0_and_x_is_0() throws Exception {
        double result = Radians.getBoundedArcTan(0, 0);
        assertEquals(0.0, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedArcTan_returns_05PI_when_y_is_greater_than_0_and_x_is_0() throws Exception {
        double result = Radians.getBoundedArcTan(0, 1);
        assertEquals(0.5, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedArcTan_returns_15PI_when_y_is_smaller_than_0_and_x_is_0() throws Exception {
        double result = Radians.getBoundedArcTan(0, -1);
        assertEquals(1.5, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedArcTan_returns_025PI_when_y_is_1_and_x_is_1() throws Exception {
        double result = Radians.getBoundedArcTan(1, 1);
        assertEquals(0.25, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedArcTan_returns_075PI_when_y_is_1_and_x_is_minus1() throws Exception {
        double result = Radians.getBoundedArcTan(-1, 1);
        assertEquals(0.75, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedArcTan_returns_125PI_when_y_is_minus1_and_x_is_minus1() throws Exception {
        double result = Radians.getBoundedArcTan(-1, -1);
        assertEquals(1.25, result/Math.PI, 0.0001);
    }

    @Test
    public void getBoundedArcTan_returns_175PI_when_y_is_minus1_and_x_is_1() throws Exception {
        double result = Radians.getBoundedArcTan(1, -1);
        assertEquals(1.75, result/Math.PI, 0.0001);
    }
}