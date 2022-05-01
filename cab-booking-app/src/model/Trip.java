package model;

public class Trip {

    private Rider rider;
    private Location startLocation;
    private Location endLocation;
    private Cab cab;

    public Trip(Rider rider, Location startLocation, Location endLocation, Cab cab) {
        this.rider = rider;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.cab = cab;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public Cab getCab() {
        return cab;
    }

    public void setCab(Cab cab) {
        this.cab = cab;
    }

}
