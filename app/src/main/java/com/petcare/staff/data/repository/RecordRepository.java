package com.petcare.staff.data.repository;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.record.CreateExaminationRequest;
import com.petcare.staff.data.model.api.record.CreatePrescriptionRequest;
import com.petcare.staff.data.model.api.record.CreateVaccinationRequest;
import com.petcare.staff.data.model.api.record.ExaminationResponse;
import com.petcare.staff.data.model.api.record.IdResponse;
import com.petcare.staff.data.model.api.record.PrescriptionResponse;
import com.petcare.staff.data.model.api.record.UpdatePetRequest;
import com.petcare.staff.data.model.api.record.VaccinationResponse;
import com.petcare.staff.data.model.mapper.RecordMapper;
import com.petcare.staff.data.model.mapper.UserMapper;
import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.data.model.ui.MedicalRecord;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.data.model.ui.Prescription;
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

    public RecordRepository(Context context) {
        apiRecord = ApiClient.getRecordApi(context);
    }

    public LiveData<List<MedicalRecord>> getMedicalRecords(String petId) {
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

    public LiveData<List<VaccineRecord>> getVaccineRecords(String petId) {
        MutableLiveData<List<VaccineRecord>> liveData = new MutableLiveData<>();
        apiRecord.listVaccinationsByPet(petId).enqueue(new Callback<List<VaccinationResponse>>() {
            @Override
            public void onResponse(Call<List<VaccinationResponse>> call, Response<List<VaccinationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<VaccineRecord> records = RecordMapper.toVaccineRecordList(response.body());
                    Log.d("API_DEBUG", "VaccineRecord: " + records.size());
                    liveData.setValue(records);
                } else {
                    Log.d("API_DEBUG", "Null body");
                    liveData.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<VaccinationResponse>> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get vaccine records");
                liveData.setValue(new ArrayList<>());
            }
        });

        return liveData;
    }

    public LiveData<VaccineRecord> getVaccineRecord(String vaccineId) {
        MutableLiveData<VaccineRecord> liveData = new MutableLiveData<>();
        apiRecord.getVaccination(vaccineId).enqueue(new Callback<VaccinationResponse>() {
            @Override
            public void onResponse(Call<VaccinationResponse> call, Response<VaccinationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VaccineRecord record = RecordMapper.toVaccineRecord(response.body());
                    Log.d("API_DEBUG", "VaccineRecord id: " + record.getId());
                    liveData.setValue(record);
                } else {
                    Log.d("API_DEBUG", "Null body");
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<VaccinationResponse> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get vaccine record");
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    public void createVaccineRecord(VaccineRecord record, RepositoryCallback callback) {
        CreateVaccinationRequest request = RecordMapper.toCreateVaccinationRequest(record);
        apiRecord.createVaccination(request).enqueue(new Callback<IdResponse>() {
            @Override
            public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getId());
                } else {
                    callback.onError(new Exception("Tạo thất bại: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<IdResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void craetePrescription(Prescription p, RepositoryCallback repositoryCallback) {
        CreatePrescriptionRequest request = RecordMapper.toCreatePrescriptionRequest(p);
        apiRecord.createPrescription(request).enqueue(new Callback<IdResponse>() {
            @Override
            public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    repositoryCallback.onSuccess(response.body().getId());
                } else {
                    repositoryCallback.onError(new Exception("Null body"));
                }
            }

            @Override
            public void onFailure(Call<IdResponse> call, Throwable t) {
                repositoryCallback.onError(t);
            }
        });
    }

    public void createMedicalRecord(MedicalRecord record, RepositoryCallback repositoryCallback) {
        CreateExaminationRequest request = RecordMapper.toCreateExaminationRequest(record);
        apiRecord.createExamination(request).enqueue(new Callback<IdResponse>() {
            @Override
            public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    repositoryCallback.onSuccess(response.body().getId());
                } else {
                    repositoryCallback.onError(new Exception("Null body"));
                }
            }

            @Override
            public void onFailure(Call<IdResponse> call, Throwable t) {
                repositoryCallback.onError(t);
            }
        });
    }

    public LiveData<MedicalRecord> getMedicalRecord(String id) {
        MutableLiveData<MedicalRecord> liveData = new MutableLiveData<>();
        apiRecord.getExamination(id).enqueue(new Callback<ExaminationResponse>() {
            @Override
            public void onResponse(Call<ExaminationResponse> call, Response<ExaminationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MedicalRecord record = RecordMapper.toMedicalRecord(response.body());
                    Log.d("API_DEBUG", "Medical record ID: " + record.getId());
                    liveData.setValue(record);
                } else {
                    Log.d("API_DEBUG", "Null body");
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ExaminationResponse> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get medical record");
                liveData.setValue(null);
            }
        });

        return liveData;
    }

//    public LiveData<List<Prescription>> getPrescription(String medicalRecordId) {
//        MutableLiveData<List<Prescription>> liveData = new MutableLiveData<>();
//        apiRecord.listPrescriptionsByExamination(medicalRecordId).enqueue(new Callback<List<PrescriptionResponse>>() {
//            @Override
//            public void onResponse(Call<List<PrescriptionResponse>> call, Response<List<PrescriptionResponse>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Prescription> list = RecordMapper.toPrescriptionList(response.body());
//                    liveData.setValue(list);
//                } else {
//                    Log.d("API_DEBUG", "Null body");
//                    liveData.setValue(null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<PrescriptionResponse>> call, Throwable t) {
//                Log.d("API_DEBUG", "Failure to get list of prescriptions");
//                liveData.setValue(null);
//            }
//        });
//
//        return liveData;
//    }

    public LiveData<Prescription> getPrescription(String medicalRecordId) {
        MutableLiveData<Prescription> liveData = new MutableLiveData<>();
        apiRecord.listPrescriptionsByExamination(medicalRecordId).enqueue(new Callback<PrescriptionResponse>() {
            @Override
            public void onResponse(Call<PrescriptionResponse> call, Response<PrescriptionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Prescription list = RecordMapper.toPrescription(response.body());
                    liveData.setValue(list);
                } else {
                    Log.d("API_DEBUG", "Null body");
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PrescriptionResponse> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get list of prescriptions");
                liveData.setValue(null);
            }
        });

        return liveData;
    }
}
