package service.state_machine;

import model.Cab;

public class BookedState implements State {

    @Override
    public void doAction(Cab cab) {
        cab.setCabState(Cab.state.BOOKED.toString());
    }

}
