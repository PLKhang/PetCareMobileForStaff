package com.petcare.staff.ui.pet.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.petcare.staff.data.model.ui.MedicalRecord;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.data.model.ui.VaccineRecord;
import com.petcare.staff.data.repository.PetRepository;
import com.petcare.staff.data.repository.RecordRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;

import java.util.ArrayList;
import java.util.List;

public class PetDetailViewModel extends AndroidViewModel {
    private final RecordRepository recordRepo;
    private final PetRepository petRepo;
    private final MutableLiveData<List<MedicalRecord>> medicalRecords = new MutableLiveData<>();
    private final MutableLiveData<List<VaccineRecord>> vaccineRecords = new MutableLiveData<>();
    private boolean clearedOnce = false;

    public PetDetailViewModel(@NonNull Application application) {
        super(application);

        this.petRepo = new PetRepository(application.getApplicationContext());
        this.recordRepo = new RecordRepository(application.getApplicationContext());
    }

    public void loadMedicalRecords(String petId) {
        LiveData<List<MedicalRecord>> liveData = recordRepo.getMedicalRecords(petId);

        Observer<List<MedicalRecord>> observer = new Observer<List<MedicalRecord>>() {
            @Override
            public void onChanged(List<MedicalRecord> records) {
                medicalRecords.setValue(records);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public void loadVaccineRecords(String petId) {
        LiveData<List<VaccineRecord>> liveData = recordRepo.getVaccineRecords(petId);

        Observer<List<VaccineRecord>> observer = new Observer<List<VaccineRecord>>() {
            @Override
            public void onChanged(List<VaccineRecord> records) {
                vaccineRecords.setValue(records);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public LiveData<List<MedicalRecord>> getMedicalRecords() {
        return medicalRecords;
    }

    public LiveData<List<VaccineRecord>> getVaccineRecords() {
        return vaccineRecords;
    }

    public void changeInfo(Pet pet, RepositoryCallback callback) {
        petRepo.changeInfo(pet, callback);
    }

    public void clear() {
        if (! this.clearedOnce) {
            medicalRecords.setValue(new ArrayList<>());
            vaccineRecords.setValue(new ArrayList<>());
            this.clearedOnce = true;
        }
    }


    public void resetClearFlag() {
        this.clearedOnce = false;
    }
}
