package com.petcare.staff.ui.billing.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.petcare.staff.data.model.ui.Service;
import com.petcare.staff.data.repository.AppointmentRepository;

import java.util.ArrayList;
import java.util.List;

public class SharedServiceViewModel extends AndroidViewModel {

    private final AppointmentRepository repository;
    private final MutableLiveData<List<Service>> services = new MutableLiveData<>();
    private final MutableLiveData<List<Service>> selectedServices = new MutableLiveData<>();
    private boolean clearedOnce = false;

    public SharedServiceViewModel(@NonNull Application application) {
        super(application);
        repository = new AppointmentRepository(application.getApplicationContext());
        loadServices();

        selectedServices.setValue(new ArrayList<>());
    }

    private void loadServices() {
        LiveData<List<Service>> liveData = repository.getAllServices();

        Observer<List<Service>> observer = new Observer<List<Service>>() {
            @Override
            public void onChanged(List<Service> serviceList) {
                services.setValue(serviceList);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public LiveData<List<Service>> getServices() {
        return services;
    }

    private Service findServiceById(List<Service> list, String id) {
        for (Service s : list) {
            if (s.getId().equals(id)) return s;
        }
        return null;
    }

    public void addSelectedService(Service service) {
        List<Service> current = selectedServices.getValue();
        if (current == null) current = new ArrayList<>();

        Service existing = findServiceById(current, service.getId());
        if (existing != null) {
            current.remove(existing);
        } else {
            current.add(service);
        }

        selectedServices.setValue(current);
    }


    public LiveData<List<Service>> getSelectedServices() {
        return selectedServices;
    }

    public void clearSelectedService() {
        if (!clearedOnce) {
            selectedServices.setValue(new ArrayList<>());
            clearedOnce = true;
        }
    }

    public void resetClearFlag() {
        clearedOnce = false;
    }

    public void setSelectedServicesById(List<Service> selectedFromAppointment) {
        List<Service> all = services.getValue();
        if (all == null) return;

        List<Service> selected = new ArrayList<>();
        for (Service sFromAppointment : selectedFromAppointment) {
            for (Service s : all) {
                if (s.getId().equals(sFromAppointment.getId())) {
                    Service copy = new Service(s);
                    copy.setQuantity(sFromAppointment.getQuantity());
                    selected.add(copy);
                    break;
                }
            }
        }
        selectedServices.setValue(selected);
    }

}

