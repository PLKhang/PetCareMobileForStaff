package com.petcare.staff.ui.record.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.petcare.staff.data.model.ui.MedicalRecord;
import com.petcare.staff.data.model.ui.Prescription;
import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.data.repository.RecordRepository;
import com.petcare.staff.data.repository.UserRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;

public class MedicalRecordViewModel extends AndroidViewModel {
    private RecordRepository recordRepo;
    private UserRepository userRepo;
    private MutableLiveData<MedicalRecord> record = new MutableLiveData<>();
    private final MutableLiveData<User> user = new MutableLiveData<>();

    public MedicalRecordViewModel(@NonNull Application application) {
        super(application);
        this.recordRepo = new RecordRepository(application.getApplicationContext());
        this.userRepo = new UserRepository(application.getApplicationContext());
    }

    public void createMedicalRecord(MedicalRecord record, RepositoryCallback repositoryCallback) {
        recordRepo.createMedicalRecord(record, repositoryCallback);
    }

    public void createPrescription(Prescription p, RepositoryCallback repositoryCallback) {
        recordRepo.craetePrescription(p, repositoryCallback);
    }

    public void loadUser(String id) {
        LiveData<User> userLiveData = userRepo.getUserInfoById(id);

        Observer<User> observer = new Observer<User>() {
            @Override
            public void onChanged(User response) {
                user.setValue(response);
                userLiveData.removeObserver(this);
            }
        };

        userLiveData.observeForever(observer);
    }

    public void loadMedicalRecord(String id) {
        LiveData<MedicalRecord> liveData = recordRepo.getMedicalRecord(id);

        Observer<MedicalRecord> observer = new Observer<MedicalRecord>() {
            @Override
            public void onChanged(MedicalRecord medicalRecord) {
                record.setValue(medicalRecord);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public LiveData<User> getUser() {
        return user;
    }
}
