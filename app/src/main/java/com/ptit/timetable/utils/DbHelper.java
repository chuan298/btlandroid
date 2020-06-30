package com.ptit.timetable.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ptit.timetable.model.DaySchedule;
import com.ptit.timetable.model.ScheduleCourse;
import com.ptit.timetable.model.Subject_;
import com.ptit.timetable.model.Teacher;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ulan on 07.09.2018.
 */
public class DbHelper extends SQLiteOpenHelper{

    private static final int DB_VERSION = 6;
    private static final String DB_NAME = "timetabledb";
    private static final String TIMETABLE = "timetable";
    private static final String WEEK_ID = "id";
    private static final String WEEK_SUBJECT = "subject";
    private static final String WEEK_FRAGMENT = "fragment";
    private static final String WEEK_TEACHER = "teacher";
    private static final String WEEK_ROOM = "room";
    private static final String WEEK_FROM_TIME = "fromtime";
    private static final String WEEK_TO_TIME = "totime";
    private static final String WEEK_COLOR = "color";

    private static final String HOMEWORKS = "homeworks";
    private static final String HOMEWORKS_ID  = "id";
    private static final String HOMEWORKS_SUBJECT = "subject";
    private static final String HOMEWORKS_DESCRIPTION = "description";
    private static final String HOMEWORKS_DATE = "date";
    private static final String HOMEWORKS_COLOR = "color";

    private static final String NOTES = "notes";
    private static final String NOTES_ID = "id";
    private static final String NOTES_TITLE = "title";
    private static final String NOTES_TEXT = "text";
    private static final String NOTES_COLOR = "color";

    private static final String TEACHERS = "teachers";
    private static final String TEACHERS_ID = "id";
    private static final String TEACHERS_NAME = "name";
    private static final String TEACHERS_POST = "post";
    private static final String TEACHERS_PHONE_NUMBER = "phonenumber";
    private static final String TEACHERS_EMAIL = "email";
    private static final String TEACHERS_COLOR = "color";

    private static final String EXAMS = "exams";
    private static final String EXAMS_ID = "id";
    private static final String EXAMS_SUBJECT = "subject";
    private static final String EXAMS_TEACHER = "teacher";
    private static final String EXAMS_ROOM = "room";
    private static final String EXAMS_DATE = "date";
    private static final String EXAMS_TIME = "time";
    private static final String EXAMS_COLOR = "color";

    Context context;
    public DbHelper(Context context){
        super(context , DB_NAME, null, DB_VERSION);
    }

     public void onCreate(SQLiteDatabase db) {
        String CREATE_TIMETABLE = "CREATE TABLE " + TIMETABLE + "("
                + WEEK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WEEK_SUBJECT + " TEXT,"
                + WEEK_FRAGMENT + " TEXT,"
                + WEEK_TEACHER + " TEXT,"
                + WEEK_ROOM + " TEXT,"
                + WEEK_FROM_TIME + " TEXT,"
                + WEEK_TO_TIME + " TEXT,"
                + WEEK_COLOR + " INTEGER" +  ")";

        String CREATE_HOMEWORKS = "CREATE TABLE " + HOMEWORKS + "("
                + HOMEWORKS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + HOMEWORKS_SUBJECT + " TEXT,"
                + HOMEWORKS_DESCRIPTION + " TEXT,"
                + HOMEWORKS_DATE + " TEXT,"
                + HOMEWORKS_COLOR + " INTEGER" + ")";

        String CREATE_NOTES = "CREATE TABLE " + NOTES + "("
                + NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NOTES_TITLE + " TEXT,"
                + NOTES_TEXT + " TEXT,"
                + NOTES_COLOR + " INTEGER" + ")";

        String CREATE_TEACHERS = "CREATE TABLE " + TEACHERS + "("
                + TEACHERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TEACHERS_NAME + " TEXT,"
                + TEACHERS_POST + " TEXT,"
                + TEACHERS_PHONE_NUMBER + " TEXT,"
                + TEACHERS_EMAIL + " TEXT,"
                + TEACHERS_COLOR + " INTEGER" + ")";

        String CREATE_EXAMS = "CREATE TABLE " + EXAMS + "("
                + EXAMS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EXAMS_SUBJECT + " TEXT,"
                + EXAMS_TEACHER + " TEXT,"
                + EXAMS_ROOM + " TEXT,"
                + EXAMS_DATE + " TEXT,"
                + EXAMS_TIME + " TEXT,"
                + EXAMS_COLOR + " INTEGER" + ")";

        db.execSQL(CREATE_TIMETABLE);
        db.execSQL(CREATE_HOMEWORKS);
        db.execSQL(CREATE_NOTES);
        db.execSQL(CREATE_TEACHERS);
        db.execSQL(CREATE_EXAMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("DROP TABLE IF EXISTS " + TIMETABLE);

            case 2:
                db.execSQL("DROP TABLE IF EXISTS " + HOMEWORKS);

            case 3:
                db.execSQL("DROP TABLE IF EXISTS " + NOTES);

            case 4:
                db.execSQL("DROP TABLE IF EXISTS " + TEACHERS);

            case 5:
                db.execSQL("DROP TABLE IF EXISTS " + EXAMS);
                break;
        }
        onCreate(db);
    }

