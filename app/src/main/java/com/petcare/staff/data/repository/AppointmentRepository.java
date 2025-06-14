package com.petcare.staff.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.appointment.AppointmentDetailResponse;
import com.petcare.staff.data.model.api.appointment.AppointmentResponse;
import com.petcare.staff.data.model.api.appointment.AppointmentStatus;
import com.petcare.staff.data.model.api.appointment.CreateAppointmentRequest;
import com.petcare.staff.data.model.api.appointment.CreateAppointmentResponse;
import com.petcare.staff.data.model.api.appointment.ServiceResponse;
import com.petcare.staff.data.model.api.appointment.UpdateAppointmentEmployeeRequest;
import com.petcare.staff.data.model.api.appointment.UpdateAppointmentEmployeeResponse;
import com.petcare.staff.data.model.api.appointment.UpdateAppointmentStatusRequest;
import com.petcare.staff.data.model.api.appointment.UpdateAppointmentStatusResponse;
import com.petcare.staff.data.model.mapper.AppointmentMapper;
import com.petcare.staff.data.model.ui.Appointment;
import com.petcare.staff.data.model.ui.Service;
import com.petcare.staff.data.model.ui.SimplifiedAppointment;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.utils.ApiClient;
import com.petcare.staff.data.remote.AppointmentApi;

import java.util.List;
import java.util.SortedMap;

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

    public LiveData<List<SimplifiedAppointment>> getAppointmentByUserId(String id) {
        MutableLiveData<List<SimplifiedAppointment>> appointmentLiveData = new MutableLiveData<>();
        apiAppointment.getAppointmentsByEmployee(Integer.parseInt(id)).enqueue(new Callback<List<AppointmentResponse>>() {
            @Override
            public void onResponse(Call<List<AppointmentResponse>> call, Response<List<AppointmentResponse>> response) {
                if (response.isSuccessful() && response.body() != null)
                {
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

    public LiveData<List<SimplifiedAppointment>> getAppointmentByBranchId(String id) {
        MutableLiveData<List<SimplifiedAppointment>> appointmentLiveData = new MutableLiveData<>();
        apiAppointment.getAppointmentsByBranch(Integer.parseInt(id)).enqueue(new Callback<List<AppointmentResponse>>() {
            @Override
            public void onResponse(Call<List<AppointmentResponse>> call, Response<List<AppointmentResponse>> response) {
                if (response.isSuccessful() && response.body() != null)
                {
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

    public void createAppointment(Appointment appointment, RepositoryCallback callback) {
        CreateAppointmentRequest request = AppointmentMapper.toCreateAppointmentRequest(appointment);
        apiAppointment.createAppointment(request).enqueue(new Callback<CreateAppointmentResponse>() {
            @Override
            public void onResponse(Call<CreateAppointmentResponse> call, Response<CreateAppointmentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // response trả về có ID trong Appointment
                    String createdId = String.valueOf(response.body().getAppointment_id());
                    callback.onSuccess(createdId);
                } else {
                    callback.onError(new Exception("Failed to create appointment. Response code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<CreateAppointmentResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public LiveData<Appointment> getAppointmentDetail(String appointmentId) {
        MutableLiveData<Appointment> liveData = new MutableLiveData<>();
        apiAppointment.getAppointmentDetails(Integer.parseInt(appointmentId)).enqueue(new Callback<AppointmentDetailResponse>() {
            @Override
            public void onResponse(Call<AppointmentDetailResponse> call, Response<AppointmentDetailResponse> response) {
                if (response.isSuccessful() && response.body()!= null)
                {
                    Appointment appointment = AppointmentMapper.toAppointmentDetail(response.body());
                    Log.d("API_DEBUG", "Appointment total: " + appointment.getTotal());
                    liveData.setValue(appointment);
                }
                else {
                    Log.d("API_DEBUG", "Null body or fail response");
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AppointmentDetailResponse> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get appointment detail");
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    public void updateAppointmentStatus(Appointment current, RepositoryCallback callback) {
        UpdateAppointmentStatusRequest request = new UpdateAppointmentStatusRequest(current.getId(), current.getStatus());
        apiAppointment.updateAppointmentStatus(request).enqueue(new Callback<UpdateAppointmentStatusResponse>() {
            @Override
            public void onResponse(Call<UpdateAppointmentStatusResponse> call, Response<UpdateAppointmentStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    Log.d("API_DEBUG", "Appointment status: " + response.body().getStatus());
                    callback.onSuccess(response.body().getStatus());
                }
                else {
                    Log.d("API_DEBUG", "Appointment status: fail");
                    callback.onError(new Exception("Null body"));
                }
            }

            @Override
            public void onFailure(Call<UpdateAppointmentStatusResponse> call, Throwable t) {

                Log.d("API_DEBUG", "fail");
                callback.onError(new Exception("Failure: " + t.getMessage()));
            }
        });
    }

    public void updateAppointmentStatus(String id, AppointmentStatus status, RepositoryCallback callback) {
        UpdateAppointmentStatusRequest request = new UpdateAppointmentStatusRequest(id, status);
        apiAppointment.updateAppointmentStatus(request).enqueue(new Callback<UpdateAppointmentStatusResponse>() {
            @Override
            public void onResponse(Call<UpdateAppointmentStatusResponse> call, Response<UpdateAppointmentStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    Log.d("API_DEBUG", "Appointment status: " + response.body().getStatus());
                    callback.onSuccess(response.body().getStatus());
                }
                else {
                    Log.d("API_DEBUG", "Appointment status: fail");
                    callback.onError(new Exception("Null body"));
                }
            }

            @Override
            public void onFailure(Call<UpdateAppointmentStatusResponse> call, Throwable t) {

                Log.d("API_DEBUG", "fail");
                callback.onError(new Exception("Failure: " + t.getMessage()));
            }
        });
    }

    public void updateEmployeeForAppointment(Appointment temp, RepositoryCallback callback) {
        UpdateAppointmentEmployeeRequest request = AppointmentMapper.toUpdateAppointmentEmployeeRequest(temp);
        apiAppointment.updateAppointmentEmployee(request).enqueue(new Callback<UpdateAppointmentEmployeeResponse>() {
            @Override
            public void onResponse(Call<UpdateAppointmentEmployeeResponse> call, Response<UpdateAppointmentEmployeeResponse> response) {
                if (response.isSuccessful()&&response.body()!= null)
                {
                    callback.onSuccess("Success");
                }
                else {
                    callback.onError(new Exception("Null body"));
                }
            }

            @Override
            public void onFailure(Call<UpdateAppointmentEmployeeResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
