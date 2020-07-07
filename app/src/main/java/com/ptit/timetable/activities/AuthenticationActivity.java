package com.ptit.timetable.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ptit.timetable.R;
import com.ptit.timetable.utils.DecodeToken;
import com.ptit.timetable.utils.HttpServices;


public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        HttpServices.setContext(getBaseContext());
        String token = HttpServices.getCurrentToken();

        if(token.equals("")){
            Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        else{
            Intent mainIntent = new Intent(getBaseContext(), SubjectCurrentActivity.class);
            try {
                String decode = DecodeToken.decoded(token);
//                Toast.makeText(getBaseContext(), decode, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(mainIntent);
            finish();
        }

    }
}