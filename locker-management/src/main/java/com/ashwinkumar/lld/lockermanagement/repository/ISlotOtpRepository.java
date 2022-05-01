package com.ashwinkumar.lld.lockermanagement.repository;

import com.ashwinkumar.lld.lockermanagement.models.Locker;
import com.ashwinkumar.lld.lockermanagement.models.Slot;

import java.util.List;

public interface ISlotOtpRepository {

    public void addOtp(String otp, String slotId);

    public String getOtp(String slotId);

}
