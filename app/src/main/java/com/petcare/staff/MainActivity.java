package com.petcare.staff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.petcare.staff.data.model.api.notification.NotificationApiRequest;
import com.petcare.staff.ui.activity.LoginActivity;
import com.petcare.staff.ui.appointment.viewmodel.AppointmentListViewModel;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.ui.home.viewmodel.BranchViewModel;
import com.petcare.staff.ui.home.viewmodel.NotificationViewModel;
import com.petcare.staff.ui.userprofile.viewmodel.UserProfileViewModel;
import com.petcare.staff.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ImageView btnNav;
    private LinearLayout bot_nav_home, bot_nav_appointment, bot_nav_notification, bot_nav_profile;
    private FloatingActionButton bot_nav_bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserProfileViewModel userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        userProfileViewModel.loadCurrentUser(); //load user từ repository và lưu vào LiveData
        BranchViewModel branchViewModel = new ViewModelProvider(this).get(BranchViewModel.class);
        AppointmentListViewModel appointmentListViewModel = new ViewModelProvider(this).get(AppointmentListViewModel.class);
        NotificationViewModel notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);


        // notification
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    Log.d("FCM", "FCM Token: " + token);

                    userProfileViewModel.getUser().observe(this, user -> {
                    NotificationApiRequest request = new NotificationApiRequest(user.getId(), user.getBranchId(), token);

                    notificationViewModel.sendNotificationRequest(request, new RepositoryCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Log.d("Notification", "Success: " + message);
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.d("Notification", "Fail: " + t.getMessage());
                        }
                    });
                    });
                });

        FirebaseMessaging.getInstance().subscribeToTopic("staff");

        // Set up NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        } else {
            throw new IllegalStateException("NavHostFragment not found!");
        }

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                updateToolbarTitle(navDestination.getLabel().toString());
            }
        });

        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        btnNav = toolbar.findViewById(R.id.btnNav);

        bot_nav_home = findViewById(R.id.bot_nav_home);
        bot_nav_appointment = findViewById(R.id.bot_nav_appointment);
        bot_nav_bill = findViewById(R.id.btn_bill);
        bot_nav_notification = findViewById(R.id.bot_nav_notification);
        bot_nav_profile = findViewById(R.id.bot_nav_profile);


        // Custom bottom navigation
        setBottomNavigationItemSelectedListener();

        // Navigation Drawer
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        View headerView = navigationView.getHeaderView(0);

        // Tìm các View con trong header
        ImageView imageView = headerView.findViewById(R.id.image);
        TextView nameTextView = headerView.findViewById(R.id.name);
        TextView emailTextView = headerView.findViewById(R.id.email);

        userProfileViewModel.getUser().observe(this, user -> {
            imageView.setImageResource(R.drawable.temp_avatar);
            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());

            branchViewModel.loadBranch(user.getBranchId());
            appointmentListViewModel.loadUserAppointment(user.getId());
            appointmentListViewModel.loadBranchAppointment(user.getBranchId());
        });

        branchViewModel.getBranchInfo().observe(this, info -> {
            TextView branchName = toolbar.findViewById(R.id.tvBranch);
            branchName.setText(info.getName());
        });


        btnNav.setOnClickListener(view -> drawerLayout.open());
    }

    private void setBottomNavigationItemSelectedListener() {
        bot_nav_home.setOnClickListener(v -> {
            navigateWithoutBackStack(R.id.homePageFragment, null);
//            loadFragment(new HomePageFragment());
        });
        bot_nav_appointment.setOnClickListener(v -> {
            navigateWithoutBackStack(R.id.appointmentListFragment, null);
//            loadFragment(new AppointmentListFragment());
        });
        bot_nav_bill.setOnClickListener(v -> {
            navigateWithoutBackStack(R.id.createBillFragment, null);
//            loadFragment(new CreateBillFragment());
        });
        bot_nav_notification.setOnClickListener(v -> {
            navigateWithoutBackStack(R.id.notificationFragment, null);
//            loadFragment(new NotificationFragment());
        });
        bot_nav_profile.setOnClickListener(v -> {
            navigateWithoutBackStack(R.id.userProfileFragment, null);
//            loadFragment(new UserProfileFragment());
        });
    }

    private boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                navigateWithoutBackStack(R.id.homePageFragment, null);
                break;
            case R.id.appointment:
                navigateWithoutBackStack(R.id.appointmentListFragment, null);
                break;
            case R.id.bill:
                navigateWithoutBackStack(R.id.createBillFragment, null);
                break;
            case R.id.inventory:
                navigateWithoutBackStack(R.id.inventoryFragment, null);
                break;
            case R.id.nav_Profile:
                navigateWithoutBackStack(R.id.userProfileFragment, null);
                break;
            case R.id.nav_Logout:
                logout();
                break;
            default:
                navigateWithoutBackStack(R.id.homePageFragment, null);
                break;
        }
        return true;
    }

    private void logout() {
        SharedPrefManager.clearToken(this);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefManager.PREF_NAME, MODE_PRIVATE);
        sharedPreferences.edit().remove(SharedPrefManager.KEY_REMEMBER).apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void updateToolbarTitle(String newTitle) {
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(newTitle);
    }

    public void setActiveNav(int mainFragment) {
        // Reset màu của tất cả các nút
        resetNavColors();
        LinearLayout selectedNav = null;

        switch (mainFragment) {
            case 1: // Home
                selectedNav = findViewById(R.id.bot_nav_home);
                break;
            case 2: // Appointment
                selectedNav = findViewById(R.id.bot_nav_appointment);
                break;
            case 3: // Notification
                selectedNav = findViewById(R.id.bot_nav_notification);
                break;
            case 4: // Profile
                selectedNav = findViewById(R.id.bot_nav_profile);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mainFragment);
        }

        assert selectedNav != null;
        // Đặt màu cho nút được chọn
        ImageView icon = (ImageView) selectedNav.getChildAt(0);
        icon.setColorFilter(ContextCompat.getColor(this, R.color.color_nav_selected));
    }

    private void resetNavColors() {
        int[] navIds = {R.id.bot_nav_home, R.id.bot_nav_appointment, R.id.bot_nav_notification, R.id.bot_nav_profile};

        for (int id : navIds) {
            LinearLayout navItem = findViewById(id);
            ImageView icon = (ImageView) navItem.getChildAt(0);
            icon.setColorFilter(ContextCompat.getColor(this, R.color.color_nav_unselected));
        }
    }

    public void hideBottomNavigation(boolean isHide) {
        CoordinatorLayout custom_bottom_nav = findViewById(R.id.custom_bottom_nav);
        custom_bottom_nav.setVisibility(isHide ? View.GONE : View.VISIBLE);
    }

    /**
     * Điều hướng đến fragment mới và lưu lại vào back stack.
     */
    public void navigateToWithBackStack(int destinationId, Bundle args) {
        navController.navigate(destinationId, args);
    }

    /**
     * Điều hướng đến fragment mới nhưng xóa tất cả back stack trước đó.
     */
    public void navigateToClearStack(int destinationId, Bundle args) {
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.homePageFragment, false)  // Xóa hết về trang chủ
                .build();
        navController.navigate(destinationId, args, navOptions);
    }

    /**
     * Điều hướng mà không lưu lại fragment trước đó vào back stack.
     */
    public void navigateWithoutBackStack(int destinationId, Bundle args) {
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(destinationId, true)  // Xóa fragment trước đó
                .build();
        navController.navigate(destinationId, args, navOptions);
    }

    /**
     * Quay lại fragment trước đó.
     */
    public void navigateBack() {
        navController.popBackStack();
    }
}
