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

import java.util.ArrayList;
import java.util.List;

public class AppointmentDetailViewModel extends AndroidViewModel {
    private final AppointmentRepository appointmentRepo;
    private final UserRepository userRepo;
    private final MutableLiveData<Appointment> appointment = new MutableLiveData<>();
    private final MutableLiveData<User> assignedUser = new MutableLiveData<>();
    private List<Product> allProduct;
    private List<Service> allService;
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
                updatePrice(response);
                appointment.setValue(response);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    private void updatePrice(Appointment appointment) {
        for (Service s: appointment.getServices()) {
            for (Service service: allService)
            {
                if (s.getId().equals(service.getId()))
                {
                    service.setQuantity(s.getQuantity());
                    s.copy(service);
                }
            }
        }

        if (appointment.getOrder() != null)
        {
            for (Product p: appointment.getOrder().getProducts()) {
                for (Product product: allProduct) {
                    if (p.getId().equals(product.getId()) && p.getType().equals(product.getType())) {
                        product.setQuantity(p.getQuantity());
                        p.copy(product);
                    }
                }
            }
        }
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
        return appointment;
    }

    public void setServiceList(List<Service> serviceList) {
        this.allService = new ArrayList<>(serviceList);
    }

    public void setProductList(List<Product> productList) {
        this.allProduct = new ArrayList<>(productList);
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
    }

    public void updateAppointmentStatus(String appointmentId, AppointmentStatus status, RepositoryCallback callback)
    {
        appointmentRepo.updateAppointmentStatus(appointmentId, status, callback);
    }

    public void setStatus(AppointmentStatus selectedStatus) {
        Appointment current = appointment.getValue();
        if (current == null) return;

        current.setStatus(selectedStatus);
        appointment.setValue(current);
    }

    public void updateStockForAllProductList(List<Product> listOfStock) {
        List<Product> current = new ArrayList<>(allProduct);

        List<Product> updatedList = new ArrayList<>();
        for (Product des: current) {
            for (Product p: listOfStock) {
                if (p.getId().equals(des.getId()) && p.getType().equals(des.getType())){
                    Product temp = new Product(des);
                    temp.setStock(p.getStock());
                    updatedList.add(temp);
                }
            }
        }

        this.allProduct = updatedList;
    }
}
