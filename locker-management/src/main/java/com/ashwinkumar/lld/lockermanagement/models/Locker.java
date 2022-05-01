package com.ashwinkumar.lld.lockermanagement.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Locker {

    private String id;
    private List<Slot> slots;

    public Locker(String id) {
        this.id = id;
        this.slots = new ArrayList<>();
    }

    public void addSlot(Slot newSlot) {
        slots.add(newSlot);
    }

    public List<Slot> getAvailableSlots() {
        return slots.stream().filter(Slot::isAvailable).collect(Collectors.toList());
    }

}
