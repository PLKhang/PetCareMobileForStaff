package com.petcare.staff.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.appointment.AppointmentResponse;
import com.petcare.staff.data.model.api.record.PetResponse;
import com.petcare.staff.data.model.mapper.AppointmentMapper;
import com.petcare.staff.data.model.mapper.RecordMapper;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.data.model.ui.SimplifiedAppointment;
import com.petcare.staff.data.remote.RecordApi;
import com.petcare.staff.utils.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetRepository {
    private final RecordApi apiPet;

    public PetRepository(Context context)
    {
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


}
