package com.ashwinkumar.lld.lockermanagement.service;

import com.ashwinkumar.lld.lockermanagement.models.Locker;
import com.ashwinkumar.lld.lockermanagement.models.LockerItem;
import com.ashwinkumar.lld.lockermanagement.models.Size;
import com.ashwinkumar.lld.lockermanagement.models.Slot;
import com.ashwinkumar.lld.lockermanagement.repository.ILockerRepository;
import com.ashwinkumar.lld.lockermanagement.repository.LockerRepositoryInMemory;
import com.ashwinkumar.lld.lockermanagement.strategy.IRandomGenerator;
import com.ashwinkumar.lld.lockermanagement.strategy.ISlotFilteringStrategy;
import com.ashwinkumar.lld.lockermanagement.strategy.LockerAllocationStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class LockerService {

    private ILockerRepository lockerRepository;
    private ISlotFilteringStrategy slotFilteringStrategy;
    private LockerAllocationStrategy allocationStrategy;
    private SlotService slotService;

    public Locker createLocker(String lockerId) {
        return lockerRepository.createLocker(lockerId);
    }

    public Slot createSlotForLocker(Locker locker, Size slotSize) {
        Slot slot = slotService.createSlot(locker, slotSize);
        locker.addSlot(slot);
        return slot;
    }

}
