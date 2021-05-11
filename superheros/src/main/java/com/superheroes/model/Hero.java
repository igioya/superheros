package com.superheroes.model;

import java.io.Serializable;

public class Hero implements Serializable {
    private String name;

    public Hero(String heroname) {
        this.name = heroname;
    }

    public void setName(String name) {
        this.name = name;
    }
}
