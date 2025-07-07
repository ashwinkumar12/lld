package com.ashwinkumar.lld.lockermanagement.repository;

import com.ashwinkumar.lld.lockermanagement.models.Locker;
import com.ashwinkumar.lld.lockermanagement.models.Slot;

import java.util.List;
import java.util.UUID;

public interface ILockerRepository {

    public Locker createLocker(UUID lockerId);

    public Locker createLocker();

    public List<Slot> getAvailableSlots();

}
