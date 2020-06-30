package com.ptit.timetable.model;

public class Attendance {
    private Integer id;

    private StudentCourse studentCourse;

    private String time;

    private String image;

    public Attendance() {
    }

    public Attendance(Integer id, StudentCourse studentCourse, String time, String image) {
        this.id = id;
        this.studentCourse = studentCourse;
        this.time = time;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public String getTime() {
        return time;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", studentCourse=" + studentCourse +
                ", time='" + time + '\'' +
                '}';
    }
}
