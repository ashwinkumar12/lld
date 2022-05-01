package commands;

import model.Cab;
import model.Command;
import model.Location;
import service.CabBookingService;

import java.util.List;

public class DriverAvailability extends CommandExecutor {

    public static final String driver_availability = "driver_availability";

    public DriverAvailability(CabBookingService service) {
        super(service);
    }

    @Override
    public boolean validate(Command command) {
        if (command.getParams().size() == 2) {
            //validate cab number, driver availability boolean value
            return true;
        }
        return false;
    }

    @Override
    public void execute(Command command) {
        List<String> params = command.getParams();
        Cab cab = getCabBookingService().getCabByNumber(params.get(0));
        getCabBookingService().updateDriverAvailability(cab, Boolean.parseBoolean(params.get(1)));
    }

}
