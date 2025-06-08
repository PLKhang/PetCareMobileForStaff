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

    public void addSelectedService(Service service) {
        List<Service> current = selectedServices.getValue();
        assert current != null;
        if (!current.contains(service)) {
            current.add(service);
            selectedServices.setValue(current);
        } else {
            removeSelectedService(service);
        }
    }

    public void removeSelectedService(Service service) {
        List<Service> current = selectedServices.getValue();
        if (current.contains(service)) {
            current.remove(service);
            selectedServices.setValue(current);
        }
    }

    public LiveData<List<Service>> getSelectedServices() {
        return selectedServices;
    }

    public void clearSelectedService() {
        selectedServices.setValue(new ArrayList<>());
    }
}

