package commands;

import model.*;
import service.CabBookingService;

import java.util.List;

public class BookCab extends CommandExecutor {

    public static final String book_cab = "book_cab";

    public BookCab(CabBookingService service) {
        super(service);
    }

    @Override
    public boolean validate(Command command) {
        if (command.getParams().size() == 5) {
            //validate rider number, rider start location point x and y, rider end location point x and y
            return true;
        }
        return false;
    }

    @Override
    public void execute(Command command) {
        List<String> params = command.getParams();
        Rider rider = getCabBookingService().getRiderByNumber(params.get(0));
        Location startLocation = new Location(Double.parseDouble(params.get(1)), Double.parseDouble(params.get(2)));
        Location endLocation = new Location(Double.parseDouble(params.get(3)), Double.parseDouble(params.get(4)));
        getCabBookingService().bookCab(rider, startLocation, endLocation);
    }

}
