package model;

public class Cab {

    private Driver driver;
    private String cabNumber;
    private Location currentLocation;
    private String cabState;

    public String getCabState() {
        return cabState;
    }

    public void setCabState(String cabState) {
        this.cabState = cabState;
    }

    public enum state {
        BOOKED,
        EMPTY
    }

    public Cab(Driver driver, String cabNumber) {
        this.driver = driver;
        this.cabNumber = cabNumber;
        this.cabState = state.EMPTY.toString();
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getCabNumber() {
        return cabNumber;
    }

    public void setCabNumber(String cabNumber) {
        this.cabNumber = cabNumber;
    }

}
