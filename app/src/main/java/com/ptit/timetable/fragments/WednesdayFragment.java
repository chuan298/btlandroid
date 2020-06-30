package com.ptit.timetable.fragments;

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


public class WednesdayFragment extends Fragment {

    public static final int KEY_WEDNESDAY_FRAGMENT = 4;
    private DbUtils db;
    private ListView listView;
    private WeekAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wednesday, container, false);
        setupAdapter(view);
//        setupListViewMultiSelect();
        return view;
    }

    private void setupAdapter(View view) {
        db = new DbUtils(getActivity());
        listView = view.findViewById(R.id.wednesdaylist);
        adapter = new WeekAdapter(getActivity(), listView, R.layout.listview_week_adapter, db.getWeek(KEY_WEDNESDAY_FRAGMENT));
        listView.setAdapter(adapter);
    }

//    private void setupListViewMultiSelect() {
//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        listView.setMultiChoiceModeListener(FragmentHelper.setupListViewMultiSelect(getActivity(), listView, adapter, db));
//    }
}