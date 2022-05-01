package service;

import model.*;
import service.distance_calculator.DistanceCalculatorStrategy;
import service.distance_calculator.EuclideanDistanceCalculator;
import service.state_machine.BookedState;
import service.state_machine.EmptyState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CabBookingService {

    private static List<Cab> cabList = new ArrayList<>();
    private static List<Rider> riders = new ArrayList<>();
    private static List<Trip> trips = new ArrayList<>();

    private static final double ALLOWED_DISTANCE = 5;

    public static List<Cab> getCabList() {
        return cabList;
    }

    public static List<Rider> getRiders() {
        return riders;
    }

    public Driver registerDriver(String driverName, String driverNumber, boolean isAvailable) {
        return new Driver(driverName, driverNumber, isAvailable);
    }

    public Cab registerCab(Driver driver, String cabNumber) {
        Cab cab = new Cab(driver, cabNumber);
        cabList.add(cab);
        return cab;
    }

    public Rider registerDriver(String name, String mobileNumber) {
        Rider rider = new Rider(name, mobileNumber);
        riders.add(rider);
        return rider;
    }

    private Cab allotACab(Rider rider, Location startLocation) {

        DistanceCalculatorStrategy distanceCalculator = new EuclideanDistanceCalculator();
        List<Cab> cabsInRange = getCabList().stream()
                .filter(cab -> cab.getDriver().isAvailable())
                .filter(cab -> cab.getCabState().equals(Cab.state.EMPTY.toString()))
                .filter(cab -> distanceCalculator.calculateDistance(cab.getCurrentLocation(), startLocation) < ALLOWED_DISTANCE)
                .sorted()
                .collect(Collectors.toList());
        if (cabsInRange.get(0) == null) {
            throw new RuntimeException("No cabs available at the moment");
        }
        return cabsInRange.get(0);

    }

    public void bookCab(Rider rider, Location startLocation, Location endLocation) {
        Cab cab = allotACab(rider, startLocation);
        new BookedState().doAction(cab);
        Trip ride = new Trip(rider, startLocation, endLocation, cab);
        trips.add(ride);
    }


    public Cab getCabByNumber(String cabNumber) {
        Optional<Cab> cabOptional = getCabList().stream()
                .filter(cab -> cab.getCabNumber().equals(cabNumber))
                .findFirst();
        if (!cabOptional.isPresent()) {
            throw new RuntimeException("Cab not present");
        }
        return cabOptional.get();
    }

    public Rider getRiderByNumber(String phoneNumber) {
        Optional<Rider> riderOptional = getRiders().stream()
                .filter(rider -> rider.getMobileNumber().equals(phoneNumber))
                .findFirst();
        if (!riderOptional.isPresent()) {
            throw new RuntimeException("Rider not present");
        }
        return riderOptional.get();
    }

    public void updateCabLocation(Cab cab, Location location) {
        cab.setCurrentLocation(location);
    }

    public void updateDriverAvailability(Cab cab, boolean isAvailable) {
        cab.getDriver().setAvailable(isAvailable);
    }

    public List<Trip> fetchRideDetailsForRider(Rider rider) {
        return trips.stream()
                .filter(ride -> ride.getRider().getMobileNumber().equals(rider.getMobileNumber()))
                .collect(Collectors.toList());
    }

    private Trip getCurrentTripForRider(Rider rider) {
        Optional<Trip> tripOptional = trips.stream()
                .filter(ride -> ride.getRider().getMobileNumber().equals(rider.getMobileNumber()))
                .filter(ride -> ride.getCab().getCabState().equals(Cab.state.BOOKED.toString()))
                .findFirst();
        if (!tripOptional.isPresent()) {
            throw new RuntimeException("No active trip found for the rider");
        }
        return tripOptional.get();
    }

    public void endTrip(Rider rider, Location endLocation) {
        Trip ride = getCurrentTripForRider(rider);
        new EmptyState().doAction(ride.getCab());
        ride.setEndLocation(endLocation);
    }

}