package com.ptit.timetable.model;

import java.io.Serializable;

public class RecognizeResponse implements Serializable {
    private static final long serialVersionUID = 191515111265519L;
    String maSv;
    Double top_left_x, top_left_y, bottom_right_x, bottom_right_y;

    public RecognizeResponse(String maSv, Double top_left_x, Double top_left_y, Double bottom_right_x, Double bottom_right_y) {
        this.maSv = maSv;
        this.top_left_x = top_left_x;
        this.top_left_y = top_left_y;
        this.bottom_right_x = bottom_right_x;
        this.bottom_right_y = bottom_right_y;
    }

    public RecognizeResponse() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMaSv() {
        return maSv;
    }

    public Double getTop_left_x() {
        return top_left_x;
    }

    public Double getTop_left_y() {
        return top_left_y;
    }

    public Double getBottom_right_x() {
        return bottom_right_x;
    }

    public Double getBottom_right_y() {
        return bottom_right_y;
    }

    @Override
    public String toString() {
        return "RecognizeResponse{" +
                "maSv='" + maSv + '\'' +
                ", top_left_x=" + top_left_x +
                ", top_left_y=" + top_left_y +
                ", bottom_right_x=" + bottom_right_x +
                ", bottom_right_y=" + bottom_right_y +
                '}';
    }
}