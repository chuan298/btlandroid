package com.ptit.timetable.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ptit.timetable.adapters.FragmentsTabAdapter;
import com.ptit.timetable.fragments.FridayFragment;
import com.ptit.timetable.fragments.MondayFragment;
import com.ptit.timetable.fragments.ThursdayFragment;
import com.ptit.timetable.fragments.TuesdayFragment;
import com.ptit.timetable.fragments.WednesdayFragment;
import com.ptit.timetable.R;
import com.ptit.timetable.model.DaySchedule;
import com.ptit.timetable.model.Schedule;
import com.ptit.timetable.model.Subject_;
import com.ptit.timetable.model.Timetable;
import com.ptit.timetable.utils.AlertDialogsHelper;
import com.ptit.timetable.utils.HttpServices;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class TKBActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentsTabAdapter adapter;
    private ViewPager viewPager;
//    private boolean switchSevenDays;
    private String NAME = "";
    private String USERNAME = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferenceManager", MODE_PRIVATE);
        NAME = sharedPreferences.getString("NAME", " ");
        USERNAME = sharedPreferences.getString("USERNAME", " ");
        int ID = sharedPreferences.getInt("ID", 0);

        HttpServices.getWithToken("http://192.168.1.67:8080/api/get-timetable?student_id=" + ID, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    String responStr = response.body().string();
                    //System.out.println(responStr);
                    Type complexType = new TypeToken<Map<Integer, DaySchedule>>() {}.getType();
                    Map<Integer, Map<Integer, List<Pair<Schedule, Integer>>>> timetable = gson.fromJson(responStr, complexType);
//                    Set<Map.Entry<Integer, Map<Integer, List<Pair<Schedule, Integer>>>> entries = timetable.entrySet();
//                    for( Map.Entry<Integer, List<Pair<Schedule, Integer>>>> entry : entries){
//                        System.out.println(entry.getValue().getSchedule());
//                    }
                }
            }
        });


        setContentView(R.layout.activity_tkb);
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
        setupFragments();
        setupCustomDialog();
    }

    private void setupFragments() {
        adapter = new FragmentsTabAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        adapter.addFragment(new MondayFragment(), getResources().getString(R.string.monday));
        adapter.addFragment(new TuesdayFragment(), getResources().getString(R.string.tuesday));
        adapter.addFragment(new WednesdayFragment(), getResources().getString(R.string.wednesday));
        adapter.addFragment(new ThursdayFragment(), getResources().getString(R.string.thursday));
        adapter.addFragment(new FridayFragment(), getResources().getString(R.string.friday));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(day == 1 ? 6 : day-2, true);
        tabLayout.setupWithViewPager(viewPager);
    }

    
    private void setupCustomDialog() {
        ArrayList<Subject_> subjectArrayList = new ArrayList<>();
        subjectArrayList.add(new Subject_("Lap trinh java","Nguyen Manh Son","402-A2","9:00","11:00", Color.GREEN,"monday"));
        subjectArrayList.add(new Subject_("Lap trinh C++","Nguyen Manh Son","402-A2","9:00","11:00", Color.YELLOW,"tuesday"));
        subjectArrayList.add(new Subject_("Lap trinh Android","Nguyen Manh Son","402-A2","9:00","11:00", Color.LTGRAY,"thursday"));
        subjectArrayList.add(new Subject_("Lap trinh C#","Nguyen Manh Son","402-A2","9:00","11:00", Color.BLUE,"monday"));
        AlertDialogsHelper.getAddSubjectDialog(TKBActivity.this, subjectArrayList, adapter, viewPager);
    }




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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final NavigationView navigationView = findViewById(R.id.nav_view);
        switch (item.getItemId()) {
            case R.id.subjectCurrent:
                Intent subject = new Intent(TKBActivity.this, SubjectCurrentActivity.class);
                startActivity(subject);
                return true;
            case R.id.history:
                Intent teacher = new Intent(TKBActivity.this, HistoryCheckinActivity.class);
                startActivity(teacher);
                return true;
            case R.id.personInfo:
                Intent homework = new Intent(TKBActivity.this, PersonInfoActivity.class);
                startActivity(homework);
                return true;
            case R.id.tkb:
                Toast.makeText(this,"chuyen sang trang Thời khoá biểu",Toast.LENGTH_SHORT).show();
                Intent note = new Intent(TKBActivity.this, TKBActivity.class);
                startActivity(note);
                return true;
            default:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }

}
