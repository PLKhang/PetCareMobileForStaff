package com.petcare.staff.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.appointment.AppointmentResponse;
import com.petcare.staff.data.model.api.record.CreatePetRequest;
import com.petcare.staff.data.model.api.record.IdResponse;
import com.petcare.staff.data.model.api.record.PetResponse;
import com.petcare.staff.data.model.api.record.UpdatePetRequest;
import com.petcare.staff.data.model.mapper.AppointmentMapper;
import com.petcare.staff.data.model.mapper.RecordMapper;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.data.model.ui.SimplifiedAppointment;
import com.petcare.staff.data.remote.RecordApi;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.utils.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetRepository {
    private final RecordApi apiPet;

    public PetRepository(Context context) {
        apiPet = ApiClient.getRecordApi(context);
    }

    public LiveData<List<Pet>> getPetsByCustomerId(String id) {
        MutableLiveData<List<Pet>> petLiveData = new MutableLiveData<>();
        apiPet.listPetsByOwner(Integer.parseInt(id)).enqueue(new Callback<List<PetResponse>>() {
            @Override
            public void onResponse(Call<List<PetResponse>> call, Response<List<PetResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pet> petList = RecordMapper.toPetList(response.body());
                    Log.d("API_DEBUG", "Appointment: " + petList.size());
                    petLiveData.setValue(petList);
                } else {
                    Log.d("API_DEBUG", "No body");
                    petLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<PetResponse>> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get pet list");
                petLiveData.setValue(null);
            }
        });

        return petLiveData;
    }

    public void addNewPet(Pet pet, RepositoryCallback callback) {
        CreatePetRequest request = RecordMapper.toCreatePetRequest(pet);
        Call<IdResponse> call = apiPet.createPet(request);
        call.enqueue(new Callback<IdResponse>() {
            @Override
            public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String newPetId = response.body().getId();
                    callback.onSuccess("Create successful, petId: " + newPetId);
                } else {
                    callback.onError(new Exception("Error creating pet: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<IdResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void changeInfo(Pet pet, RepositoryCallback callback) {
        UpdatePetRequest request = RecordMapper.toUpdatePetRequest(pet);
        apiPet.updatePet(request).enqueue(new Callback<PetResponse>() {
            @Override
            public void onResponse(Call<PetResponse> call, Response<PetResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess("");
                } else {
                    callback.onError(new Exception("Cập nhật thất bại: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<PetResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
