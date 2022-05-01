package com.lld.ashwinkumar.theatreticketbooking.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Theatre {

    private String id;
    private String name;
    private List<Screen> screens;

    public Theatre (String id, String name) {
        this.id = id;
        this.name = name;
        this.screens = new ArrayList<>();
    }

    public void addScreen(Screen screen) {
        this.screens.add(screen);
    }

}
