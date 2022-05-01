package commands;

import model.Command;
import service.CabBookingService;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutorFactory {

    private Map<String, CommandExecutor> commands = new HashMap<>();

    public CommandExecutorFactory(CabBookingService service) {

        commands.put(RegisterCab.register_cab, new RegisterCab(service));
        commands.put(BookCab.book_cab, new BookCab(service));
        commands.put(DriverAvailability.driver_availability, new DriverAvailability(service));
        commands.put(EndTrip.end_trip, new EndTrip(service));
        commands.put(FetchRideDetailsForRider.fetch_ride_details_for_a_rider, new FetchRideDetailsForRider(service));
        commands.put(RegisterRider.register_rider, new RegisterRider(service));
        commands.put(UpdateCabLocation.update_location, new UpdateCabLocation(service));

    }

    public CommandExecutor getCommandExecutor(Command command) {
        CommandExecutor commandExecutor = commands.get(command.getCommand());
        if (commandExecutor == null) {
            throw new RuntimeException();
        }
        return commandExecutor;
    }

}
