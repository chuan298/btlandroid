package com.ptit.timetable.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ptit.timetable.R;
import com.ptit.timetable.model.Student;
import com.ptit.timetable.utils.DbHelper;
import com.ptit.timetable.utils.HttpServices;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class PersonInfoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context context = this;
    private ListView listView;
    private DbHelper db;
    private int listposition = 0;
    final String BASE_URL = "http://73a24c38911b.ngrok.io";
    private String NAME = "";
    private String USERNAME = "";
    int ID = 0;
    private TextView tv_id;
    private TextView tv_name;
    private TextView tv_class;
    private TextView tv_ptit;
    private TextView tv_phone;
    private TextView tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
        //
        tv_class = findViewById(R.id.tv_class);
        tv_email = findViewById(R.id.tv_email);
        tv_name = findViewById(R.id.tv_student_name);
        tv_id = findViewById(R.id.tv_student_id);
        tv_phone = findViewById(R.id.tv_sdt);
        //
        HttpServices.setContext(getBaseContext());
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferenceManager", MODE_PRIVATE);
        NAME = sharedPreferences.getString("NAME", " ");
        USERNAME = sharedPreferences.getString("USERNAME", " ");
        ID = sharedPreferences.getInt("ID", 0);
        HttpServices.getWithToken(BASE_URL + "/api/get-info?student_id=" + ID, new Callback() {
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
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    String responseStr = response.body().string();
                    final Student student = gson.fromJson(responseStr, Student.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), student.toString(), Toast.LENGTH_LONG).show();
                            Log.d("PersonInfo", "run: student null = "+(student==null));
                            tv_id.setText(student.getUsername());
                            tv_phone.setText(student.getPhone());
                            tv_name.setText(student.getName());
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "dsadsadsa", Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "chuyen sang trang Thời khoá biểu", Toast.LENGTH_SHORT).show();
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
