package commands;

import model.Command;
import service.CabBookingService;

public abstract class CommandExecutor {

    private CabBookingService cabBookingService;

    public CabBookingService getCabBookingService() {
        return cabBookingService;
    }

    public CommandExecutor(CabBookingService service) {
        this.cabBookingService = service;
    }

    public abstract boolean validate(Command command);

    public abstract void execute(Command command);

}
