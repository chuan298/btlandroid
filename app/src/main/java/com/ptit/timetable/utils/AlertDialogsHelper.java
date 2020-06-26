package com.ptit.timetable.utils;

import android.app.Activity;

import android.support.v4.view.ViewPager;


import com.ptit.timetable.adapters.FragmentsTabAdapter;

import com.ptit.timetable.model.Subject;

import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Ulan on 22.10.2018.
 */
public class AlertDialogsHelper {


    public static void getAddSubjectDialog(final Activity activity, final ArrayList<Subject> subjectArrays, final FragmentsTabAdapter adapter, final ViewPager viewPager) {
        DbHelper dbHelper = new DbHelper(activity);
        Subject subject = new Subject();
        for (Subject w : subjectArrays) {
            subject.setSubject_name(w.getSubject_name());
            Matcher fragment = Pattern.compile("(.*Fragment)").matcher(w.getFragment());
            subject.setFragment(fragment.find() ? fragment.group() : null);
            subject.setTeacher(w.getTeacher());
            subject.setRoom(w.getRoom());
            subject.setColor(w.getColor());
            subject.setFromTime(w.getFromTime());
            subject.setToTime(w.getToTime());
            dbHelper.insertWeek(subject);
            adapter.notifyDataSetChanged();
        }
    }
}