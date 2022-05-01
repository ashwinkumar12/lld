package com.ashwinkumar.lld.lockermanagement.strategy;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OtpGeneratorRandom implements IOtpGenerator {

    private int otpLength;
    private IRandomGenerator randomGenerator;

    public String generateOtp() {
        StringBuilder builder = new StringBuilder();
        for (int i=0; i < otpLength; i++) {
            builder.append(randomGenerator.getRandomNumber(10));
        }
        return builder.toString();
    }

}
