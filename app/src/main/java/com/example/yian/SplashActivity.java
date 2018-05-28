package com.example.yian;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.yian.Apollo.Publish;
import com.example.yian.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    private Publish publish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        publish=new Publish("LED/topic");
        publish.getInstance("LED");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoHome();
            }
        }, 2000);
    }

    private void gotoHome() {
        publish.open(SplashActivity.this);
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }
}
