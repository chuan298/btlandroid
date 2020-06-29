package com.ptit.timetable.model;

public class TokenDecode {
    private Integer id;
    private String name, username;

    public TokenDecode(Integer id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public TokenDecode() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "TokenDecode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
