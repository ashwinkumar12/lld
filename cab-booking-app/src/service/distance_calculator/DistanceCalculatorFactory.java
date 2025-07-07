package service.distance_calculator;

public class DistanceCalculatorFactory {

    public enum DistanceCalculatorType {
        EUCLIDEAN, MANHATTAN
    }

    public static DistanceCalculatorStrategy getDistanceCalculator(DistanceCalculatorType type) {
        switch (type) {
            case EUCLIDEAN:
                return new EuclideanDistanceCalculator();
            case MANHATTAN:
                return new ManhattanDistanceCalculator();
            default:
                return new EuclideanDistanceCalculator(); // Default fallback
        }
    }

    public static DistanceCalculatorStrategy getDistanceCalculator(String type) {
        try {
            DistanceCalculatorType calculatorType = DistanceCalculatorType.valueOf(type.toUpperCase());
            return getDistanceCalculator(calculatorType);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid distance calculator type: " + type + ". Using default Euclidean calculator.");
            return new EuclideanDistanceCalculator();
        }
    }
} 