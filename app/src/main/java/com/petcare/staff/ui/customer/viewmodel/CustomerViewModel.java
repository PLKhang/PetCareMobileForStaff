package com.petcare.staff.ui.customer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.data.repository.CustomerRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;

import java.util.List;

public class CustomerViewModel extends AndroidViewModel {
    private final CustomerRepository repository;
    private final MutableLiveData<List<Customer>> customers = new MutableLiveData<>();

    public CustomerViewModel(@NonNull Application application) {
        super(application);
        this.repository = new CustomerRepository(application.getApplicationContext());

        loadAllCustomers();
    }

    public void loadAllCustomers() {
        LiveData<List<Customer>> liveData = repository.getAllCustomer();

        Observer<List<Customer>> observer = new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customerList) {
                customers.setValue(customerList);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public void addNewCustomer(Customer record, RepositoryCallback callback) {
        repository.addNewCustomer(record, callback);
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return customers;
    }

    public Customer getCustomerById(String customerId) {
        for (Customer c : customers.getValue()) {
            if (c.getId().equals(customerId))
                return c;
        }
        return null;
    }
}



