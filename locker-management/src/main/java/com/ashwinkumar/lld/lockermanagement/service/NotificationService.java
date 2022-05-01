package com.ashwinkumar.lld.lockermanagement.service;

import com.ashwinkumar.lld.lockermanagement.models.*;
import com.ashwinkumar.lld.lockermanagement.repository.ILockerRepository;
import com.ashwinkumar.lld.lockermanagement.strategy.ISlotFilteringStrategy;
import com.ashwinkumar.lld.lockermanagement.strategy.LockerAllocationStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class NotificationService {

    public void notifyUser(LockerUser user, String otp, Slot slot) {
        System.out.println("Sending OTP " + otp + "to user " + user.getContact() + "for slot " + slot.getId());
    }

}
