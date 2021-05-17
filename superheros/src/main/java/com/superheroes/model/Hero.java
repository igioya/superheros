package com.superheroes.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Hero implements Serializable {
    private Long id;
    private String name;

    public Hero(){}

    public Hero(String heroname) {
        this.name = heroname;
    }

    public Hero(Long heroid, String heroname) {
        this.name = heroname;
        this.id = heroid;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
}
