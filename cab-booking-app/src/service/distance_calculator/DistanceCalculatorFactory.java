package service.distance_calculator;

public class DistanceCalculatorFactory {

    public enum DistanceCalculatorType {
        EUCLIDEAN, MANHATTAN
    }

    /**
     * Returns a distance calculator strategy instance based on the specified type.
     *
     * If the provided type is not recognized, a Euclidean distance calculator is returned by default.
     *
     * @param type the type of distance calculator strategy to obtain
     * @return an instance of the corresponding distance calculator strategy
     */
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

    /**
     * Returns a distance calculator strategy based on the provided type name.
     *
     * If the input string does not match a known calculator type, the default Euclidean calculator is returned.
     *
     * @param type the name of the distance calculator type (case-insensitive)
     * @return an instance of the corresponding distance calculator strategy, or the default Euclidean calculator if the type is invalid
     */
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