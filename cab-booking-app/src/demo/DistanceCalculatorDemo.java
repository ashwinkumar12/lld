package demo;

import model.Location;
import service.distance_calculator.DistanceCalculatorFactory;
import service.distance_calculator.DistanceCalculatorStrategy;

public class DistanceCalculatorDemo {
    
    public static void main(String[] args) {
        // Create two test locations
        Location locationA = new Location(0, 0);
        Location locationB = new Location(3, 4);
        
        System.out.println("Distance Calculator Comparison Demo");
        System.out.println("====================================");
        System.out.println("Point A: (" + locationA.getX() + ", " + locationA.getY() + ")");
        System.out.println("Point B: (" + locationB.getX() + ", " + locationB.getY() + ")");
        System.out.println();
        
        // Test Euclidean Distance
        DistanceCalculatorStrategy euclideanCalculator = DistanceCalculatorFactory.getDistanceCalculator("EUCLIDEAN");
        double euclideanDistance = euclideanCalculator.calculateDistance(locationA, locationB);
        System.out.println("Euclidean Distance: " + euclideanDistance);
        System.out.println("Formula: sqrt((x2-x1)² + (y2-y1)²) = sqrt((3-0)² + (4-0)²) = sqrt(9 + 16) = sqrt(25) = 5.0");
        System.out.println();
        
        // Test Manhattan Distance
        DistanceCalculatorStrategy manhattanCalculator = DistanceCalculatorFactory.getDistanceCalculator("MANHATTAN");
        double manhattanDistance = manhattanCalculator.calculateDistance(locationA, locationB);
        System.out.println("Manhattan Distance: " + manhattanDistance);
        System.out.println("Formula: |x2-x1| + |y2-y1| = |3-0| + |4-0| = 3 + 4 = 7.0");
        System.out.println();
        
        System.out.println("Key Differences:");
        System.out.println("- Euclidean: Straight-line distance (as the crow flies)");
        System.out.println("- Manhattan: Grid-based distance (like navigating city blocks)");
        System.out.println("- Manhattan distance is always >= Euclidean distance");
    }
} 