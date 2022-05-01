package com.lld.ashwinkumar.theatreticketbooking.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Screen {

    private String id;
    private String name;
    private Theatre theatre;
    private List<Seat> seatList;

    public Screen(String id, String name, Theatre theatre) {
        this.id = id;
        this.name = name;
        this.theatre = theatre;
        this.seatList = new ArrayList<>();
    }

    public void addSeat(Seat seat) {
        this.seatList.add(seat);
    }
}
