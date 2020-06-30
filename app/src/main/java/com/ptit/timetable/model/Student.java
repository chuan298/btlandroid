package com.ptit.timetable.model;

public class Student {
    private int id;

    private String username;


    private String password;


    private String name;


    private String birthday;


    private String phone;

    private String avatar;

    public Student(int id, String username, String password, String name, String birthday, String phone, String avatar) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.birthday = birthday;
        this.phone = phone;
        this.avatar = avatar;
    }

    public Student() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getAvatar() {
        return avatar;
    }
}
