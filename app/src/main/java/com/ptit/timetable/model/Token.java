package com.ptit.timetable.model;

public class Token {
    private String accessToken;
    private String tokenType;

    public Token(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
