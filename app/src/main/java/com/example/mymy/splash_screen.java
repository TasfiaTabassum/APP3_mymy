package com.example.mymy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class splash_screen extends AppCompatActivity {

    LottieAnimationView lottie;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        lottie = findViewById(R.id.lottie);
        textView = findViewById(R.id.text);

        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //Set animation to elements
        lottie.setAnimation(topAnim);
        textView.setAnimation(bottomAnim);

        lottie.animate().translationY(2400).setDuration(1000).setStartDelay(2500);
        textView.animate().translationY(1400).setDuration(1000).setStartDelay(2500);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash_screen.this, logIn.class);
                startActivity(intent);
                finish();
            }
        }, 2400);
    }
}