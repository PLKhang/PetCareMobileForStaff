package com.petcare.staff.ui.userprofile.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.petcare.staff.data.model.api.user.ChangeInfoRequest;
import com.petcare.staff.data.model.api.user.ChangeInfoResponse;
import com.petcare.staff.data.model.api.user.ChangePasswordRequest;
import com.petcare.staff.data.model.api.user.ChangePasswordResponse;
import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.data.repository.UserRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileViewModel extends AndroidViewModel {
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final UserRepository userRepository;

    public UserProfileViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void loadCurrentUser() {
        LiveData<User> liveData = userRepository.getCurrentUser();

        Observer<User> observer = new Observer<User>() {
            @Override
            public void onChanged(User response) {
                userLiveData.setValue(response);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public void setUser(User user) {
        userLiveData.setValue(user);
    }

    public void clear() {
        userLiveData.setValue(null);
    }

    public void changeInfo(ChangeInfoRequest request, RepositoryCallback callback) {
        userRepository.changeInfo(request, callback);
    }

    public void changePassword(ChangePasswordRequest request, RepositoryCallback callback) {
        userRepository.changePassword(request, callback);
    }
}
