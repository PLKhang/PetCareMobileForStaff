package com.petcare.staff.data.remote;

import com.petcare.staff.data.model.api.appointment.AppointmentDetailResponse;
import com.petcare.staff.data.model.api.appointment.AppointmentResponse;
import com.petcare.staff.data.model.api.appointment.CreateAppointmentRequest;
import com.petcare.staff.data.model.api.appointment.CreateAppointmentResponse;
import com.petcare.staff.data.model.api.appointment.ServiceResponse;
import com.petcare.staff.data.model.api.appointment.UpdateAppointmentEmployeeRequest;
import com.petcare.staff.data.model.api.appointment.UpdateAppointmentEmployeeResponse;
import com.petcare.staff.data.model.api.appointment.UpdateAppointmentStatusRequest;
import com.petcare.staff.data.model.api.appointment.UpdateAppointmentStatusResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AppointmentApi {

    // ------------------ Appointment ------------------

    @POST("api/v1/appointments")
    Call<CreateAppointmentResponse> createAppointment(@Body CreateAppointmentRequest appointment);

    @GET("api/v1/appointments/customer/{customer_id}")
    Call<List<AppointmentResponse>> getAppointmentsByCustomer(@Path("customer_id") int customerId);

    @GET("api/v1/appointments/employee/{employee_id}")
    Call<List<AppointmentResponse>> getAppointmentsByEmployee(@Path("employee_id") int employeeId);

    @GET("api/v1/appointments/branch/{branch_id}")
    Call<List<AppointmentResponse>> getAppointmentsByBranch(@Path("branch_id") int branchId);

    @PUT("api/v1/appointments/update-status")
    Call<UpdateAppointmentStatusResponse> updateAppointmentStatus(@Body UpdateAppointmentStatusRequest request);

    @PUT("api/v1/appointments/update-employee")
    Call<UpdateAppointmentEmployeeResponse> updateAppointmentEmployee(@Body UpdateAppointmentEmployeeRequest request);

    @GET("api/v1/appointments/{appointment_id}")
    Call<AppointmentDetailResponse> getAppointmentDetails(@Path("appointment_id") int appointmentId);

    // ------------------ Service ------------------
    @GET("api/v1/services")
    Call<List<ServiceResponse>> getAllServices();

    @GET("services/{id}")
    Call<ServiceResponse> getServiceById(@Path("id") int serviceId);

}
