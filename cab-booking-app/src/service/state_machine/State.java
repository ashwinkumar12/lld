package service.state_machine;

import model.Cab;

public interface State {

    public void doAction(Cab cab);

}
