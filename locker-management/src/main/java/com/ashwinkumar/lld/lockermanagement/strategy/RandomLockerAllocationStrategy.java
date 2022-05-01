package com.ashwinkumar.lld.lockermanagement.strategy;

import com.ashwinkumar.lld.lockermanagement.models.Locker;
import com.ashwinkumar.lld.lockermanagement.models.Slot;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RandomLockerAllocationStrategy implements LockerAllocationStrategy {

    private IRandomGenerator randomGenerator;

    @Override
    public Slot allocateSlot(List<Slot> slots) {
        if (slots.isEmpty()) {
            return null;
        }
        return slots.get(randomGenerator.getRandomNumber(slots.size()));
    }

}
