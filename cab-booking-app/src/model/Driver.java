package model;

public class Driver {

    public Driver(String name, String phoneNumber, boolean isAvailable) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isAvailable = isAvailable;
    }

    private String name;
    private String phoneNumber;
    private boolean isAvailable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

}
