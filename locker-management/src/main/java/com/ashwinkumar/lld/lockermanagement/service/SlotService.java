package com.ashwinkumar.lld.lockermanagement.service;

import com.ashwinkumar.lld.lockermanagement.models.Locker;
import com.ashwinkumar.lld.lockermanagement.models.LockerItem;
import com.ashwinkumar.lld.lockermanagement.models.Size;
import com.ashwinkumar.lld.lockermanagement.models.Slot;
import com.ashwinkumar.lld.lockermanagement.repository.ILockerRepository;
import com.ashwinkumar.lld.lockermanagement.strategy.ISlotFilteringStrategy;
import com.ashwinkumar.lld.lockermanagement.strategy.LockerAllocationStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public class SlotService {

    private List<Slot> allSlots;
    private ILockerRepository lockerRepository;
    private ISlotFilteringStrategy slotFilteringStrategy;
    private LockerAllocationStrategy allocationStrategy;

    public SlotService(ILockerRepository lockerRepository,
                       ISlotFilteringStrategy slotFilteringStrategy,
                       LockerAllocationStrategy allocationStrategy) {
        this.lockerRepository = lockerRepository;
        this.slotFilteringStrategy = slotFilteringStrategy;
        this.allocationStrategy = allocationStrategy;
        this.allSlots = new ArrayList<>();
    }

    public Slot createSlot(Locker locker, Size slotSize) {
        Slot slot = new Slot(UUID.randomUUID().toString(), slotSize, locker);
        this.allSlots.add(slot);
        return slot;
    }

    public Optional<Slot> getSlot(String slotId) {
        return allSlots.stream().filter(slot -> slot.getId().equals(slotId)).findFirst();
    }

    public List<Slot> getAvailableSlots() {
        return lockerRepository.getAvailableSlots();
    }

    public Slot allocateSlot(LockerItem lockerItem) {
        List<Slot> availableSlots = lockerRepository.getAvailableSlots();
        List<Slot> filteredSlots = slotFilteringStrategy.filterSlots(availableSlots, lockerItem);
        Slot toBeAllocated = allocationStrategy.allocateSlot(filteredSlots);

        if (toBeAllocated == null) {
            throw new RuntimeException();
        }

        toBeAllocated.allocatePackage(lockerItem);
        return toBeAllocated;
    }

    public void deallocateSlot(Slot slot) {
        slot.deallocatePackage();
    }

}
