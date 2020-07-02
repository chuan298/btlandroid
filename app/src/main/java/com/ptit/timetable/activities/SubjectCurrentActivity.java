package com.ptit.timetable.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;
import android.util.Base64;
import android.util.Pair;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.ptit.timetable.R;
import com.ptit.timetable.model.AttendanceResponse;
import com.ptit.timetable.model.Schedule;
import com.ptit.timetable.model.ScheduleCourse;
import com.ptit.timetable.utils.DbUtils;
import com.ptit.timetable.utils.HttpServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class SubjectCurrentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView imgAvatar, imgCheckin;
    TextView tvStatus, tvName, tvUsername, tvSubjectName, tvTeacher, tvRoom, tvStartTime, tvEndTime;
    Button btn_checkin;
    private int REQUEST_CODE_CAMERA = 123;
    String NAME = " ";
    String USERNAME = " ";
    int ID = 0;
    final String BASE_URL = "http://192.168.43.34:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HttpServices.setContext(getBaseContext());
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferenceManager", MODE_PRIVATE);
        NAME = sharedPreferences.getString("NAME", " ");
        USERNAME = sharedPreferences.getString("USERNAME", " ");
        ID = sharedPreferences.getInt("ID", 0);

        ///

        System.out.println( BASE_URL + "/api/get-current-schedule?student_id=" + ID);
        HttpServices.getWithToken(BASE_URL + "/api/get-current-schedule?student_id=" + ID, new Callback() {
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
                    final ScheduleCourse scheduleCourse = gson.fromJson(response.body().string(), ScheduleCourse.class);
                    if(scheduleCourse == null){

                        runOnUiThread(new Runnable() {
                            public void run() {
                                setContentView(R.layout.activity_no_subject_current);
                                initAllNoCourse();
                            }
                        });
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setContentView(R.layout.activity_subject_current);
                                System.out.println(scheduleCourse);
                                initAll(scheduleCourse);
                            }
                        });
                    }

                }
            }
        });


//        setContentView(R.layout.activity_no_subject_current);
//        initAll();
    }

    private void initAll(final ScheduleCourse scheduleCourse) {
        DbUtils dbUtils = new DbUtils(getBaseContext());
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        imgAvatar = findViewById(R.id.imgAvatar);
        imgCheckin  = findViewById(R.id.imageViewCheckin);
        btn_checkin = findViewById(R.id.btn_checkin);
        tvStatus = findViewById(R.id.status);
        tvSubjectName = findViewById(R.id.subjectName);
        tvTeacher = findViewById(R.id.teacherSubject);
        tvRoom = findViewById(R.id.roomSubject);
        tvStartTime = findViewById(R.id.startTimeSubject);
        tvEndTime = findViewById(R.id.EndtimeSubject);
        //
        View headerView = navigationView.getHeaderView(0);
        tvName = headerView.findViewById(R.id.tvHeaderName);
        tvUsername = headerView.findViewById(R.id.tvHeaderUserName);
        tvName.setText(NAME);
        tvUsername.setText(USERNAME);
        if(scheduleCourse.getIsAttendanced()){
            tvStatus.setText("Đã điểm danh");
            btn_checkin.setEnabled(false);
            imgCheckin.setEnabled(false);
        }
        //
        tvSubjectName.setText(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getSubject().getName());
        tvTeacher.setText(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getTeacher());
        //
        String startTime = "";
        String endTime = "";
        String room = "";
        if(scheduleCourse.getTypeSchedule() == 1){
            System.out.println("start: " + scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getShift1().getStartLesson());
            room = scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getRoom().getName();
            startTime = DbUtils.getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getShift1().getStartLesson());
            endTime = DbUtils.getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getShift1().getEndLesson());
        }
        else if(scheduleCourse.getTypeSchedule() == 2){
            room = scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getRoom().getName();
            startTime = DbUtils.getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getShift2().getStartLesson());
            endTime = DbUtils.getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getShift2().getEndLesson());
        }
        else if(scheduleCourse.getTypeSchedule() == 3){
            room = scheduleCourse.getSchedule().getPracticeGroup().getPracticeRoom().getName();
            startTime = DbUtils.getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getPracticeShift().getStartLesson());
            endTime = DbUtils.getTimeFromShift(scheduleCourse.getSchedule().getPracticeGroup().getPracticeShift().getEndLesson());
        }
        tvRoom.setText(room);
        tvStartTime.setText(startTime);
        tvEndTime.setText(endTime);
        //
        final String time = startTime;
        imgCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        btn_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgAvatar.getVisibility() == View.GONE) {
                    Toast.makeText(SubjectCurrentActivity.this, "click icon status để chụp ảnh", Toast.LENGTH_SHORT).show();
                } else {
//                    tvStatus.setText("Đã điểm danh");
                    JSONObject jsonObject = new JSONObject();
                    try {
                        String imgbase64 = convertImageToBase64(imgAvatar);
                        Integer subject_group_id = scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getId();
                        jsonObject.put("imgbase64", imgbase64);
                        jsonObject.put("longitude", 1);
                        jsonObject.put("latitude", 1);
                        jsonObject.put("subject_group_id", subject_group_id);
                        jsonObject.put("username", USERNAME);
                        jsonObject.put("studentId", ID);
                        jsonObject.put("time", time);
                        HttpServices.postWithToken(BASE_URL + "/api/attendance", String.valueOf(jsonObject), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Xảy ra lỗi", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.isSuccessful()){
                                    try{
                                        String responseStr = response.body().string();
                                        Gson gson = new Gson();
                                        final AttendanceResponse attendanceResponse = gson.fromJson(responseStr, AttendanceResponse.class);

                                        if(attendanceResponse.getIsAttendant()){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getBaseContext(), "Điểm danh thành công ^^", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                        else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getBaseContext(), attendanceResponse.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getBaseContext(), "Xảy ra lỗi", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }


                                }
                                else{

                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    @SuppressLint("SetTextI18n")
    private void initAllNoCourse() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //
        View headerView = navigationView.getHeaderView(0);
        tvName = headerView.findViewById(R.id.tvHeaderName);
        tvUsername = headerView.findViewById(R.id.tvHeaderUserName);
        tvName.setText(NAME);
        tvUsername.setText(USERNAME);

        //
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgAvatar.setVisibility(View.VISIBLE);
            imgAvatar.setImageBitmap(bitmap);
        }
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
                Intent subject = new Intent(SubjectCurrentActivity.this, SubjectCurrentActivity.class);
                startActivity(subject);
                return true;
            case R.id.history:
                Intent teacher = new Intent(SubjectCurrentActivity.this, HistoryCheckinActivity.class);
                startActivity(teacher);
                return true;
            case R.id.personInfo:
                Intent homework = new Intent(SubjectCurrentActivity.this, PersonInfoActivity.class);
                startActivity(homework);
                return true;
            case R.id.tkb:
                Toast.makeText(this,"chuyen sang trang Thời khoá biểu",Toast.LENGTH_SHORT).show();
                Intent note = new Intent(SubjectCurrentActivity.this, TKBActivity.class);
                startActivity(note);
                return true;
            default:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }
    public static String convertImageToBase64(ImageView imageView){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
        byte[] bb = bos.toByteArray();
        String encodedImage = Base64.encodeToString(bb, Base64.DEFAULT);
        return encodedImage;
    }
}
