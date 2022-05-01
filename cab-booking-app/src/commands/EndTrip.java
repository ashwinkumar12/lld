package commands;

import model.Command;
import model.Location;
import model.Rider;
import service.CabBookingService;

import java.util.List;

public class EndTrip extends CommandExecutor {

    public static final String end_trip = "end_trip";

    public EndTrip(CabBookingService service) {
        super(service);
    }

    @Override
    public boolean validate(Command command) {
        if (command.getParams().size() == 3) {
            //validate rider number, end location
            return true;
        }
        return false;
    }

    @Override
    public void execute(Command command) {
        List<String> params = command.getParams();
        Rider rider = getCabBookingService().getRiderByNumber(params.get(0));
        Location endLocation = new Location(Double.parseDouble(params.get(1)), Double.parseDouble((params.get(2))));
        getCabBookingService().endTrip(rider, endLocation);
    }

}
