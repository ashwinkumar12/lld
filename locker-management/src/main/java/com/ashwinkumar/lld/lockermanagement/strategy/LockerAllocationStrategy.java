package com.ashwinkumar.lld.lockermanagement.strategy;

import com.ashwinkumar.lld.lockermanagement.models.Slot;

import java.util.List;

public interface LockerAllocationStrategy {

    public Slot allocateSlot(List<Slot> slots);

}
