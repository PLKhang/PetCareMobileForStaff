package com.petcare.staff.ui.appointment.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.petcare.staff.data.model.api.appointment.AppointmentStatus;
import com.petcare.staff.data.model.ui.Appointment;
import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.data.model.ui.Service;
import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.data.repository.AppointmentRepository;
import com.petcare.staff.data.repository.UserRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;

import java.util.List;

public class AppointmentDetailViewModel extends AndroidViewModel {
    private final AppointmentRepository appointmentRepo;
    private final UserRepository userRepo;
    private final MutableLiveData<Appointment> appointment = new MutableLiveData<>();
    private final MutableLiveData<User> assignedUser = new MutableLiveData<>();

    public AppointmentDetailViewModel(@NonNull Application application) {
        super(application);
        this.appointmentRepo = new AppointmentRepository(application.getApplicationContext());
        this.userRepo = new UserRepository(application.getApplicationContext());
    }

    public void loadAppointmentDetail(String appointmentId) {
        LiveData<Appointment> liveData = appointmentRepo.getAppointmentDetail(appointmentId);
        Observer<Appointment> observer = new Observer<Appointment>() {
            @Override
            public void onChanged(Appointment response) {
                appointment.setValue(response);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public LiveData<User> getAssignedUser() {
        return assignedUser;
    }

    public void loadAssignedUser() {
        if (appointment.getValue() == null) return;

        String currentUserId = appointment.getValue().getEmployeeId();

        LiveData<User> userLiveData = userRepo.getUserInfoById(currentUserId);

        Observer<User> observer = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                assignedUser.setValue(user);
                userLiveData.removeObserver(this);
            }
        };

        userLiveData.observeForever(observer);
    }

    public LiveData<Appointment> getAppointmentDetail() {
        return this.appointment;
    }

    public void setProductList(List<Product> productList) {
        Appointment current = appointment.getValue();
        if (current == null || current.getOrder() == null) return;

        List<Product> selectedProducts = current.getOrder().getProducts();
        if (selectedProducts == null) return;

        for (Product p : selectedProducts) {
            for (Product ref : productList) {
                if (p.getId().equals(ref.getId())) {
                    p.setPrice(ref.getPrice()); // bổ sung giá
                    Log.d("APPOINTMENT_DETAIL", "Product: " + p.getId() + "; Quantity: " + p.getQuantity() + "; Price" + p.getPrice());
                    break;
                }
            }
        }

        appointment.setValue(current); // cập nhật LiveData
    }

    public void setServiceList(List<Service> serviceList) {
        Appointment current = appointment.getValue();
        if (current == null) return;

        List<Service> selectedServices = current.getServices();
        if (selectedServices == null) return;

        for (Service s : selectedServices) {
            for (Service ref : serviceList) {
                if (s.getId().equals(ref.getId())) {
                    s.setPrice(ref.getPrice()); // bổ sung giá
                    break;
                }
            }
        }

        appointment.setValue(current); // cập nhật LiveData
    }

    public void setAssignedUser(String userId, RepositoryCallback callback) {
        Appointment temp = appointment.getValue();
        temp.setEmployeeId(userId);
        appointment.setValue(temp);
        appointmentRepo.updateEmployeeForAppointment(temp, callback);
    }

    public void updateAppointmentStatus(AppointmentStatus status, RepositoryCallback callback) {
        Appointment current = appointment.getValue();
        if (current == null) return;

        current.setStatus(status);
        appointmentRepo.updateAppointmentStatus(current, callback);
        appointment.setValue(current); // cập nhật lại LiveData
    }
}
