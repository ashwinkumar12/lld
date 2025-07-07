# Cab Booking Application

A cab booking platform that allows riders to book cabs and drivers to manage their availability.

## Features

- Register riders and drivers/cabs
- Book cabs based on proximity to riders
- Update cab locations and driver availability
- Fetch ride history for riders
- End trips
- **Configurable Distance Calculator Strategies**

## Distance Calculator Strategies

The application now supports multiple distance calculation methods:

### 1. Euclidean Distance Calculator (Default)
- **Formula**: `sqrt((x2-x1)² + (y2-y1)²)`
- **Use case**: Straight-line distance calculation (as the crow flies)
- **Best for**: Scenarios where direct distance matters (e.g., helicopter services)

### 2. Manhattan Distance Calculator
- **Formula**: `|x2-x1| + |y2-y1|`
- **Use case**: Grid-based distance calculation (like navigating city blocks)
- **Best for**: Urban environments with grid-like street layouts

## How to Run

1. **Compile the application:**
   ```bash
   javac -cp src src/driver/Test.java
   ```

2. **Run the application:**
   ```bash
   java -cp src driver.Test
   ```

3. **Select Distance Calculator Strategy:**
   - When prompted, choose:
     - `1` for Euclidean Distance Calculator (default)
     - `2` for Manhattan Distance Calculator

## Available Commands

- `register_cab <driver_name> <driver_number> <is_available> <cab_number>`
- `register_rider <rider_name> <rider_number>`
- `book_cab <rider_number> <start_x> <start_y> <end_x> <end_y>`
- `update_location <x> <y> <cab_number>`
- `driver_availability <cab_number> <is_available>`
- `fetch_ride_details_for_a_rider <rider_number>`
- `end_trip <rider_number> <end_x> <end_y>`
- `exit`

## Distance Calculator Demo

To see the difference between distance calculators:

```bash
javac -cp src src/demo/DistanceCalculatorDemo.java
java -cp src demo.DistanceCalculatorDemo
```

## Example Usage

```bash
# Start the application
java -cp src driver.Test

# Select distance calculator (1 for Euclidean, 2 for Manhattan)
Select Distance Calculator Strategy:
1. EUCLIDEAN (default)
2. MANHATTAN
Enter your choice (1 or 2): 2
Using Manhattan Distance Calculator

# Register a cab
register_cab john_doe 1234567890 true CAB001

# Register a rider
register_rider jane_smith 0987654321

# Update cab location
update_location 0 0 CAB001

# Book a cab
book_cab 0987654321 1 1 5 5

# Exit
exit
```

## Architecture

The application uses the **Strategy Pattern** for distance calculation:

- `DistanceCalculatorStrategy` - Interface defining the contract
- `EuclideanDistanceCalculator` - Implements straight-line distance
- `ManhattanDistanceCalculator` - Implements grid-based distance
- `DistanceCalculatorFactory` - Factory for creating strategy instances
- `CabBookingService` - Uses the configured strategy for cab allocation

## Configuration

The distance calculator strategy is configured at application startup through user input. The selected strategy is then used throughout the application session for all distance calculations during cab allocation.

## Extensibility

To add new distance calculation strategies:

1. Create a new class implementing `DistanceCalculatorStrategy`
2. Add the new strategy to `DistanceCalculatorFactory`
3. Update the user selection menu in `Test.java` 