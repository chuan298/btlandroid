package com.ptit.timetable.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ptit.timetable.adapters.HistoryAdapter;
import com.ptit.timetable.adapters.WeekAdapter;
import com.ptit.timetable.model.Attendance;
import com.ptit.timetable.model.Teacher;
import com.ptit.timetable.R;
import com.ptit.timetable.utils.DbHelper;
import com.ptit.timetable.utils.DbUtils;
import com.ptit.timetable.utils.HttpServices;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class HistoryCheckinActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context context = this;
    private ListView listView;
    private DbHelper db;
    private HistoryAdapter adapter;
    private String NAME = "";
    private String USERNAME = "";
    int ID = 0;
    final String BASE_URL = "http://192.168.1.67:8080";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historycheckin);
        listView = findViewById(R.id.historylist);
        //
        HttpServices.setContext(getBaseContext());
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferenceManager", MODE_PRIVATE);
        NAME = sharedPreferences.getString("NAME", " ");
        USERNAME = sharedPreferences.getString("USERNAME", " ");
        ID = sharedPreferences.getInt("ID", 0);
        //
        HttpServices.setContext(getBaseContext());

        HttpServices.getWithToken(BASE_URL + "/api/get-attendance?student_id=" + ID, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "Có lỗi xảy ra!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    Type complexType = new TypeToken<List<Attendance>>() {}.getType();
                    final List<Attendance> attendances = gson.fromJson(response.body().string(), complexType);
                    System.out.println(attendances);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new HistoryAdapter(HistoryCheckinActivity.this, listView, R.layout.listview_history_adapter, (ArrayList<Attendance>) attendances);
                            listView.setAdapter(adapter);
                        }
                    });
                }
            }
        });


        initAll();
    }

    private void initAll() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        View headerView = navigationView.getHeaderView(0);
        TextView tvName = headerView.findViewById(R.id.tvHeaderName);
        TextView tvUsername = headerView.findViewById(R.id.tvHeaderUserName);
        tvName.setText(NAME);
        tvUsername.setText(USERNAME);
        //
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //setupAdapter();
        //setupListViewMultiSelect();
//        setupCustomDialog();
    }

//    private void setupAdapter() {
//        db = new DbHelper(context);
//        listView = findViewById(R.id.historylist);
//        adapter = new HistoryAdapter(HistoryCheckinActivity.this, listView, R.layout.listview_history_adapter, db.getTeacher());
//        listView.setAdapter(adapter);
//    }
//    private void setupAdapter(View view) {
//        db = new DbUtils(getBaseContext());
//        listView = view.findViewById(R.id.mondaylist);
//        adapter = new WeekAdapter(getActivity(), listView, R.layout.listview_week_adapter, db.getWeek(KEY_MONDAY_FRAGMENT, week));
//        listView.setAdapter(adapter);
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final NavigationView navigationView = findViewById(R.id.nav_view);
        switch (item.getItemId()) {
            case R.id.subjectCurrent:
                Intent subject = new Intent(HistoryCheckinActivity.this, SubjectCurrentActivity.class);
                startActivity(subject);
                return true;
            case R.id.history:
                Intent teacher = new Intent(HistoryCheckinActivity.this, HistoryCheckinActivity.class);
                startActivity(teacher);
                return true;
            case R.id.personInfo:
                Intent homework = new Intent(HistoryCheckinActivity.this, PersonInfoActivity.class);
                startActivity(homework);
                return true;
            case R.id.tkb:
                Toast.makeText(this,"chuyen sang trang Thời khoá biểu",Toast.LENGTH_SHORT).show();
                Intent note = new Intent(HistoryCheckinActivity.this, TKBActivity.class);
                startActivity(note);
                return true;
            default:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }
}
