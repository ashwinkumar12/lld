package commands;

import model.Cab;
import model.Command;
import model.Driver;
import model.Location;
import service.CabBookingService;

import java.util.List;

public class UpdateCabLocation extends CommandExecutor {

    public static final String update_location = "update_location";

    public UpdateCabLocation(CabBookingService service) {
        super(service);
    }

    @Override
    public boolean validate(Command command) {
        if (command.getParams().size() == 3) {
            //validate point x, point y, cab number
            return true;
        }
        return false;
    }

    @Override
    public void execute(Command command) {
        List<String> params = command.getParams();
        Location location = new Location(Double.parseDouble(params.get(0)), Double.parseDouble(params.get(1)));
        Cab cab = getCabBookingService().getCabByNumber(params.get(2));
        getCabBookingService().updateCabLocation(cab, location);
    }

}
