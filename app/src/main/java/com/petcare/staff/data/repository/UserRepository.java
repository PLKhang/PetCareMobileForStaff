package com.petcare.staff.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.user.ChangeInfoRequest;
import com.petcare.staff.data.model.api.user.ChangeInfoResponse;
import com.petcare.staff.data.model.api.user.ChangePasswordRequest;
import com.petcare.staff.data.model.api.user.ChangePasswordResponse;
import com.petcare.staff.data.model.api.user.LoginCallback;
import com.petcare.staff.data.model.api.user.LoginRequest;
import com.petcare.staff.data.model.api.user.LoginResponse;
import com.petcare.staff.data.model.api.user.UserByIdResponse;
import com.petcare.staff.data.model.api.user.UserResponse;
import com.petcare.staff.data.model.mapper.UserMapper;
import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.ui.activity.LoginActivity;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.utils.ApiClient;
import com.petcare.staff.data.remote.UserApi;
import com.petcare.staff.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserApi userApi;
    private final SharedPreferences userPref;

    public UserRepository(Context context) {
        this.userApi = ApiClient.getUserApi(context);
        this.userPref = context.getSharedPreferences(SharedPrefManager.PREF_NAME, Context.MODE_PRIVATE);
    }

    public LiveData<User> getUserInfoById(@NonNull String id) {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        userApi.getUserInfoById(Integer.parseInt(id)).enqueue(new Callback<UserByIdResponse>() {
            @Override
            public void onResponse(Call<UserByIdResponse> call, Response<UserByIdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = UserMapper.fromUserByIdResponseToUser(response.body());
                    Log.d("API_DEBUG", "User id: " + user.getId());
                    userMutableLiveData.setValue(user);
                } else {

                    Log.d("API_DEBUG", "Fail, code: " + response.code());
                    userMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserByIdResponse> call, Throwable t) {

                Log.d("API_DEBUG", "Fail" + t.getMessage());
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }

    public LiveData<User> getCurrentUser() {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        userApi.getCurrentUser().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = UserMapper.toUser(response.body());
                    Log.d("API_DEBUG", "User id: " + user.getId());
                    userMutableLiveData.setValue(user);
                } else {
                    userMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }

    public void login(String email, String password, boolean rememberMe, LoginCallback loginCallback) {
        LoginRequest request = new LoginRequest(email, password, rememberMe);

        userApi.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    Log.d("API_DEBUG", "TOKEN: " + token);

                    userPref.edit().putString(SharedPrefManager.KEY_TOKEN, token).apply();

                    if (rememberMe) {
                        userPref.edit().putBoolean(SharedPrefManager.KEY_REMEMBER, true).apply();
                    } else {
                        userPref.edit().putBoolean(SharedPrefManager.KEY_REMEMBER, false).apply();
                    }
                    loginCallback.onSuccess(token);
                } else {
                    loginCallback.onError("Đăng nhập thất bại: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginCallback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void changeInfo(ChangeInfoRequest request, RepositoryCallback callback) {
        userApi.changeInfo(request).enqueue(new Callback<ChangeInfoResponse>() {
            @Override
            public void onResponse(Call<ChangeInfoResponse> call, Response<ChangeInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getStatus());
                } else {
                    callback.onError(new Exception("Cập nhật thất bại: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<ChangeInfoResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void changePassword(ChangePasswordRequest request, RepositoryCallback callback) {
        userApi.changePassword(request).enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getStatus());
                } else {
                    callback.onError(new Exception("Đổi mật khẩu thất bại: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
