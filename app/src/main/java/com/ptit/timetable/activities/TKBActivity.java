package com.ptit.timetable.activities;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.ptit.timetable.adapters.FragmentsTabAdapter;
import com.ptit.timetable.fragments.FridayFragment;
import com.ptit.timetable.fragments.MondayFragment;
import com.ptit.timetable.fragments.ThursdayFragment;
import com.ptit.timetable.fragments.TuesdayFragment;
import com.ptit.timetable.fragments.WednesdayFragment;
import com.ptit.timetable.R;
import com.ptit.timetable.model.Subject_;
import com.ptit.timetable.utils.AlertDialogsHelper;

import java.util.ArrayList;
import java.util.Calendar;


public class TKBActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentsTabAdapter adapter;
    private ViewPager viewPager;
//    private boolean switchSevenDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tkb);
        initAll();
    }

    private void initAll() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
