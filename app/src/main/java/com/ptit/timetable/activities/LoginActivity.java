package com.ptit.timetable.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;



import com.google.gson.Gson;
import com.ptit.timetable.Constant;
import com.ptit.timetable.R;
import com.ptit.timetable.model.Schedule;
import com.ptit.timetable.model.Token;
import com.ptit.timetable.model.TokenDecode;
import com.ptit.timetable.utils.DecodeToken;
import com.ptit.timetable.utils.HttpServices;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    Button button_login;
    LinearLayout layout;
    ProgressBar progressBar;
    EditText edUsername, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button_login = findViewById(R.id.btn_login);
        layout = findViewById(R.id.layout);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        HttpServices.setContext(getBaseContext());
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                ////                String json = "{\"username\":1,\"password\":\"1\"}";
                JSONObject jsonObject = new JSONObject();
                try {
                    String username = edUsername.getText().toString().trim();
                    String password = edPassword.getText().toString().trim();
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("->>>>");
                //
                HttpServices.post(Constant.BASE_URL + "/api/login", String.valueOf(jsonObject), new Callback() {
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
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String responseStr = response.body().string();
                            Gson gson = new Gson();

                            final Token token = gson.fromJson(responseStr, Token.class);
                            System.out.println(token);
                            //

                            SharedPreferences preferences = getSharedPreferences("sharedPreferenceManager", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("TOKEN", token.getAccessToken());
                            //
                            TokenDecode tokenDecode = null;
                            try {
                                tokenDecode = gson.fromJson(DecodeToken.decoded(token.getAccessToken()), TokenDecode.class);
                                editor.putInt("ID", tokenDecode.getId());
                                editor.putString("NAME", tokenDecode.getName());
                                editor.putString("USERNAME", tokenDecode.getUsername());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            editor.apply();
                            ///
                            runOnUiThread(new Runnable() {
                                public void run() {
//                                    Toast.makeText(getBaseContext(), HttpServices.getCurrentToken(), Toast.LENGTH_LONG).show();
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Intent intent = new Intent(getBaseContext(), SubjectCurrentActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            // Request not successful
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getBaseContext(), "Sai thông tin.", Toast.LENGTH_LONG).show();
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    progressBar.setVisibility(View.INVISIBLE);

                                }
                            });
                        }
                    }
                });


            }
        });
        //
    }


}