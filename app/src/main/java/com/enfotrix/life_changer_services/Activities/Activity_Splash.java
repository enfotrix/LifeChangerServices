package com.enfotrix.life_changer_services.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.Utils;

public class Activity_Splash extends AppCompatActivity {
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        utils = new Utils(this);

        delay();

    }

    private void delay() {


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (utils.isLoggedIn()) {
                    Intent intent = new Intent(Activity_Splash.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Activity_Splash.this, ActivityLogin.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 3000);

    }
}