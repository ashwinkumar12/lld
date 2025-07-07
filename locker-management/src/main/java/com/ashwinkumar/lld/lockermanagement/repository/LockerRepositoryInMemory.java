package com.ashwinkumar.lld.lockermanagement.repository;

import com.ashwinkumar.lld.lockermanagement.models.Locker;
import com.ashwinkumar.lld.lockermanagement.models.Slot;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class LockerRepositoryInMemory implements ILockerRepository {

    private List<Locker> allLockers;

    public LockerRepositoryInMemory() {
        this.allLockers = new ArrayList<>();
    }

    private Optional<Locker> getLocker(UUID lockerId) {
        return allLockers.stream().filter(locker -> locker.getId().equals(lockerId)).findFirst();
    }

    @Override
    public Locker createLocker(UUID lockerId) {
        if (getLocker(lockerId).isPresent()) {
            throw new RuntimeException("Locker with ID " + lockerId + " already exists");
        }
        Locker locker = new Locker(lockerId);
        allLockers.add(locker);
        return locker;
    }

    @Override
    public Locker createLocker() {
        Locker locker = new Locker();
        allLockers.add(locker);
        return locker;
    }

    public List<Slot> getAvailableSlots() {
        List<Slot> slots = new ArrayList<>();
        for (Locker locker : allLockers) {
            slots.addAll(locker.getAvailableSlots());
        }
        return slots;
    }
    
}
