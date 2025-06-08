package com.petcare.staff.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.petcare.staff.MainActivity;
import com.petcare.staff.data.repository.UserRepository;
import com.petcare.staff.utils.SharedPrefManager;

import org.json.JSONException;

public class LoadingActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(SharedPrefManager.PREF_NAME, MODE_PRIVATE);

        if (sharedPreferences.getBoolean(SharedPrefManager.KEY_REMEMBER, false)) {
            try {
                if (SharedPrefManager.isActiveToken(this)) {
                    goToMain();
                } else {
                    goToLogin();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            goToLogin();
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showErrorAndExit(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        new Handler().postDelayed(this::finish, 2000);
    }
}

