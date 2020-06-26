package com.ptit.timetable.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.ptit.timetable.R;
import com.ptit.timetable.utils.DbHelper;


public class PersonInfoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context context = this;
    private ListView listView;
    private DbHelper db;
    private int listposition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
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
                Intent subject = new Intent(PersonInfoActivity.this, SubjectCurrentActivity.class);
                startActivity(subject);
                return true;
            case R.id.history:
                Intent teacher = new Intent(PersonInfoActivity.this, HistoryCheckinActivity.class);
                startActivity(teacher);
                return true;
            case R.id.personInfo:
                Intent homework = new Intent(PersonInfoActivity.this, PersonInfoActivity.class);
                startActivity(homework);
                return true;
            case R.id.tkb:
                Toast.makeText(this,"chuyen sang trang Thời khoá biểu",Toast.LENGTH_SHORT).show();
                Intent note = new Intent(PersonInfoActivity.this, TKBActivity.class);
                startActivity(note);
                return true;
            default:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }


}
