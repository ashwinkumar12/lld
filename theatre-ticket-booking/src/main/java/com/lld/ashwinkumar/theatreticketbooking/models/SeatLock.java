package com.lld.ashwinkumar.theatreticketbooking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Date;

@Getter
@AllArgsConstructor
public class SeatLock {

    private Show show;
    private Seat seat;
    private Integer timeoutInSeconds;
    private Date lockTime;
    private String lockedBy;

    public boolean isLockExpired() {
        Instant lockInstant = lockTime.toInstant().plusSeconds(timeoutInSeconds);
        Instant currentInstant = new Date().toInstant();
        return lockInstant.isBefore(currentInstant);
    }

}
