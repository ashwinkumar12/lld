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
        Contact contact = user.getContact();
        if (!contact.isComplete()) {
            System.out.println("Warning: Incomplete contact information for user: " + contact);
        }
        
        if (contact.hasValidPhone() && contact.hasValidEmail()) {
            System.out.println("Sending OTP " + otp + " to user via SMS: " + contact.getPhone() + " and Email: " + contact.getEmail() + " for slot " + slot.getId());
        } else if (contact.hasValidPhone()) {
            notifyUserBySms(user, otp, slot);
        } else if (contact.hasValidEmail()) {
            notifyUserByEmail(user, otp, slot);
        } else {
            System.out.println("Error: No valid contact method available for user");
        }
    }
    
    public void notifyUserBySms(LockerUser user, String otp, Slot slot) {
        Contact contact = user.getContact();
        if (contact.hasValidPhone()) {
            System.out.println("Sending SMS OTP " + otp + " to " + contact.getPhone() + " for slot " + slot.getId());
        } else {
            System.out.println("Error: Invalid phone number for SMS notification");
        }
    }
    
    public void notifyUserByEmail(LockerUser user, String otp, Slot slot) {
        Contact contact = user.getContact();
        if (contact.hasValidEmail()) {
            System.out.println("Sending Email OTP " + otp + " to " + contact.getEmail() + " for slot " + slot.getId());
        } else {
            System.out.println("Error: Invalid email address for email notification");
        }
    }

}
