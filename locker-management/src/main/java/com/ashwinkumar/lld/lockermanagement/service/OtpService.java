package com.ashwinkumar.lld.lockermanagement.service;

import com.ashwinkumar.lld.lockermanagement.models.LockerUser;
import com.ashwinkumar.lld.lockermanagement.models.Slot;
import com.ashwinkumar.lld.lockermanagement.repository.ISlotOtpRepository;
import com.ashwinkumar.lld.lockermanagement.strategy.IOtpGenerator;
import com.ashwinkumar.lld.lockermanagement.strategy.OtpGeneratorRandom;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtpService {

    private IOtpGenerator otpGenerator;
    private ISlotOtpRepository slotOtpRepository;

    public String generateOtp(Slot slot) {
        String otp = otpGenerator.generateOtp();
        slotOtpRepository.addOtp(otp, slot.getId());
        return otp;
    }

    public boolean validateOtp(Slot slot, String otp) {
        return slotOtpRepository.getOtp(slot.getId()).equals(otp);
    }

}
