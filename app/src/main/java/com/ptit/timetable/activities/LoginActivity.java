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
import android.widget.Toast;



import com.google.gson.Gson;
import com.ptit.timetable.R;
import com.ptit.timetable.model.Schedule;
import com.ptit.timetable.model.Token;
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
    Button button_login, bt;
    LinearLayout layout;
    ProgressDialog progressDialog;
    EditText edUsername, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button_login = findViewById(R.id.btn_login);
        layout = findViewById(R.id.layout);
        bt = findViewById(R.id.button1);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
//        // hide action bar
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        // hide status bar
//        android.app.ActionBar status = getActionBar();
//        status.hide();

//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        HttpServices.setContext(getBaseContext());
        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_NoActionBar);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////                String json = "{\"username\":1,\"password\":\"1\"}";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", edUsername.getText().toString().trim());
                    jsonObject.put("password", edUsername.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("->>>>");
                //
//                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Đăng đăng nhập...");
                progressDialog.show();
                HttpServices.post("http://192.168.1.6:8080/api/login", String.valueOf(jsonObject), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("login fail");
                        e.printStackTrace();
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
                            editor.apply();
                            ///
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast.makeText(getBaseContext(), HttpServices.getCurrentToken(), Toast.LENGTH_LONG).show();
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getBaseContext(), SubjectCurrentActivity.class);
                                    startActivity(intent);
                                    finish();


                                }
                            });
                        } else {
                            // Request not successful
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast.makeText(getBaseContext(), "dsadsadsadsad", Toast.LENGTH_LONG).show();
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();


                                }
                            });
                        }
                    }
                });


            }
        });
        //
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.67:8080/api/get").newBuilder();
//                urlBuilder.addQueryParameter("id", "1");
//                String url = urlBuilder.build().toString();
//                System.out.println(url);
                HttpServices.getWithToken("http://192.168.2.182:8080/api/get-timetable?student_id=1", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("faillll");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Gson gson = new Gson();
                            //final String student = response.body().string();
                            //System.out.println(response.body().string());
                            //Type listType = new TypeToken<ArrayList<Schedule>>(){}.getType();
                            final List<Schedule> schedules = gson.fromJson(response.body().string(), List.class);

                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast.makeText(getBaseContext(), schedules.toString(), Toast.LENGTH_LONG).show();
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();


                                }
                            });
                        }

                    }
                });
            }
        });
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}