package com.example.yian;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yian.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoHome();
            }
        }, 2000);
    }

    private void gotoHome() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }
}
