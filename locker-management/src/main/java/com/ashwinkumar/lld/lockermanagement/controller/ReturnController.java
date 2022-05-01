package com.ashwinkumar.lld.lockermanagement.controller;

import com.ashwinkumar.lld.lockermanagement.models.Buyer;
import com.ashwinkumar.lld.lockermanagement.models.DeliveryPerson;
import com.ashwinkumar.lld.lockermanagement.models.LockerItem;
import com.ashwinkumar.lld.lockermanagement.models.Slot;
import com.ashwinkumar.lld.lockermanagement.service.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturnController {

    private OtpService otpService;
    private NotificationService notificationService;
    private DeliveryPersonService deliveryPersonService;
    private SlotService slotService;

    public Slot allocateSlot(LockerItem lockerItem) {
        Slot slot = slotService.allocateSlot(lockerItem);
        String otp = otpService.generateOtp(slot);
        DeliveryPerson deliveryPerson = deliveryPersonService.getDeliveryPerson(slot);
        notificationService.notifyUser(deliveryPerson, otp, slot);
        return slot;
    }

}
