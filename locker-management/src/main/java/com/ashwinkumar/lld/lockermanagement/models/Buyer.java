package com.ashwinkumar.lld.lockermanagement.models;

import lombok.Getter;

@Getter
public class Buyer extends LockerUser {

    public Buyer(Contact contact) {
        super(contact);
    }

}