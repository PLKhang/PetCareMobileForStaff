package com.petcare.staff.ui.record.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.petcare.staff.data.model.ui.Prescription;
import com.petcare.staff.data.repository.RecordRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionViewModel extends AndroidViewModel {
    private final RecordRepository recordRepo;
    private MutableLiveData<List<Prescription>> prescriptionList = new MutableLiveData<>();
    private MutableLiveData<Prescription> selectedPrescription = new MutableLiveData<>();
    private boolean clearedOnce = false;

    public PrescriptionViewModel(@NonNull Application application) {
        super(application);
        recordRepo = new RecordRepository(application.getApplicationContext());
    }

    public void loadPrescriptionList(String medicalRecordId) {
        LiveData<Prescription> liveData = recordRepo.getPrescription(medicalRecordId);

        Observer<Prescription> observer = new Observer<Prescription>() {
            @Override
            public void onChanged(Prescription prescriptions) {
                List<Prescription> list = new ArrayList<>();
                list.add(prescriptions);
                prescriptionList.setValue(list);
            }
        };

        liveData.observeForever(observer);
    }

    public Prescription getPrescriptionDetail(String prescriptionId)
    {
        for (Prescription record: prescriptionList.getValue()){
            if (record.getId().equals(prescriptionId))
                return record;
        }

        return null;
    }

    public LiveData<List<Prescription>> getPrescriptionList() {
        return prescriptionList;
    }

    public void addPrescription(Prescription prescription)
    {
        List<Prescription> prescriptions = prescriptionList.getValue();
        assert prescriptions != null;
        prescriptions.add(prescription);
        setPrescriptionList(prescriptions);
    }

    public void removePrescription(int index) {
        List<Prescription> current = prescriptionList.getValue();
        if (index >= 0 && index < current.size()) {
            current.remove(index);
            prescriptionList.setValue(current);
        }
    }
    public void setPrescriptionList(List<Prescription> prescriptionList) {
        this.prescriptionList.setValue(prescriptionList);
    }

    public LiveData<Prescription> getSelectedPrescription() {
        return selectedPrescription;
    }

    public void setSelectedPrescription(Prescription selectedPrescription) {
        this.selectedPrescription.setValue(selectedPrescription);
    }

    public void clearPrescriptionList() {
        if (!clearedOnce) {
            prescriptionList.setValue(new ArrayList<>());
            clearedOnce = true;
        }
    }

    public void resetClearFlag() {
        clearedOnce = false;
    }
}
