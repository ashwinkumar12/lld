package com.ashwinkumar.lld.lockermanagement.strategy;

import com.ashwinkumar.lld.lockermanagement.models.Locker;
import com.ashwinkumar.lld.lockermanagement.models.LockerItem;
import com.ashwinkumar.lld.lockermanagement.models.Slot;

import java.util.List;

public interface ISlotFilteringStrategy {

    List<Slot> filterSlots(List<Slot> slots, LockerItem lockerItem);

}
