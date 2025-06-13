package com.petcare.staff.ui.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(applyDefaultConfiguration(newBase));
    }

    @NonNull
    private Context applyDefaultConfiguration(Context context) {
        Configuration config = new Configuration(context.getResources().getConfiguration());

        // Reset về cấu hình mặc định
        config.fontScale = 1.0f; // Không scale font
        config.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE; // Không scale hiển thị UI
        config.setToDefaults(); // Reset toàn bộ (nếu cần)

        return context.createConfigurationContext(config);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration override = new Configuration(res.getConfiguration());

        if (override.fontScale != 1.0f) {
            override.fontScale = 1.0f;
            res.updateConfiguration(override, res.getDisplayMetrics());
        }

        return res;
    }
}
