package com.ashwinkumar.lld.lockermanagement.strategy;

import com.ashwinkumar.lld.lockermanagement.models.LockerItem;
import com.ashwinkumar.lld.lockermanagement.models.Slot;

import java.util.List;
import java.util.stream.Collectors;

public class SizeBasedSlotFilteringStrategy implements ISlotFilteringStrategy {

    @Override
    public List<Slot> filterSlots(List<Slot> slots, LockerItem lockerItem) {
        return slots.stream()
                .filter(slot -> slot.getSize().canAccomodate(lockerItem.getSize()))
                .collect(Collectors.toList());
    }

}
