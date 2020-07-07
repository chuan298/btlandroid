package com.ptit.timetable.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.ptit.timetable.Constant;
import com.ptit.timetable.MainActivity;
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
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class SubjectCurrentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ImageView imgAvatar, imgCheckin;
    TextView tvStatus, tvName, tvUsername, tvSubjectName, tvTeacher, tvRoom, tvStartTime, tvEndTime;
    Button btn_checkin;
    private int REQUEST_CODE_CAMERA = 123;
    String NAME = " ";
    String USERNAME = " ";
    int ID = 0;
    Location mLocation;

    ProgressBar progressBar;
    GoogleApiClient mGoogleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 15000;  /* 15 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private Double longitude = -1.0;
    private Double latitude = -1.0;

    private final static int ALL_PERMISSIONS_RESULT = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HttpServices.setContext(getBaseContext());
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferenceManager", MODE_PRIVATE);
        NAME = sharedPreferences.getString("NAME", " ");
        USERNAME = sharedPreferences.getString("USERNAME", " ");
        ID = sharedPreferences.getInt("ID", 0);

        ///

        HttpServices.getWithToken(Constant.BASE_URL + "/api/get-current-schedule?student_id=" + ID, new Callback() {
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
        //
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        //
        //
        final String time = startTime;
        imgCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        //
        progressBar = findViewById(R.id.progressBar_current_subject);
        progressBar.setVisibility(View.INVISIBLE);
        //
        btn_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (imgAvatar.getVisibility() == View.GONE) {
                    Toast.makeText(SubjectCurrentActivity.this, "click icon status để chụp ảnh", Toast.LENGTH_SHORT).show();
//                    btn_checkin.setEnabled(true);
//                    progressBar.setVisibility(View.INVISIBLE);
                } else {
//                    tvStatus.setText("Đã điểm danh");

                    progressBar.setVisibility(View.VISIBLE);
                    btn_checkin.setEnabled(false);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        String imgbase64 = convertImageToBase64(imgAvatar);
                        Integer subject_group_id = scheduleCourse.getSchedule().getPracticeGroup().getSubjectGroup().getId();
                        jsonObject.put("imgbase64", imgbase64);
                        jsonObject.put("longitude", longitude);
                        jsonObject.put("latitude", latitude);
                        jsonObject.put("subject_group_id", subject_group_id);
                        jsonObject.put("username", USERNAME);
                        jsonObject.put("studentId", ID);
                        jsonObject.put("time", time);
                        HttpServices.postWithToken(Constant.BASE_URL + "/api/attendance", String.valueOf(jsonObject), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Xảy ra lỗi", Toast.LENGTH_LONG).show();
                                        btn_checkin.setEnabled(true);
                                        progressBar.setVisibility(View.INVISIBLE);
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
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                }
                                            });
                                        }
                                        else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getBaseContext(), attendanceResponse.getMessage(), Toast.LENGTH_LONG).show();
                                                    btn_checkin.setEnabled(true);
                                                    progressBar.setVisibility(View.INVISIBLE);
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
                                                btn_checkin.setEnabled(true);
                                                progressBar.setVisibility(View.INVISIBLE);
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
//                Toast.makeText(this,"chuyen sang trang Thời khoá biểu",Toast.LENGTH_SHORT).show();
                Intent note = new Intent(SubjectCurrentActivity.this, TKBActivity.class);
                startActivity(note);
                return true;
            default:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }
    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
//            latLng.setText("Please install Google Play services.");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if(mLocation!=null)
        {
//            latLng.setText("Latitude : "+mLocation.getLatitude()+" , Longitude : "+mLocation.getLongitude());
            longitude = mLocation.getLongitude();
            latitude = mLocation.getLatitude();
            System.out.println("Latitude : "+mLocation.getLatitude()+" , Longitude : "+mLocation.getLongitude());
        }

        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        latLng.setText("No connect");
        Toast.makeText(getBaseContext(), "Không có kết nối", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        if(location!=null){
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            System.out.println("Latitude : "+location.getLatitude()+" , Longitude : "+location.getLongitude());
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else
                finish();

            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Enable Permissions", Toast.LENGTH_LONG).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);


    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SubjectCurrentActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }


    public void stopLocationUpdates()
    {
        try{
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi
                        .removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }
        catch (Exception e){
            e.printStackTrace();
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
