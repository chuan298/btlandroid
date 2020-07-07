package com.ptit.timetable.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.ptit.timetable.Constant;
import com.ptit.timetable.R;
import com.ptit.timetable.model.Student;
import com.ptit.timetable.utils.DbHelper;
import com.ptit.timetable.utils.HttpServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class PersonInfoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context context = this;
    private ListView listView;
    private DbHelper db;
    private int listposition = 0;
    private String NAME = "";
    private String USERNAME = "";
    int ID = 0;
    private TextView tv_id;
    private TextView tv_name;
    private TextView tv_class;
    private TextView tv_ptit;
    private TextView tv_phone;
    private TextView tv_email;
    private Button btLogout, btChangePw;
    private TextView tvUsernameheader;
    private TextView tvNameheader;
    private FusedLocationProviderClient client;
    ImageView imageView;

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
        btLogout = findViewById(R.id.bt_Logout);
        imageView = findViewById(R.id.iwAvatar);
        btChangePw = findViewById(R.id.bt_changepw);
        //
        HttpServices.setContext(getBaseContext());
        final SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferenceManager", MODE_PRIVATE);
        NAME = sharedPreferences.getString("NAME", " ");
        USERNAME = sharedPreferences.getString("USERNAME", " ");
        ID = sharedPreferences.getInt("ID", 0);
        HttpServices.getWithToken(Constant.BASE_URL + "/api/get-info?student_id=" + ID, new Callback() {
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
//                            Toast.makeText(getBaseContext(), student.toString(), Toast.LENGTH_LONG).show();
                            Log.d("PersonInfo", "run: student null = " + (student == null));
                            tv_id.setText(student.getUsername());
                            tv_phone.setText(student.getPhone());
                            tv_name.setText(student.getName());
                            if(student.getAvatar() != null){
                                imageView.setImageBitmap(convertStringBase64ToBitmap(student.getAvatar()));
                            }

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
        //
        btChangePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Bạn thực sự muốn đăng xuất?");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //editor.putString("TOKEN", "");
                        editor.clear();
                        editor.apply();
                        /////////
                        SharedPreferences sharedPreferences1 = getSharedPreferences("Timatable", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        //editor.putString("TOKEN", "");
                        editor1.clear();
                        editor1.apply();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finishAffinity();
                    }
                });
                builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage(PersonInfoActivity.this);
            }
        });
    }

    private void initAll() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //
        View headerView = navigationView.getHeaderView(0);
        tvNameheader = headerView.findViewById(R.id.tvHeaderName);
        tvUsernameheader = headerView.findViewById(R.id.tvHeaderUserName);
        tvNameheader.setText(NAME);
        tvUsernameheader.setText(USERNAME);
        //
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
//                Toast.makeText(this, "chuyen sang trang Thời khoá biểu", Toast.LENGTH_SHORT).show();
                Intent note = new Intent(PersonInfoActivity.this, TKBActivity.class);
                startActivity(note);
                return true;
            default:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }

    public static String convertBitmapImageToBase64(Bitmap bitmapImage) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bb = bos.toByteArray();
        String encodedImage = Base64.encodeToString(bb, Base64.DEFAULT);
        return encodedImage;
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Chụp ảnh", "Chọn từ thư viện","Hủy bỏ" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thay ảnh đại diện");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Chụp ảnh")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Chọn từ thư viện")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Hủy bỏ")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("resultCode: + " +  resultCode);
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        imageView.setImageURI(selectedImage);

                    }
                    break;
            }
            String imgbase64 = convertBitmapImageToBase64(((BitmapDrawable)imageView.getDrawable()).getBitmap());
            if(imgbase64 != null){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("imgbase64", imgbase64);
//                    jsonObject.put("newPassword", newpassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HttpServices.putWithToken(Constant.BASE_URL + "/api/change-avatar", String.valueOf(jsonObject), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getBaseContext(), "Có lỗi xảy ra!", Toast.LENGTH_LONG).show();
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if(response.isSuccessful()){
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    try {
                                        Toast.makeText(getBaseContext(), response.body().string(), Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                        else{
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    try {
                                        Toast.makeText(getBaseContext(), response.body().string(), Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }
    private static Bitmap convertStringBase64ToBitmap(String imgbase64){
//        System.out.println("imgbase64: " + imgbase64);
        byte[] decodedString = Base64.decode(imgbase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
