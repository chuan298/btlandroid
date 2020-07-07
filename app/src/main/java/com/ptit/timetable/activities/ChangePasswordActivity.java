package com.ptit.timetable.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ptit.timetable.Constant;
import com.ptit.timetable.R;
import com.ptit.timetable.utils.HttpServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.cache.DiskLruCache;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText edOldPW;
    private EditText edNewPW;
    private EditText edReNewPW;
    private Button btnConfirm;
    private String PASSWORD = "";
    int ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        btnConfirm = findViewById(R.id.btn_confirm);
        edOldPW = findViewById(R.id.edOldPass);
        edNewPW = findViewById(R.id.edNewPass);

        HttpServices.setContext(getBaseContext());
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferenceManager", MODE_PRIVATE);
        PASSWORD = sharedPreferences.getString("PASSWORD", " ");
        ID = sharedPreferences.getInt("ID", 0);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    String oldpassword = edOldPW.getText().toString().trim();
                    String newpassword = edNewPW.getText().toString().trim();
                    jsonObject.put("oldPassword", oldpassword);
                    jsonObject.put("newPassword", newpassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HttpServices.putWithToken(Constant.BASE_URL + "/api/change-password", String.valueOf(jsonObject), new Callback() {
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
                                    Intent intent = new Intent(getApplicationContext(), PersonInfoActivity.class);
                                    startActivity(intent);
                                    finish();
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
        });
    }
}
