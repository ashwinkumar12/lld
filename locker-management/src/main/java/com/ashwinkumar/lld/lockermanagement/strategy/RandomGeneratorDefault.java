package com.ashwinkumar.lld.lockermanagement.strategy;

public class RandomGeneratorDefault implements IRandomGenerator{

    @Override
    public Integer getRandomNumber(Integer lessThanThis) {
        return (int) (Math.random() * lessThanThis);
    }

}
