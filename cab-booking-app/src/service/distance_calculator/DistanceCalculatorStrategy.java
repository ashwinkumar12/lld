package service.distance_calculator;

import model.Location;

public interface DistanceCalculatorStrategy {

    public double calculateDistance(Location a, Location b);

}
