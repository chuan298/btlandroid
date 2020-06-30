package com.ptit.timetable.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ptit.timetable.model.DaySchedule;
import com.ptit.timetable.model.ScheduleCourse;
import com.ptit.timetable.model.Subject_;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DbUtils {
    Context context;

    public DbUtils(Context context) {
        this.context = context;
    }

    public ArrayList<Subject_> getWeek(Integer day, String week){


        ArrayList<Subject_> weeklist = new ArrayList<>();
        Subject_ subject;

        SharedPreferences sharedPreferences = context.getSharedPreferences("Timetable", Context.MODE_PRIVATE);
//        Cursor cursor = db.rawQuery("SELECT * FROM ( SELECT * FROM "+TIMETABLE+" ORDER BY " + WEEK_FROM_TIME + " ) WHERE "+ WEEK_FRAGMENT +" LIKE '"+fragment+"%'",null);
//        while (cursor.moveToNext()){
//            subject = new Subject_();
//            subject.setId(cursor.getInt(cursor.getColumnIndex(WEEK_ID)));
//            subject.setSubject_name(cursor.getString(cursor.getColumnIndex(WEEK_SUBJECT)));
//            subject.setTeacher(cursor.getString(cursor.getColumnIndex(WEEK_TEACHER)));
//            subject.setRoom(cursor.getString(cursor.getColumnIndex(WEEK_ROOM)));
//            subject.setFromTime(cursor.getString(cursor.getColumnIndex(WEEK_FROM_TIME)));
//            subject.setToTime(cursor.getString(cursor.getColumnIndex(WEEK_TO_TIME)));
//            subject.setColor(cursor.getInt(cursor.getColumnIndex(WEEK_COLOR)));
//            weeklist.add(subject);
//        }
        Gson gson = new Gson();
        Type complexType = new TypeToken<ArrayList<DaySchedule>>() {}.getType();
        week = week.split(" ")[1];
        ArrayList<DaySchedule> daySchedules = gson.fromJson(sharedPreferences.getString(week, " "), complexType);
        System.out.println("data: " + daySchedules.toString());
        for(DaySchedule daySchedule: daySchedules){
            System.out.println("daySchedule: " + daySchedule);
            if(daySchedule.getDay_number().equals(day)){
                for(ScheduleCourse scheduleCourse : daySchedule.getScheduleCourseList()){
                    subject = new Subject_();
                    subject.setId(scheduleCourse.getSchedule().getId());
                    subject.setSubject_name(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getSubject().getName());
                    subject.setTeacher(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getTeacher());
                    if(scheduleCourse.getTypeSchedule() == 3){
                        subject.setRoom(scheduleCourse.getSchedule().getPracticeGroup().getPracticeRoom().getName());
                        subject.setFromTime(getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getPracticeShift().getStartLesson()));
                        subject.setToTime(getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getPracticeShift().getEndLesson()));
                    }
                    else if(scheduleCourse.getTypeSchedule() == 2) {
                        System.out.println("dsadsad " + scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getShift2().getStartLesson());
                        subject.setRoom(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getRoom().getName());
                        subject.setFromTime(getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getShift2().getStartLesson()));
                        subject.setToTime(getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getShift2().getEndLesson()));
                    }
                    else if(scheduleCourse.getTypeSchedule() == 1) {
                        subject.setRoom(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getRoom().getName());
                        subject.setFromTime(getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getShift1().getStartLesson()));
                        subject.setToTime(getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getShift1().getEndLesson()));
                    }
                    weeklist.add(subject);
                }

                break;
            }
        }
        return  weeklist;
    }
    public static String getTimeFromShift(int i){
        String time = "";
        switch (i){
            case 1:
                time = "07:00";
                break;
            case 2:
                time = "08:50";
                break;
            case 3:
                time = "09:00";
                break;
            case 4:
                time = "10:50";
                break;
            case 5:
                time = "12:00";
                break;
            case 6:
                time = "13:50";
                break;
            case 7:
                time = "14:00";
                break;
            case 8:
                time = "15:50";
                break;
            case 9:
                time = "16:00";
                break;
            case 10:
                time = "17:50";
                break;
            case 11:
                time = "18:00";
                break;
            case 12:
                time = "19:50";
                break;
        }
        return time;
    }
}
