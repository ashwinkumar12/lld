package com.ashwinkumar.lld.lockermanagement.controller;

import com.ashwinkumar.lld.lockermanagement.models.Locker;
import com.ashwinkumar.lld.lockermanagement.models.Size;
import com.ashwinkumar.lld.lockermanagement.models.Slot;
import com.ashwinkumar.lld.lockermanagement.service.LockerService;
import com.ashwinkumar.lld.lockermanagement.service.OtpService;
import com.ashwinkumar.lld.lockermanagement.service.SlotService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class LockerController {

    private LockerService lockerService;
    private SlotService slotService;
    private OtpService otpService;

    public Locker createLocker(String lockerId) {
        return lockerService.createLocker(lockerId);
    }

    public Slot createSlot(Locker locker, Size slotSize) {
        return lockerService.createSlotForLocker(locker, slotSize);
    }

    public List<Slot> getAvailableSlots() {
        return slotService.getAvailableSlots();
    }

    public boolean unlockSlot(Slot slot, String otp) {
        return otpService.validateOtp(slot, otp);
    }

    public void deallocateSlot(Slot slot) {
        slotService.deallocateSlot(slot);
    }

}
