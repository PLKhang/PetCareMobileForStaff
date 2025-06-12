package com.petcare.staff.ui.appointment.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.petcare.staff.data.model.ui.Appointment;
import com.petcare.staff.data.model.ui.SimplifiedAppointment;
import com.petcare.staff.data.repository.AppointmentRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;

import java.util.List;

public class AppointmentListViewModel extends AndroidViewModel {
    private final AppointmentRepository repository;
    private final MutableLiveData<List<SimplifiedAppointment>> userAppointment = new MutableLiveData<>();
    private final MutableLiveData<List<SimplifiedAppointment>> branchAppoitment = new MutableLiveData<>();
    private final MutableLiveData<List<SimplifiedAppointment>> allAppointment = new MutableLiveData<>();

    public AppointmentListViewModel(@NonNull Application application) {
        super(application);
        this.repository = new AppointmentRepository(application.getApplicationContext());
    }

    public void loadOtherAppointment(String currentBranchId) {
    }

    public void loadBranchAppointment(String branchId) {
        LiveData<List<SimplifiedAppointment>> liveData = repository.getAppointmentByBranchId(branchId);

        Observer<List<SimplifiedAppointment>> observer = new Observer<List<SimplifiedAppointment>>() {
            @Override
            public void onChanged(List<SimplifiedAppointment> appointmentList) {
                branchAppoitment.setValue(appointmentList);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public void loadUserAppointment(String userId) {
        Log.d("DEBUG", "User id: " + userId);
        LiveData<List<SimplifiedAppointment>> liveData = repository.getAppointmentByUserId(userId);

        Observer<List<SimplifiedAppointment>> observer = new Observer<List<SimplifiedAppointment>>() {
            @Override
            public void onChanged(List<SimplifiedAppointment> appointmentList) {
                userAppointment.setValue(appointmentList);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public void createAppointment(Appointment appointment, RepositoryCallback callback) {
        repository.createAppointment(appointment, callback);
    }

    public LiveData<List<SimplifiedAppointment>> getBranchAppointment() {
        Log.d("APPOINTMENT", "Branch size: " + branchAppoitment.getValue().size());
        return branchAppoitment;
    }

    public LiveData<List<SimplifiedAppointment>> getCurrentUserAppointment() {
        Log.d("APPOINTMENT", "User size: " + userAppointment.getValue().size());
        return userAppointment;
    }

    public LiveData<List<SimplifiedAppointment>> getOtherAppointment() {
        return allAppointment;
    }
}
