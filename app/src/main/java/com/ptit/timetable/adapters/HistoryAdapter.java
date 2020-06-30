package com.ptit.timetable.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ptit.timetable.model.Attendance;
import com.ptit.timetable.model.Teacher;
import com.ptit.timetable.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Ulan on 08.10.2018.
 */
public class HistoryAdapter extends ArrayAdapter<Attendance> {

    private Activity mActivity;
    private int mResource;
    private ArrayList<Attendance> attendances;
    private Attendance attendance;
    private ListView mListView;

    private static class ViewHolder {
        TextView subject;
        TextView room;
        TextView teacher;
        TextView time;
        ImageView image;
        CardView cardView;
        ImageView popup;
    }

    public HistoryAdapter(Activity activity, ListView listView, int resource, ArrayList<Attendance> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mListView = listView;
        mResource = resource;
        attendances = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        String subject = Objects.requireNonNull(getItem(position)).getStudentCourse().getSubjectGroup().getSubject().getName();
        String room = "";
        if(Objects.requireNonNull(getItem(position)).getType_schedule() == 3){
            room = Objects.requireNonNull(getItem(position)).getSchedule().getPracticeGroup().getPracticeRoom().getName();
        }
        else if(Objects.requireNonNull(getItem(position)).getType_schedule() == 2) {
            room = Objects.requireNonNull(getItem(position)).getSchedule().getPracticeGroup().getSubjectGroup().getRoom().getName();
        }
        else if(Objects.requireNonNull(getItem(position)).getType_schedule() == 1) {
            room = Objects.requireNonNull(getItem(position)).getSchedule().getPracticeGroup().getSubjectGroup().getRoom().getName();
        }
//        String room = Objects.requireNonNull(getItem(position)).ge;
        String time = Objects.requireNonNull(getItem(position)).getTime();
        String teacher = Objects.requireNonNull(getItem(position)).getStudentCourse().getSubjectGroup().getTeacher();
        String image = Objects.requireNonNull(getItem(position)).getImage();
//        String phonenumber = Objects.requireNonNull(getItem(position)).getPhonenumber();
//        String email = Objects.requireNonNull(getItem(position)).getEmail();
//        int color = Objects.requireNonNull(getItem(position)).getColor();

        //attendance = new Attendance(0, );
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.subject = convertView.findViewById(R.id.tv_name);
            holder.time = convertView.findViewById(R.id.tv_time_history);
            holder.room = convertView.findViewById(R.id.tv_room);
            holder.teacher = convertView.findViewById(R.id.tv_name_teacher);
            holder.cardView = convertView.findViewById(R.id.teacher_cardview);
            holder.popup = convertView.findViewById(R.id.popupbtn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.subject.setText(subject);
        holder.teacher.setText(teacher);
        holder.room.setText(room);
        holder.time.setText(time);
        //holder.image.setImageBitmap(convertStringBase64ToBitmap(image));
//        holder.cardView.setCardBackgroundColor(teacher.getColor());


//        hidePopUpMenu(holder);

        return convertView;
    }

//    public ArrayList<Teacher> getTeacherList() {
//        return teacherlist;
//    }

//    public Teacher getTeacher() {
//        return teacher;
//    }

//    private void hidePopUpMenu(ViewHolder holder) {
//        SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();
//        if (checkedItems.size() > 0) {
//            for (int i = 0; i < checkedItems.size(); i++) {
//                int key = checkedItems.keyAt(i);
//                if (checkedItems.get(key)) {
//                    holder.popup.setVisibility(View.INVISIBLE);
//                }
//            }
//        } else {
//        }
//    }
    private static Bitmap convertStringBase64ToBitmap(String imgbase64){
        byte[] decodedString = Base64.decode(imgbase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}