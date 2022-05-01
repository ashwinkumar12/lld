package commands;

import model.Command;
import service.CabBookingService;

import java.util.List;

public class RegisterRider extends CommandExecutor {

    public static final String register_rider = "register_rider";

    public RegisterRider(CabBookingService service) {
        super(service);
    }

    @Override
    public boolean validate(Command command) {
        if (command.getParams().size() == 2) {
            //validate riderName, riderNumber
            return true;
        }
        return false;
    }

    @Override
    public void execute(Command command) {
        List<String> params = command.getParams();
        getCabBookingService().registerDriver(params.get(0), params.get(1));
    }

}
