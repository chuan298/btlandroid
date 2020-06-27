package com.ptit.timetable.model;

public class Room {
    private Integer id;

    private String name;

    public Room() {
    }

    public Room(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
