package service.distance_calculator;

import model.Location;

public class EuclideanDistanceCalculator implements DistanceCalculatorStrategy {

    @Override
    public double calculateDistance(Location a, Location b) {
        return Math.sqrt( Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(),2));
    }

}