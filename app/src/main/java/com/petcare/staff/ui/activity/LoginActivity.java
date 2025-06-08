package com.petcare.staff.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.data.model.api.user.LoginCallback;
import com.petcare.staff.data.repository.UserRepository;
import com.petcare.staff.utils.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin;
    UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository(this);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefManager.PREF_NAME, MODE_PRIVATE);

        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.edtUsername);
        etPassword = findViewById(R.id.edtPassword);
        cbRememberMe = findViewById(R.id.checkboxRemember);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean rememberMe = cbRememberMe.isChecked();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        userRepository.login(username, password, rememberMe, new LoginCallback() {
            @Override
            public void onSuccess(String token) {
                goToMain();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void goToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

