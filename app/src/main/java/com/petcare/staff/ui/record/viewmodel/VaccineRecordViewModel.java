package com.petcare.staff.ui.record.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.data.model.ui.VaccineRecord;
import com.petcare.staff.data.repository.RecordRepository;
import com.petcare.staff.data.repository.UserRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;

import java.util.List;

public class VaccineRecordViewModel extends AndroidViewModel {
    private final RecordRepository recordRepo;
    private final UserRepository userRepo;
    private MutableLiveData<User> vet = new MutableLiveData<>();
    private MutableLiveData<VaccineRecord> record = new MutableLiveData<>();
    public VaccineRecordViewModel(@NonNull Application application) {
        super(application);
        recordRepo = new RecordRepository(application.getApplicationContext());
        userRepo = new UserRepository(application.getApplicationContext());
    }

    public void loadVaccineRecord(String vaccineId){
        LiveData<VaccineRecord> liveData = recordRepo.getVaccineRecord(vaccineId);
        Observer<VaccineRecord> observer = new Observer<VaccineRecord>() {
            @Override
            public void onChanged(VaccineRecord vaccineRecord) {
                record.setValue(vaccineRecord);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public void loadVetInfo() {
        VaccineRecord current = record.getValue();
        if (current == null || current.getVetId() == null || current.getVetId().isEmpty())
            return;

        String vetId = current.getVetId();
        Log.d("VaccineViewModel", "Loading vet info for vetId: " + vetId);

        LiveData<User> userLiveData = userRepo.getUserInfoById(vetId);
        userLiveData.observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                vet.setValue(user);
                userLiveData.removeObserver(this);
            }
        });
    }

    public LiveData<User> getVet() {
        return vet;
    }
    public LiveData<VaccineRecord> getVaccineRecord() {
        return record;
    }

    public void createVaccineRecord(VaccineRecord record, RepositoryCallback callback)
    {
        recordRepo.createVaccineRecord(record, callback);
    }
}
