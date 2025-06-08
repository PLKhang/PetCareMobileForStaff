package com.petcare.staff.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.appointment.AppointmentResponse;
import com.petcare.staff.data.model.api.appointment.ServiceResponse;
import com.petcare.staff.data.model.mapper.AppointmentMapper;
import com.petcare.staff.data.model.ui.Service;
import com.petcare.staff.data.model.ui.SimplifiedAppointment;
import com.petcare.staff.utils.ApiClient;
import com.petcare.staff.data.remote.AppointmentApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentRepository {

    private final AppointmentApi apiAppointment;

    public AppointmentRepository(Context context) {
        apiAppointment = ApiClient.getAppointmentApi(context);
    }

    public LiveData<List<Service>> getAllServices() {
        MutableLiveData<List<Service>> servicesLiveData = new MutableLiveData<>();
        apiAppointment.getAllServices().enqueue(new Callback<List<ServiceResponse>>() {
            @Override
            public void onResponse(Call<List<ServiceResponse>> call, Response<List<ServiceResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Service> serviceList = AppointmentMapper.toServiceList(response.body());
                    Log.d("API_DEBUG", "Service: " + serviceList.size());
                    servicesLiveData.setValue(serviceList);
                } else {
                    Log.d("API_DEBUG", "No body");
                    servicesLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<ServiceResponse>> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get services");
                servicesLiveData.setValue(null);
            }
        });
        return servicesLiveData;
    }

    public LiveData<List<SimplifiedAppointment>> getAppointmentsByCustomerId(String id) {
        MutableLiveData<List<SimplifiedAppointment>> appointmentLiveData = new MutableLiveData<>();
        apiAppointment.getAppointmentsByCustomer(Integer.parseInt(id)).enqueue(new Callback<List<AppointmentResponse>>() {
            @Override
            public void onResponse(Call<List<AppointmentResponse>> call, Response<List<AppointmentResponse>> response) {
                Log.d("API_DEBUG", "onResponse called");
                if (response.isSuccessful() && response.body() != null) {
                    List<SimplifiedAppointment> appointmentList = AppointmentMapper.toSimplifiedAppointmentList(response.body());
                    Log.d("API_DEBUG", "Appointment: " + appointmentList.size());
                    appointmentLiveData.setValue(appointmentList);
                } else {
                    Log.d("API_DEBUG", "onResponse: unsuccessful or null body");
                    appointmentLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<AppointmentResponse>> call, Throwable t) {
                Log.d("API_DEBUG", "onFailure called: " + t.getMessage());
                appointmentLiveData.setValue(null);
            }
        });

        return appointmentLiveData;
    }
}
