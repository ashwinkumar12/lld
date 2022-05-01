package com.ashwinkumar.lld.lockermanagement.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Package implements LockerItem {

    private String id;
    private Size size;

}
