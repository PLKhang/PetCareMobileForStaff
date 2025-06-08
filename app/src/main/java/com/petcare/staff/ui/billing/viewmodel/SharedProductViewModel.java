package com.petcare.staff.ui.billing.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.data.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class SharedProductViewModel extends AndroidViewModel {
    private final ProductRepository repository;
    private final MutableLiveData<List<Product>> products = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> selectedProducts = new MutableLiveData<>();

    public SharedProductViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ProductRepository(application.getApplicationContext());
        loadAllProducts();

        //TO-DO
        selectedProducts.setValue(new ArrayList<>());
    }

    private void loadAllProducts() {
        LiveData<List<Product>> liveData = repository.getAllProducts();

        Observer<List<Product>> observer = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> productList) {
                products.setValue(productList);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public LiveData<List<Product>> getAllProducts() {
        return products;
    }

    public void addSeletedProduct(Product product) {
        List<Product> current = selectedProducts.getValue();
        if (!current.contains(product)) {
            current.add(product);
            selectedProducts.setValue(current);
        } else {
            removeSeletedProduct(product);
        }
    }

    public void removeSeletedProduct(Product product) {
        List<Product> current = selectedProducts.getValue();
        if (current.contains(product)) {
            current.remove(product);
            selectedProducts.setValue(current);
        }
    }

    public LiveData<List<Product>> getSelectedProducts() {
        return selectedProducts;
    }

    public void clearSelectedProducts() {
        selectedProducts.setValue(new ArrayList<>());
    }
}
