package com.ashwinkumar.lld.lockermanagement.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Size {

    private Double width;
    private Double height;

    public boolean canAccomodate(Size sizeToAccomodate) {
        return this.width >= sizeToAccomodate.width
                && this.height >= sizeToAccomodate.height;
    }

}
