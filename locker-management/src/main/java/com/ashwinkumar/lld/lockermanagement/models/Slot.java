package com.ashwinkumar.lld.lockermanagement.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
public class Slot {

    private String id;
    private Size size;
    private Locker locker;
    private LockerItem currentLockerItem;
    private Date allocationDate;


    public Slot(String id, Size size, Locker locker) {
        this.id = id;
        this.size = size;
        this.locker = locker;
        this.currentLockerItem = null;
    }

    synchronized public void allocatePackage(LockerItem lockerItem) {
        if (this.currentLockerItem != null) {
            throw new RuntimeException();
        }
        this.currentLockerItem = lockerItem;
        this.allocationDate = allocationDate;
    }

    synchronized public void deallocatePackage() {
        this.currentLockerItem = null;
    }

    synchronized public boolean isAvailable() {
        return currentLockerItem == null;
    }

}
