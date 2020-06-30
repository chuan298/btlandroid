package com.ptit.timetable.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ptit.timetable.adapters.WeekAdapter;
import com.ptit.timetable.utils.DbHelper;
import com.ptit.timetable.R;
import com.ptit.timetable.utils.DbUtils;
import com.ptit.timetable.utils.FragmentHelper;


@SuppressLint("ValidFragment")
public class ThursdayFragment extends Fragment {

    public static final int KEY_THURSDAY_FRAGMENT = 5;
    private DbUtils db;
    private ListView listView;
    private WeekAdapter adapter;
    private String week;

    @SuppressLint("ValidFragment")
    public ThursdayFragment(String week) {
        this.week = week;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thursday, container, false);
        setupAdapter(view);
        //setupListViewMultiSelect();
        return view;
    }

    private void setupAdapter(View view) {
        db = new DbUtils(getActivity());
        listView = view.findViewById(R.id.thursdaylist);
        adapter = new WeekAdapter(getActivity(), listView, R.layout.listview_week_adapter, db.getWeek(KEY_THURSDAY_FRAGMENT, week));
        listView.setAdapter(adapter);
    }

//    private void setupListViewMultiSelect() {
//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        listView.setMultiChoiceModeListener(FragmentHelper.setupListViewMultiSelect(getActivity(), listView, adapter, db));
//    }
}