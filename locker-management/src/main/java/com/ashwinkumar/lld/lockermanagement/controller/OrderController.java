package com.ashwinkumar.lld.lockermanagement.controller;

import com.ashwinkumar.lld.lockermanagement.models.Buyer;
import com.ashwinkumar.lld.lockermanagement.models.Locker;
import com.ashwinkumar.lld.lockermanagement.models.LockerItem;
import com.ashwinkumar.lld.lockermanagement.models.Slot;
import com.ashwinkumar.lld.lockermanagement.service.LockerService;
import com.ashwinkumar.lld.lockermanagement.service.NotificationService;
import com.ashwinkumar.lld.lockermanagement.service.OtpService;
import com.ashwinkumar.lld.lockermanagement.service.SlotService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderController {

    private OtpService otpService;
    private NotificationService notificationService;
    private SlotService slotService;

    public Slot allocateSlot(Buyer buyer, LockerItem lockerItem) {
        Slot slot = slotService.allocateSlot(lockerItem);
        String otp = otpService.generateOtp(slot);
        notificationService.notifyUser(buyer, otp, slot);
        return slot;
    }

}
