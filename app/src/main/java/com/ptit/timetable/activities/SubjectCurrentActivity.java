package com.ptit.timetable.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;
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


import com.ptit.timetable.R;
import com.ptit.timetable.utils.HttpServices;


public class SubjectCurrentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView imgAvatar, imgCheckin;
    TextView tvStatus;
    Button btn_checkin;
    private int REQUEST_CODE_CAMERA = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_current);
        HttpServices.setContext(getBaseContext());
//        HttpServices.get("")
        initAll();
    }

    private void initAll() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        imgAvatar = findViewById(R.id.imgAvatar);
        imgCheckin  = findViewById(R.id.imageViewCheckin);
        btn_checkin = findViewById(R.id.btn_checkin);
        tvStatus = findViewById(R.id.status);
        //
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
                    tvStatus.setText("Đã điểm danh");
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
}
