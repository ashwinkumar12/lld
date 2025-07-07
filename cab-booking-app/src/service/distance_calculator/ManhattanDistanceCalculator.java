package service.distance_calculator;

import model.Location;

public class ManhattanDistanceCalculator implements DistanceCalculatorStrategy {

    @Override
    public double calculateDistance(Location a, Location b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

} 