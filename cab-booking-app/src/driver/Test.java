package driver;

import commands.CommandExecutor;
import commands.CommandExecutorFactory;
import model.Command;
import service.CabBookingService;
import service.distance_calculator.DistanceCalculatorFactory;
import service.distance_calculator.DistanceCalculatorStrategy;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Allow user to select distance calculator strategy
        System.out.println("Select Distance Calculator Strategy:");
        System.out.println("1. EUCLIDEAN (default)");
        System.out.println("2. MANHATTAN");
        System.out.print("Enter your choice (1 or 2): ");
        
        String choice = scanner.nextLine().trim();
        DistanceCalculatorStrategy distanceCalculator;
        
        if ("2".equals(choice)) {
            distanceCalculator = DistanceCalculatorFactory.getDistanceCalculator("MANHATTAN");
            System.out.println("Using Manhattan Distance Calculator");
        } else {
            distanceCalculator = DistanceCalculatorFactory.getDistanceCalculator("EUCLIDEAN");
            System.out.println("Using Euclidean Distance Calculator");
        }

        CabBookingService service = new CabBookingService(distanceCalculator);

        System.out.println("\nCab Booking System initialized. Available commands:");
        System.out.println("- register_cab <driver_name> <driver_number> <is_available> <cab_number>");
        System.out.println("- register_rider <rider_name> <rider_number>");
        System.out.println("- book_cab <rider_number> <start_x> <start_y> <end_x> <end_y>");
        System.out.println("- update_location <x> <y> <cab_number>");
        System.out.println("- driver_availability <cab_number> <is_available>");
        System.out.println("- fetch_ride_details_for_a_rider <rider_number>");
        System.out.println("- end_trip <rider_number> <end_x> <end_y>");
        System.out.println("- exit");

        while (true) {
            System.out.println("\nEnter the command:");
            String inputLine = scanner.nextLine();
            Command command = new Command(inputLine);
            if (command.getCommand().equals("exit")) {
                break;
            }

            CommandExecutor commandExecutor = new CommandExecutorFactory(service).getCommandExecutor(command);
            if (commandExecutor.validate(command)) {
                commandExecutor.execute(command);
            }

        }

    }
}
