package com.petcare.staff.ui.billing.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.petcare.staff.data.model.api.order.OrderStatus;
import com.petcare.staff.data.model.ui.Appointment;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.data.model.ui.Service;
import com.petcare.staff.data.repository.AppointmentRepository;
import com.petcare.staff.data.repository.OrderRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailViewModel extends AndroidViewModel {
    private final OrderRepository orderRepository;
    private final AppointmentRepository appointmentRepository;

    private final MutableLiveData<Order> orderLiveData = new MutableLiveData<>();
    private final MutableLiveData<Appointment> appointment = new MutableLiveData<>();
    private boolean loadedAppointment = false;
    private List<Service> allService;
    private List<Product> allProduct;

    public OrderDetailViewModel(@NonNull Application application) {
        super(application);
        this.orderRepository = new OrderRepository(application.getApplicationContext());
        this.appointmentRepository = new AppointmentRepository(application.getApplicationContext());
    }

    public void loadOrderDetail(String orderId) {
        LiveData<Order> liveData = orderRepository.getOrderDetailById(orderId);

        Observer<Order> observer = new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if (!loadedAppointment && order.getAppointment_id() != "null") {
                    Log.d("ORDER_DETAIL_VM", "appointment id: " + order.getAppointment_id());
                    loadAppointmentDetail(order.getAppointment_id());
                }
                else {
                    appointment.setValue(null);
                }
                updateOrderProduct(order);
                orderLiveData.setValue(order);
                for (Product p: order.getProducts()) {
                    Log.d("LoadOrderDetail", "Product: " + p);
                }
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    private void updateOrderProduct(Order order) {
        for (Product p: order.getProducts()) {
            for (Product product: allProduct) {
                if (p.getId().equals(product.getId()) && p.getType().equals(product.getType())) {
                    product.setQuantity(p.getQuantity());
                    p.copy(product);
                }
            }
        }
    }

    public void loadAppointmentDetail(String appointmentId) {
        loadedAppointment = true;

        LiveData<Appointment> liveData = appointmentRepository.getAppointmentDetail(appointmentId);
        Observer<Appointment> observer = new Observer<Appointment>() {
            @Override
            public void onChanged(Appointment response) {
                updateAppointmentPrice(response);
                appointment.setValue(response);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    private void updateAppointmentPrice(Appointment appointment) {
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
    }
    public LiveData<Order> getOrderDetail() {
        return orderLiveData;
    }
    public LiveData<Appointment> getApointmentDetail() {
        return appointment;
    }

    public void setServiceList(List<Service> serviceList) {
        allService = new ArrayList<>(serviceList);
    }
    public void updateOrderStatus(OrderStatus status, RepositoryCallback callback)
    {
        Order current = orderLiveData.getValue();
        if (current == null) return;

        orderRepository.updateOrderStatus(current.getId(), status, callback);
    }

    public void updateOrderStatus(String orderId, OrderStatus status, RepositoryCallback callback)
    {
        orderRepository.updateOrderStatus(orderId, status, callback);
    }
    public void setStatus(OrderStatus status)
    {
        Order current = orderLiveData.getValue();
        if (current == null) return;

        current.setStatus(status);
        orderLiveData.setValue(current);
    }

    public void setLoadedAppointment(boolean loaded) {
        loadedAppointment = loaded;
    }

    public void setProductList(List<Product> list) {
        allProduct = new ArrayList<>(list);
    }
}
