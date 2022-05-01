package com.ashwinkumar.lld.lockermanagement.strategy;

import com.ashwinkumar.lld.lockermanagement.models.LockerItem;
import com.ashwinkumar.lld.lockermanagement.models.Slot;

import java.util.List;

public interface IRandomGenerator {

    Integer getRandomNumber(Integer lessThanThis);

}
