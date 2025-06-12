package com.petcare.staff.ui.customer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.petcare.staff.data.model.ui.Bill;
import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.data.model.ui.SimplifiedAppointment;
import com.petcare.staff.data.repository.AppointmentRepository;
import com.petcare.staff.data.repository.OrderRepository;
import com.petcare.staff.data.repository.PaymentRepository;
import com.petcare.staff.data.repository.PetRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;

import java.util.List;

public class CustomerDetailViewModel extends AndroidViewModel {
    private final AppointmentRepository appointmentRepo;
    private final PetRepository petRepo;
    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;
    private final MutableLiveData<List<SimplifiedAppointment>> appointments = new MutableLiveData<>();
    private final MutableLiveData<List<Pet>> pets = new MutableLiveData<>();
    private final MutableLiveData<List<Order>> orders = new MutableLiveData<>();
    private final MutableLiveData<List<Bill>> bills = new MutableLiveData<>();

    public CustomerDetailViewModel(@NonNull Application application) {
        super(application);
        this.appointmentRepo = new AppointmentRepository(application.getApplicationContext());
        this.petRepo = new PetRepository(application.getApplicationContext());
        this.orderRepo = new OrderRepository(application.getApplicationContext());
        this.paymentRepo = new PaymentRepository(application.getApplicationContext());
    }

    public void loadAppointmentList(String customerId){
        LiveData<List<SimplifiedAppointment>> liveData = appointmentRepo.getAppointmentsByCustomerId(customerId);

        Observer<List<SimplifiedAppointment>> observer = new Observer<List<SimplifiedAppointment>>() {
            @Override
            public void onChanged(List<SimplifiedAppointment> appointmentList) {
                appointments.setValue(appointmentList);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }
    public void loadPetList(String customerId){
        LiveData<List<Pet>> liveData = petRepo.getPetsByCustomerId(customerId);

        Observer<List<Pet>> observer = new Observer<List<Pet>>() {
            @Override
            public void onChanged(List<Pet> petList) {
                pets.setValue(petList);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }
    public void loadOrderList(String customerId){
        LiveData<List<Order>> liveData = orderRepo.getOrdersByCustomerId(customerId);

        Observer<List<Order>> observer = new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orderList) {
                orders.setValue(orderList);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }
    public void loadBillList(List<Order> orderList){
        LiveData<List<Bill>> liveData = paymentRepo.getBillListByOrders(orderList);

        Observer<List<Bill>> observer = new Observer<List<Bill>>() {
            @Override
            public void onChanged(List<Bill> billList) {
                bills.setValue(billList);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public LiveData<List<SimplifiedAppointment>> getAppointments() {
        return appointments;
    }

    public LiveData<List<Pet>> getPets() {
        return pets;
    }

    public LiveData<List<Order>> getOrders() {
        return orders;
    }

    public LiveData<List<Bill>> getBills() {
        return bills;
    }

    public void clear() {
        appointments.setValue(null);
        pets.setValue(null);
        orders.setValue(null);
        bills.setValue(null);
    }
}
