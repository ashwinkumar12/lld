package service.state_machine;

import model.Cab;

public class EmptyState implements State {

    @Override
    public void doAction(Cab cab) {
        cab.setCabState(Cab.state.EMPTY.toString());
    }

}
