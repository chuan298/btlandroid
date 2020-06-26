package com.ptit.timetable.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ptit.timetable.model.Teacher;
import com.ptit.timetable.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Ulan on 08.10.2018.
 */
public class HistoryAdapter extends ArrayAdapter<Teacher> {

    private Activity mActivity;
    private int mResource;
    private ArrayList<Teacher> teacherlist;
    private Teacher teacher;
    private ListView mListView;

    private static class ViewHolder {
        TextView name;
        TextView post;
        TextView phonenumber;
        TextView email;
        CardView cardView;
        ImageView popup;
    }

    public HistoryAdapter(Activity activity, ListView listView, int resource, ArrayList<Teacher> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mListView = listView;
        mResource = resource;
        teacherlist = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        String name = Objects.requireNonNull(getItem(position)).getName();
        String post = Objects.requireNonNull(getItem(position)).getPost();
        String phonenumber = Objects.requireNonNull(getItem(position)).getPhonenumber();
        String email = Objects.requireNonNull(getItem(position)).getEmail();
        int color = Objects.requireNonNull(getItem(position)).getColor();

        teacher = new Teacher(name, post, phonenumber, email, color);
        final ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.name = convertView.findViewById(R.id.nameteacher);
            holder.post = convertView.findViewById(R.id.postteacher);
            holder.phonenumber = convertView.findViewById(R.id.numberteacher);
            holder.email = convertView.findViewById(R.id.emailteacher);
            holder.cardView = convertView.findViewById(R.id.teacher_cardview);
            holder.popup = convertView.findViewById(R.id.popupbtn);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(teacher.getName());
        holder.post.setText(teacher.getPost());
        holder.phonenumber.setText(teacher.getPhonenumber());
        holder.email.setText(teacher.getEmail());
        holder.cardView.setCardBackgroundColor(teacher.getColor());


        hidePopUpMenu(holder);

        return convertView;
    }

    public ArrayList<Teacher> getTeacherList() {
        return teacherlist;
    }

    public Teacher getTeacher() {
        return teacher;
    }

     private void hidePopUpMenu(ViewHolder holder) {
        SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();
        if (checkedItems.size() > 0) {
            for (int i = 0; i < checkedItems.size(); i++) {
                int key = checkedItems.keyAt(i);
                if (checkedItems.get(key)) {
                    holder.popup.setVisibility(View.INVISIBLE);
                    }
            }
        } else {
            holder.popup.setVisibility(View.VISIBLE);
        }
    }
}