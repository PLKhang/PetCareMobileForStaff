package com.petcare.staff.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.record.ExaminationResponse;
import com.petcare.staff.data.model.api.record.UpdatePetRequest;
import com.petcare.staff.data.model.api.record.VaccinationResponse;
import com.petcare.staff.data.model.mapper.RecordMapper;
import com.petcare.staff.data.model.mapper.UserMapper;
import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.data.model.ui.MedicalRecord;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.data.model.ui.VaccineRecord;
import com.petcare.staff.data.remote.RecordApi;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.utils.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordRepository {
    private final RecordApi apiRecord;
    public RecordRepository(Context context)
    {
        apiRecord = ApiClient.getRecordApi(context);
    }

    public LiveData<List<MedicalRecord>> getMedicalRecords(String petId)
    {
        MutableLiveData<List<MedicalRecord>> medicalLiveData = new MutableLiveData<>();
        apiRecord.listExaminationsByPet(petId).enqueue(new Callback<List<ExaminationResponse>>() {
            @Override
            public void onResponse(Call<List<ExaminationResponse>> call, Response<List<ExaminationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MedicalRecord> medicalRecords = RecordMapper.toMedicalRecordList(response.body());
                    Log.d("API_DEBUG", "MedicalRecord: " + medicalRecords.size());
                    medicalLiveData.setValue(medicalRecords);
                } else {
                    Log.d("API_DEBUG", "No body");
                    medicalLiveData.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<ExaminationResponse>> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get medical records");
                medicalLiveData.setValue(new ArrayList<>());
            }
        });

        return medicalLiveData;
    }

    public LiveData<List<VaccineRecord>> getVaccineRecords(String petId)
    {
        MutableLiveData<List<VaccineRecord>> vaccineLiveData = new MutableLiveData<>();
        apiRecord.listVaccinationsByPet(petId).enqueue(new Callback<List<VaccinationResponse>>() {
            @Override
            public void onResponse(Call<List<VaccinationResponse>> call, Response<List<VaccinationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<VaccineRecord> vaccineRecords = RecordMapper.toVaccineRecordList(response.body());
                    Log.d("API_DEBUG", "VaccineRecord: " + vaccineRecords.size());
                    vaccineLiveData.setValue(vaccineRecords);
                } else {
                    Log.d("API_DEBUG", "No body");
                    vaccineLiveData.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<VaccinationResponse>> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get vaccine records");
                vaccineLiveData.setValue(new ArrayList<>());
            }
        });

        return vaccineLiveData;
    }
}
