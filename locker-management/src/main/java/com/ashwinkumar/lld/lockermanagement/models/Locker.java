package com.ashwinkumar.lld.lockermanagement.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class Locker {

    private UUID id;
    private List<Slot> slots;

    public Locker(UUID id) {
        this.id = id;
        this.slots = new ArrayList<>();
    }

    public Locker() {
        this.id = UUID.randomUUID();
        this.slots = new ArrayList<>();
    }

    public void addSlot(Slot newSlot) {
        slots.add(newSlot);
    }

    public List<Slot> getAvailableSlots() {
        return slots.stream().filter(Slot::isAvailable).collect(Collectors.toList());
    }

}
