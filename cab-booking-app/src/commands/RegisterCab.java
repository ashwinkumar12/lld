package commands;

import model.Cab;
import model.Command;
import model.Driver;
import service.CabBookingService;

import java.util.List;

public class RegisterCab extends CommandExecutor {

    public static final String register_cab = "register_cab";

    public RegisterCab(CabBookingService service) {
        super(service);
    }

    @Override
    public boolean validate(Command command) {
        if (command.getParams().size() == 4) {
            //validate driverName, driverNumber, driverIsAvailable, cabNumber
            return true;
        }
        return false;
    }

    @Override
    public void execute(Command command) {
        List<String> params = command.getParams();
        Driver driver = getCabBookingService().registerDriver(params.get(0), params.get(1), Boolean.parseBoolean(params.get(2)));
        Cab cab = getCabBookingService().registerCab(driver, params.get(3));
    }

}
