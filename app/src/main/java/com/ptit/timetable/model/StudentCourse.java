package com.ptit.timetable.model;

public class StudentCourse {
    private Integer id;

    private Student student;

    private SubjectGroup subjectGroup;

    private Integer studyNumber;

    public StudentCourse() {
    }

    public StudentCourse(Integer id, Student student, SubjectGroup subjectGroup, Integer studyNumber) {
        this.id = id;
        this.student = student;
        this.subjectGroup = subjectGroup;
        this.studyNumber = studyNumber;
    }

    public Integer getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public SubjectGroup getSubjectGroup() {
        return subjectGroup;
    }

    public Integer getStudyNumber() {
        return studyNumber;
    }
}
