package com.example.intcore_demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.intcore_demo.login.view.LogInActivity;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(SplashActivity.this::navigateToLoginActivity, 3000);
        finish();
    }
}
