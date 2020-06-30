package com.ptit.timetable.adapters;

import android.annotation.SuppressLint;
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

import com.ptit.timetable.R;
import com.ptit.timetable.model.Subject_;

import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by Ulan on 08.09.2018.
 */
public class WeekAdapter extends ArrayAdapter<Subject_> {

    private Activity mActivity;
    private int mResource;
    private ArrayList<Subject_> weeklist;
    private Subject_ subject;
    private ListView mListView;

    private static class ViewHolder {
        TextView subject;
        TextView teacher;
        TextView time;
        TextView room;
        ImageView popup;
        CardView cardView;
    }

    public WeekAdapter(Activity activity, ListView listView, int resource, ArrayList<Subject_> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mResource = resource;
        weeklist = objects;
        mListView = listView;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        String subject = Objects.requireNonNull(getItem(position)).getSubject_name();
        String teacher = Objects.requireNonNull(getItem(position)).getTeacher();
        String time_from = Objects.requireNonNull(getItem(position)).getFromTime();
        String time_to = Objects.requireNonNull(getItem(position)).getToTime();
        String room = Objects.requireNonNull(getItem(position)).getRoom();
        int color = getItem(position).getColor();
        String day = getItem(position).getFragment();
        this.subject = new Subject_(subject, teacher, room, time_from, time_to, color,day);
        final ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.subject = convertView.findViewById(R.id.subject);
            holder.teacher = convertView.findViewById(R.id.teacher);
            holder.time = convertView.findViewById(R.id.time);
            holder.room = convertView.findViewById(R.id.room);
            holder.popup = convertView.findViewById(R.id.popupbtn);
            holder.cardView = convertView.findViewById(R.id.week_cardview);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.subject.setText(this.subject.getSubject_name());
        holder.teacher.setText(this.subject.getTeacher());
        holder.room.setText(this.subject.getRoom());
        holder.time.setText(this.subject.getFromTime() + " - " + this.subject.getToTime());
        holder.cardView.setCardBackgroundColor(this.subject.getColor());

        //hidePopUpMenu(holder);

        return convertView;
    }

    public ArrayList<Subject_> getWeekList() {
        return weeklist;
    }

    public Subject_ getSubject() {
        return subject;
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