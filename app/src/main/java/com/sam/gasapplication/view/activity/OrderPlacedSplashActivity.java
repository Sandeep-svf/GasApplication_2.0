package com.sam.gasapplication.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sam.gasapplication.MainActivity;
import com.sam.gasapplication.R;

public class OrderPlacedSplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent intent = new Intent(OrderPlacedSplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, SPLASH_DISPLAY_LENGTH);


    }
}