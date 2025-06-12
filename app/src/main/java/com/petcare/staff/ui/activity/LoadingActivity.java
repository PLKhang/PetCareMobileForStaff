package com.petcare.staff.ui.activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.petcare.staff.R;
import com.petcare.staff.data.model.api.user.UserResponse;
import com.petcare.staff.data.remote.ApiHealth;
import com.petcare.staff.data.remote.UserApi;
import com.petcare.staff.utils.ApiClient;
import com.petcare.staff.utils.SharedPrefManager;
import com.petcare.staff.ui.activity.LoginActivity;
import com.petcare.staff.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private TextView tvStatus;
    private TextView tvProgress;
    private Handler handler;
    private int progress = 0;
    private static final int TOTAL_DURATION_MS = 2000;
    private static final int CHECK_DURATION_MS = TOTAL_DURATION_MS / 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Initialize views
        progressBar = findViewById(R.id.progress_bar);
        tvStatus = findViewById(R.id.tv_status);
        tvProgress = findViewById(R.id.tv_progress);
        handler = new Handler(Looper.getMainLooper());
        sharedPreferences = getSharedPreferences(SharedPrefManager.PREF_NAME, MODE_PRIVATE);

        // Start checks
        performChecks();
    }

    private void performChecks() {
        // Step 1: Check Connection
        checkConnection();
    }

    private void checkConnection() {
        tvStatus.setText("Checking connection to server...");
        if (!isInternetAvailable()) {
            showErrorAndExit("No internet connection. Please check your network.");
            return;
        }

        // Check server reachability via API
        ApiHealth apiHealth = ApiClient.getInstance(this).create(ApiHealth.class);
        Call<String> call = apiHealth.checkServerHealth();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String body = response.body().trim();
                    if (body.equalsIgnoreCase("Hello World")) {
                        updateProgress(0, 33, CHECK_DURATION_MS, LoadingActivity.this::checkLogin);
                    } else {
                        Log.e("API_DEBUG", "Unexpected response body: " + body + ", Code: " + response.code());
                        showErrorAndExit("Server connection failed. Unexpected response: " + body);
                    }
                } else {
                    Log.e("API_DEBUG", "Unsuccessful response. Code: " + response.code() + ", Body: " + (response.body() != null ? response.body() : "null"));
                    showErrorAndExit("Server connection failed. HTTP " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API_DEBUG", "Server check failed: " + t.getClass().getSimpleName() + " - " + t.getMessage(), t);
                showErrorAndExit("Failed to connect to server: " + t.getMessage());
            }
        });
    }
    private void checkLogin() {
        tvStatus.setText("Verifying Login...");
        if (!sharedPreferences.getBoolean(SharedPrefManager.KEY_REMEMBER, false) ||
                SharedPrefManager.getToken(this) == null ||
                SharedPrefManager.getToken(this).isEmpty()) {
            // No remembered login or no token, go to login
            updateProgress(33, 66, CHECK_DURATION_MS, this::goToLogin);
            return;
        }

        // Verify token with /api/v1/users/me
        UserApi apiUser = ApiClient.getUserApi(this);
        Call<UserResponse> call = apiUser.getCurrentUser();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    // Token valid, proceed to loading
                    updateProgress(33, 66, CHECK_DURATION_MS, LoadingActivity.this::checkLoading);
                } else {
                    // Token invalid, clear token and go to login
                    SharedPrefManager.clearToken(LoadingActivity.this);
                    updateProgress(33, 66, CHECK_DURATION_MS, LoadingActivity.this::goToLogin);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Network error, clear token and go to login
                SharedPrefManager.clearToken(LoadingActivity.this);
                updateProgress(33, 66, CHECK_DURATION_MS, LoadingActivity.this::goToLogin);
            }
        });
    }

    private void checkLoading() {
        tvStatus.setText("Loading App...");
        updateProgress(66, 100, CHECK_DURATION_MS, this::goToMain);
    }

    private void updateProgress(int start, int end, int duration, Runnable onComplete) {
        int steps = end - start;
        int delayPerStep = duration / steps; // Time per percentage point
        Runnable updateTask = new Runnable() {
            @Override
            public void run() {
                if (progress < end) {
                    progress++;
                    progressBar.setProgress(progress);
                    tvProgress.setText(progress + "%");
                    handler.postDelayed(this, delayPerStep);
                } else {
                    onComplete.run();
                }
            }
        };
        progress = start;
        handler.post(updateTask);
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                android.net.Network network = cm.getActiveNetwork();
                if (network != null) {
                    android.net.NetworkCapabilities caps = cm.getNetworkCapabilities(network);
                    return caps != null && (caps.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) ||
                            caps.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR));
                }
            } else {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            }
        }
        return false;
//        return true;
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
        new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage(msg)
                .setCancelable(false) // Không cho người dùng bấm ra ngoài để đóng
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();  // Đóng dialog
                    finish();          // Thoát Activity sau khi nhấn OK
                })
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}