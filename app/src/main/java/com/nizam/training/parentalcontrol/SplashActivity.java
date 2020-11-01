// SplashActivity.java
package com.nizam.training.parentalcontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    ImageView imageViewSplash;
    TextView txtAppName;
    RelativeLayout relativeLayout;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SettingActivity.setLanguage(getApplicationContext(), getResources());
        setContentView(R.layout.activity_main);
        imageViewSplash = findViewById(R.id.imageViewSplash);
        txtAppName = findViewById(R.id.txtAppName);
        relativeLayout = findViewById(R.id.rel);
        startAnimations();
        final SharedPreferences sharedPreferences = getSharedPreferences("PinNumber", 0);
        int pin = sharedPreferences.getInt("Pin", 0);
        if (pin == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToSignup();
                }
            }, 2000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToLogin();
                }
            }, 2000);
        }
    }

    private void goToSignup() {
        startActivity(new Intent(this, SignupActivity.class));
        finish();
    }

    public void goToLogin() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnimations();
    }

    private void startAnimations() {
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.anim);
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.boune);
        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        Animation lay = AnimationUtils.loadAnimation(this, R.anim.animation_popup_enter);
        rotate.reset();
        fadein.reset();
        lay.reset();
        bounce.reset();
        relativeLayout.startAnimation(fadein);
        imageViewSplash.startAnimation(rotate);
        txtAppName.startAnimation(bounce);
    }
}