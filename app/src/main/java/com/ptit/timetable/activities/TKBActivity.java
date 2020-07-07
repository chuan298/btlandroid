package com.ptit.timetable.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ptit.timetable.Constant;
import com.ptit.timetable.adapters.FragmentsTabAdapter;
import com.ptit.timetable.fragments.FridayFragment;
import com.ptit.timetable.fragments.MondayFragment;
import com.ptit.timetable.fragments.SaturdayFragment;
import com.ptit.timetable.fragments.SundayFragment;
import com.ptit.timetable.fragments.ThursdayFragment;
import com.ptit.timetable.fragments.TuesdayFragment;
import com.ptit.timetable.fragments.WednesdayFragment;
import com.ptit.timetable.R;
import com.ptit.timetable.model.DaySchedule;
import com.ptit.timetable.model.Schedule;
import com.ptit.timetable.model.ScheduleCourse;
import com.ptit.timetable.model.Subject_;
import com.ptit.timetable.model.Timetable;
import com.ptit.timetable.utils.AlertDialogsHelper;
import com.ptit.timetable.utils.HttpServices;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
    private Spinner spinnerWeek;
//    private boolean switchSevenDays;
    private String NAME = "";
    private String USERNAME = "";
    private String weeks[] = {"Tuần 1 (01/06 - 07/06)", "Tuần 2 (08/06 - 14/06)", "Tuần 3 (15/06 - 21/06)", "Tuần 4 (22/06 - 28/06)", "Tuần 5 (29/06 - 05/07)", "Tuần 6 (06/07 - 12/07)", "Tuần 7 (13/07 - 19/07)", "Tuần 8 (20/07 - 26/07)", "Tuần 9 (27/07 - 02/08)", "Tuần 10 (03/08 - 09/08)"};
    private String week = "Tuần 1";

    private Map<Integer, List<DaySchedule>> timetable;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tkb);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferenceManager", MODE_PRIVATE);
        NAME = sharedPreferences.getString("NAME", " ");
        USERNAME = sharedPreferences.getString("USERNAME", " ");
        int ID = sharedPreferences.getInt("ID", 0);

        HttpServices.getWithToken(Constant.BASE_URL + "/api/get-timetable?student_id=" + ID, new Callback() {
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
                    SharedPreferences sharedPreferencesTimeTable = getSharedPreferences("Timetable", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesTimeTable.edit();
                    //
                    String responStr = response.body().string();
                    System.out.println(responStr);
                    Type complexType = new TypeToken<Map<Integer, List<DaySchedule>>>() {}.getType();
                    timetable = gson.fromJson(responStr, complexType);
                    Set<Map.Entry<Integer, List<DaySchedule>>> entries = timetable.entrySet();
                    for( Map.Entry<Integer, List<DaySchedule>> entry : entries){
                        editor.putString(entry.getKey() + "", gson.toJson(entry.getValue()));
                    }
                    editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }
                    });

                }
            }
        });
        initAll();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        spinnerWeek = findViewById(R.id.spinnerWeek);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_item_week, weeks);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeek.setAdapter(arrayAdapter);
        //
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            LocalDateTime now = LocalDateTime.now();
            String timeNow = dtf.format(now);
            long week1 = calculateBetwenDateAndDate(Constant.SCHOOL_TIME_START, timeNow) / 7;
            week1 = week1 % 7 == 0 ? week1 : week1 + 1;

            week = "Tuần " + week1;
            spinnerWeek.setSelection((int) (week1-1));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //
        spinnerWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                week = weeks[i];
                setupFragments();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });
        //
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setupFragments();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupFragments() {
        adapter = new FragmentsTabAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        adapter.addFragment(new MondayFragment(week), getResources().getString(R.string.monday));
        adapter.addFragment(new TuesdayFragment(week), getResources().getString(R.string.tuesday));
        adapter.addFragment(new WednesdayFragment(week), getResources().getString(R.string.wednesday));
        adapter.addFragment(new ThursdayFragment(week), getResources().getString(R.string.thursday));
        adapter.addFragment(new FridayFragment(week), getResources().getString(R.string.friday));
        adapter.addFragment(new SaturdayFragment(week), getResources().getString(R.string.saturday));
        adapter.addFragment(new SundayFragment(week), getResources().getString(R.string.sunday));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(day == 1 ? 6 : day-2, true);
        tabLayout.setupWithViewPager(viewPager);
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
                //Toast.makeText(this,"chuyen sang trang Thời khoá biểu",Toast.LENGTH_SHORT).show();
                Intent note = new Intent(TKBActivity.this, TKBActivity.class);
                startActivity(note);
                return true;
            default:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }
    public static Long calculateBetwenDateAndDate(String date1, String date2) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        Date firstDate = sdf.parse(date1);
        Date secondDate = sdf.parse(date2);
        System.out.println(firstDate + " " + secondDate);
        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());

        return diffInMillies / (24 * 60 * 60 * 1000);
    }

}
