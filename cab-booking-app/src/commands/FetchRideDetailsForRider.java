package commands;

import model.Command;
import model.Rider;
import model.Trip;
import service.CabBookingService;

import java.util.List;

public class FetchRideDetailsForRider extends CommandExecutor {

    public static final String fetch_ride_details_for_a_rider = "fetch_ride_details_for_a_rider";

    public FetchRideDetailsForRider(CabBookingService service) {
        super(service);
    }

    @Override
    public boolean validate(Command command) {
        if (command.getParams().size() == 1) {
            //validate rider mobile number
            return true;
        }
        return false;
    }

    @Override
    public void execute(Command command) {
        List<String> params = command.getParams();
        Rider rider = getCabBookingService().getRiderByNumber(params.get(0));
        List<Trip> trips = getCabBookingService().fetchRideDetailsForRider(rider);
        for (Trip trip : trips) {
            System.out.println("cab" + trip.getCab().getCabNumber());
        }

    }

}
