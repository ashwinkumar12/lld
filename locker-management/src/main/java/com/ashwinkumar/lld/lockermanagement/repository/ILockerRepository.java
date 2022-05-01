package com.ashwinkumar.lld.lockermanagement.repository;

import com.ashwinkumar.lld.lockermanagement.models.Locker;
import com.ashwinkumar.lld.lockermanagement.models.Slot;

import java.util.List;

public interface ILockerRepository {

    public Locker createLocker(String lockerId);

    public List<Slot> getAvailableSlots();

}
