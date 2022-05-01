package com.ashwinkumar.lld.lockermanagement.repository;

import com.ashwinkumar.lld.lockermanagement.models.Slot;
import com.ashwinkumar.lld.lockermanagement.service.LockerService;
import com.ashwinkumar.lld.lockermanagement.service.SlotService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SlotOtpRepositoryInMemory implements ISlotOtpRepository {

    private Map<Slot, String> slotOtpMap;
    private SlotService slotService;

    public SlotOtpRepositoryInMemory(SlotService slotService) {
        this.slotService = slotService;
        this.slotOtpMap = new HashMap<>();
    }

    @Override
    public void addOtp(String otp, String slotId) {
        Optional<Slot> slot = slotService.getSlot(slotId);
        if (! slot.isPresent()) {
            throw new RuntimeException();
        }
        slotOtpMap.put(slot.get(), otp);
    }

    @Override
    public String getOtp(String slotId) {
        Optional<Slot> slot = slotService.getSlot(slotId);
        if (! slot.isPresent()) {
            throw new RuntimeException();
        }
        return slotOtpMap.get(slot.get());
    }

}