    /**
     * Methods for Week fragments
     **/
    public void insertWeek(Subject_ subject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEEK_SUBJECT, subject.getSubject_name());
        contentValues.put(WEEK_FRAGMENT, subject.getFragment());
        contentValues.put(WEEK_TEACHER, subject.getTeacher());
        contentValues.put(WEEK_ROOM, subject.getRoom());
        contentValues.put(WEEK_FROM_TIME, subject.getFromTime());
        contentValues.put(WEEK_TO_TIME, subject.getToTime());
        contentValues.put(WEEK_COLOR, subject.getColor());
        db.insert(TIMETABLE,null, contentValues);
        db.update(TIMETABLE, contentValues, WEEK_FRAGMENT, null);
        db.close();
    }

    public void deleteWeekById(Subject_ subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TIMETABLE, WEEK_ID + " = ? ", new String[]{String.valueOf(subject.getId())});
        db.close();
    }

    public void updateWeek(Subject_ subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEEK_SUBJECT, subject.getSubject_name());
        contentValues.put(WEEK_TEACHER, subject.getTeacher());
        contentValues.put(WEEK_ROOM, subject.getRoom());
        contentValues.put(WEEK_FROM_TIME, subject.getFromTime());
        contentValues.put(WEEK_TO_TIME, subject.getToTime());
        contentValues.put(WEEK_COLOR, subject.getColor());
        db.update(TIMETABLE, contentValues, WEEK_ID + " = " + subject.getId(), null);
        db.close();
    }

    public ArrayList<Subject_> getWeek(Integer fragment){
        SQLiteDatabase db = this.getWritableDatabase();

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
        ArrayList<DaySchedule> daySchedules = gson.fromJson(sharedPreferences.getString("1", " "), complexType);
        for(DaySchedule daySchedule: daySchedules){
            if(daySchedule.getDay_number().equals(fragment)){
                for(ScheduleCourse scheduleCourse : daySchedule.getScheduleCourseList()){
                    subject = new Subject_();
                    subject.setId(scheduleCourse.getSchedule().getId());
                    subject.setSubject_name(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getSubject().getName());
                    subject.setTeacher("An");
                    if(scheduleCourse.getTypeSchedule() == 3){
                        subject.setRoom(scheduleCourse.getSchedule().getPracticeGroup().getPracticeRoom().getName());
                        subject.setFromTime(getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getPracticeShift().getStartLesson()));
                        subject.setToTime(getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getPracticeShift().getEndLesson()));
                    }
                    else if(scheduleCourse.getTypeSchedule() == 2) {
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

    public void insertTeacher(Teacher teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEACHERS_NAME, teacher.getName());
        contentValues.put(TEACHERS_POST, teacher.getPost());
        contentValues.put(TEACHERS_PHONE_NUMBER, teacher.getPhonenumber());
        contentValues.put(TEACHERS_EMAIL, teacher.getEmail());
        contentValues.put(TEACHERS_COLOR, teacher.getColor());
        db.insert(TEACHERS, null, contentValues);
        db.close();
    }

    public void updateTeacher(Teacher teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEACHERS_NAME, teacher.getName());
        contentValues.put(TEACHERS_POST, teacher.getPost());
        contentValues.put(TEACHERS_PHONE_NUMBER, teacher.getPhonenumber());
        contentValues.put(TEACHERS_EMAIL, teacher.getEmail());
        contentValues.put(TEACHERS_COLOR, teacher.getColor());
        db.update(TEACHERS, contentValues, TEACHERS_ID + " = " + teacher.getId(), null);
        db.close();
    }

    public void deleteTeacherById(Teacher teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TEACHERS, TEACHERS_ID + " =? ", new String[] {String.valueOf(teacher.getId())});
        db.close();
    }

    public ArrayList<Teacher> getTeacher() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Teacher> teacherlist = new ArrayList<>();
        Teacher teacher;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TEACHERS, null);
        while (cursor.moveToNext()) {
            teacher = new Teacher();
            teacher.setId(cursor.getInt(cursor.getColumnIndex(TEACHERS_ID)));
            teacher.setName(cursor.getString(cursor.getColumnIndex(TEACHERS_NAME)));
            teacher.setPost(cursor.getString(cursor.getColumnIndex(TEACHERS_POST)));
            teacher.setPhonenumber(cursor.getString(cursor.getColumnIndex(TEACHERS_PHONE_NUMBER)));
            teacher.setEmail(cursor.getString(cursor.getColumnIndex(TEACHERS_EMAIL)));
            teacher.setColor(cursor.getInt(cursor.getColumnIndex(TEACHERS_COLOR)));
            teacherlist.add(teacher);
        }
        cursor.close();
        db.close();
        return teacherlist;
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
