package com.ptit.timetable.model;

import java.io.Serializable;

public class AttendanceResponse implements Serializable {
    private static final long serialVersionUID = 1915151112659L;

    private RecognizeResponse recognizeResponse;

    private Boolean isAttendant;

    private String message;

    public AttendanceResponse(RecognizeResponse recognizeResponse, Boolean isAttendant, String message) {
        this.recognizeResponse = recognizeResponse;
        this.isAttendant = isAttendant;
        this.message = message;
    }

    public AttendanceResponse() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public RecognizeResponse getRecognizeResponse() {
        return recognizeResponse;
    }

    public Boolean getIsAttendant() {
        return isAttendant;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "AttendanceResponse{" +
                "recognizeResponse=" + recognizeResponse +
                ", isAttendant=" + isAttendant +
                ", message='" + message + '\'' +
                '}';
    }
}