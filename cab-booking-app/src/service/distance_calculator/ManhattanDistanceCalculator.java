package service.distance_calculator;

import model.Location;

public class ManhattanDistanceCalculator implements DistanceCalculatorStrategy {

    /**
     * Calculates the Manhattan distance between two locations.
     *
     * @param a the first location
     * @param b the second location
     * @return the sum of the absolute differences of the x and y coordinates of the two locations
     */
    @Override
    public double calculateDistance(Location a, Location b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

} 