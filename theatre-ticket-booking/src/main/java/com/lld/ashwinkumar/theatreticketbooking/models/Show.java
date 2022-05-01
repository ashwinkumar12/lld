package com.lld.ashwinkumar.theatreticketbooking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Show {

    private String id;
    private Movie movie;
    private Screen screen;
    private Date startTime;
    private Integer showDuration;

}
